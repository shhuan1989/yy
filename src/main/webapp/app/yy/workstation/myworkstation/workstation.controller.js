(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('WorkstationController', WorkstationController);

    WorkstationController.$inject = ['$scope', '$state', '$timeout', 'WorkstationService', 'ProjectRate', 'PerformanceApprovalRequest', 'UserService', 'WksVacationService', 'WksWorkService', 'WksGoodsService', 'WksShootConfigService', 'WksShootBudgetService', 'WksShootCostService', 'StatisticsService', 'ShootAgendaService', 'EmployeeService', 'ExpenseService', 'AdminNoticeService', 'OtherIncomeService', 'MeetingService', 'Task'];

    function WorkstationController ($scope, $state, $timeout, WorkstationService, ProjectRate, PerformanceApprovalRequest, UserService, WksVacationService, WksWorkService, WksGoodsService, WksShootConfigService, WksShootBudgetService, WksShootCostService, StatisticsService, ShootAgendaService, EmployeeService, ExpenseService, AdminNoticeService, OtherIncomeService, MeetingService, Task) {
        var vm = this;
        console.log("WorkstationController Initialized");

        vm.rates = [];
        vm.auditRates = [];
        vm.bonusApprovals = [];
        vm.vacationApprovals = [];
        vm.workApprovals = [];
        vm.utilApplications = [];
        vm.shootConfigApprovals = [];
        vm.employeeProbations = [];
        vm.events = [];
        vm.currentUserId = -1;
        vm.paymentTimeRange = {};
        vm.invoiceTimeRange = {};
        vm.isLoadingPaymentStatistics = true;
        vm.isLoadingInvoiceStatistics = true;
        vm.paymentStatistics = {};
        vm.invoiceStatistics = {};
        vm.meetings = [];

        vm.loadAll = loadAll;

        UserService.currentUser().then(
            function (user) {
                vm.currentUserId = user != undefined ? user.id : -1;
                refresh();
            }
        );

        function refresh() {
            vm.loadAll();
            $timeout(refresh, 30000);
        }

        function loadAll() {
            var rateQuery = ProjectRate.query({onlyOwned: true, finished: false}).$promise;
            var perfQuery = PerformanceApprovalRequest.query({statusId: 1}).$promise;
            var vacationQuery = WksVacationService.query({approvalStatusId: 1}).$promise;
            var workQuery = WksWorkService.query({approvalStatusId: 1}).$promise;
            var utilAppQuery = WksGoodsService.query({approvalStatusId: 1}).$promise;
            var shootConfigQuery = WksShootConfigService.query({approvalStatusId: 1}).$promise;
            var budgetQuery = WksShootBudgetService.query({approvalStatusId: 1}).$promise;
            var shootCostQuery = WksShootCostService.query({approvalStatusId: 1}).$promise;
            var eventsQuery = ShootAgendaService.query().$promise;
            var empQuery = EmployeeService.query({hireDateTo: moment().subtract(3, 'months').add(7, 'days').valueOf(), jobPositionStatusId: 2}).$promise;
            var expenseQuery = ExpenseService.query({approvalStatusId: 1}).$promise;
            var incomeQuery = OtherIncomeService.query({approvalStatusId: 1}).$promise;
            var noticeQuery = AdminNoticeService.query({ownedOnly: true, validate: true}).$promise;
            var meetingQuery = MeetingService.query({currentUserOnly: true, statusId: 1}).$promise;
            var taskQuery = Task.query({shownOwnedOnly: true, statusIds: [-2, -1, 0]}).$promise;

            $.when(rateQuery, perfQuery, vacationQuery, workQuery, utilAppQuery, shootConfigQuery, budgetQuery, shootCostQuery, eventsQuery, empQuery, incomeQuery, expenseQuery, noticeQuery, meetingQuery, taskQuery).then(
                function (rateResp, perfResp, vacationResp, workResp, utiAppResp, shootConfigs, budgets, shootCosts, events, employees, incomes, expenses, notices, meetings, tasks) {
                    // rate
                    var rates = [];
                    var auditRates = [];
                    $.each(rateResp, function (index, item) {
                        if (item.audit == true) {
                            auditRates.push(item);
                        } else {
                            rates.push(item);
                        }
                    });
                    vm.rates = rates.slice(0, 5);
                    vm.auditRates = auditRates.slice(0, 5);

                    // bonus approvals
                    vm.bonusApprovals = extractApprovals(perfResp).slice(0, 5);

                    //vacation approvals
                    vm.vacationApprovals = extractApprovals(vacationResp).slice(0, 5);

                    //work approvals
                    vm.workApprovals = extractApprovals(workResp).slice(0, 5);

                    // utilities application
                    vm.utilApplications = extractApprovals(utiAppResp).slice(0, 5);

                    // shoot config approvals
                    vm.shootConfigApprovals = extractApprovals(shootConfigs).slice(0, 5);

                    // budges approvals
                    vm.budgetApprovals = extractApprovals(budgets).slice(0, 5);

                    // shoot cost approvals
                    vm.shootCostApprovals = extractApprovals(shootCosts).slice(0, 5);

                    // income approvals
                    vm.incomeApprovals = extractApprovals(incomes).slice(0, 5);

                    // expense approvals
                    vm.expenseApprovals = extractApprovals(expenses).slice(0, 5);

                    // probation alerts
                    vm.employeeProbations = employees.slice(0, 5);

                    // notices
                    vm.hasMoreNotices = notices.length > 5;
                    vm.notices = notices.slice(0, 5);

                    // meetings
                    vm.hasMoreMeetings = meetings.length > 5;
                    vm.meetings = meetings.slice(0, 5);;

                    // tasks
                    vm.hasMoreTasks = tasks.length > 5;
                    vm.tasks = tasks.slice(0, 5);

                    // calendar
                    vm.events = events;
                    $.each(vm.events, function (index, event) {
                        event.start = moment(event.startTime).toDate();
                        event.end = moment(event.endTime).toDate();
                        if (event.tooltip == undefined) {
                            event.tooltip = '主题：' + event.title + '<br>'
                                + '地点：' + event.location + '<br>'
                                + '开始时间：' + formatTimestampDmyHms(event.startTime) + '<br>'
                                + '结束时间：' + formatTimestampDmyHms(event.endTime);
                        }
                    });
                    initCalendar();

                    // payment statistics
                    updatePaymentChart();

                    // invoice statistics chart
                    updateInvoiceChart();
                },
                function () {
                    // PNotifyLoadFail();
                }
            );
        }

        function extractApprovals(resp) {
            var approvals = [];
            $.each(resp, function (index, item) {
                var p = item.approvalRequest != undefined ? item.approvalRequest.approvalRoot : item.approvalRoot;
                while (p != undefined) {
                    if (p.status.id == 1 && p.approverId == vm.currentUserId) {
                        approvals.push(item);
                        break;
                    }
                    p = p.nextApproval;
                }
            });
            return approvals;
        }

        vm.timeLabelClass = function (time) {
            return labelClassForTime(time);
        };

        function updatePaymentChart() {
            vm.isLoadingPaymentStatistics = true;
            StatisticsService.paymentStatistics(vm.paymentTimeRange).then(function (data) {
                vm.paymentStatistics = data.data;
                var paymentData =  [
                    {
                        label: '已收款项',
                        data: vm.paymentStatistics.paidAmount.toFixed(2),
                    },
                    {
                        label: '未收款项',
                        data: (vm.paymentStatistics.amount - vm.paymentStatistics.paidAmount).toFixed(2),
                    },
                ];
                updateChart('#chart-payment', paymentData);
                vm.isLoadingPaymentStatistics = false;
            }, function () {
                vm.isLoadingPaymentStatistics = false;
            })
        }

        function updateInvoiceChart() {
            vm.isLoadingInvoiceStatistics = true;
            StatisticsService.invoiceStatistics(vm.paymentTimeRange).then(function (data) {
                vm.invoiceStatistics = data.data;
                var invoiceData =  [
                    {
                        label: '已开发票',
                        data: vm.invoiceStatistics.invoiceAmount.toFixed(2),
                    },
                    {
                        label: '未开发票',
                        data: (vm.invoiceStatistics.totalAmount - vm.invoiceStatistics.invoiceAmount).toFixed(2),
                    },
                ];
                updateChart('#chart-invoice', invoiceData);
                vm.isLoadingInvoiceStatistics = false;
            }, function () {
                vm.isLoadingInvoiceStatistics = false;
            })

        }

        function updateChart(chartId, data) {
            $.plot(chartId, data, {
                series: {
                    pie: {
                        show: true,
                        radius: 1,
                        label: {
                            show: true,
                            radius: 3/4,
                            formatter: function (label, series) {
                                return "<div style='font-size:8pt; text-align:center; padding:2px; color:white;'>" + label + "<br/>" + series.data + "</div>";
                            },
                            background: {
                                opacity: 0.5
                            }
                        }
                    }
                },
                legend: {
                    show: false
                }
            });
        }

        vm.initInvoiceDateRangePicker = function () {
            $("#field_invoice_timerange").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.invoiceTimeRange.timeFrom = start.valueOf();
                    vm.invoiceTimeRange.timeTo = end.valueOf();
                    updateInvoiceChart();
                }
            );
        };

        vm.initPaymentDateRangePicker = function () {
            $("#field_payment_timerange").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.paymentTimeRange.timeFrom = start.valueOf();
                    vm.paymentTimeRange.timeTo = end.valueOf();
                    updatePaymentChart();
                }
            );
        };

        function initCalendar() {
            /* initialize the calendar */
            $('#wks-calendar').fullCalendar({
                locale: 'zh-cn',
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                buttonText: {
                    today: '今天',
                    month: '月',
                    week: '周',
                    day: '日'
                },
                eventRender: function(event, element) {
                    element.qtip({
                        content: event.tooltip,
                        position: {
                            my: 'bottom left',
                            at: 'top left'
                        }
                    });
                },
                events: vm.events,
                editable: false,
                droppable: false, // this allows things to be dropped onto the calendar !!!
            });
        }
    }
})();
