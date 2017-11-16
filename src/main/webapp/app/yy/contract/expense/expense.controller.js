(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ExpenseController', ExpenseController);

    ExpenseController.$inject = ['$scope', '$rootScope', '$state', 'ShootCostAuditService', 'ExpenseService', 'DictionaryService'];

    function ExpenseController ($scope, $rootScope, $state, ShootCostAuditService, ExpenseService, DictionaryService) {
        var vm = this;

        vm.searchParams = {};
        vm.expensesTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.expensesTableParams.count();
        vm.expenses = [];
        vm.totalExpense = 0.0;
        vm.isLoading = false;

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.expensesTableParams.count(vm.pageSize);
        });

        DictionaryService.payMethodOptions().then(function (options) {
           vm.payMethodOptions = options;
        });
        DictionaryService.approvalStatusOptions().then(function (options) {
           vm.paymentStatusOptions = options;
        });
        function search() {
            vm.isLoading = true;

            var shootCostQueryParams = $.extend(true, {}, vm.searchParams);
            shootCostQueryParams.approvalStatusId = shootCostQueryParams.paymentStatusId;

            var shootCostQuery = ShootCostAuditService.query(shootCostQueryParams).$promise;
            var otherCostQuery = ExpenseService.query($.extend(true, {}, vm.searchParams)).$promise;

            $.when(shootCostQuery, otherCostQuery).then(function (shootCosts, otherCosts) {
                if (vm.searchParams.payMethodId != undefined) {
                    shootCosts = [];
                }

                var configs = {};
                $.each(shootCosts, function (index, config) {
                    var projectId = config.projectId;
                    if (configs[projectId] == undefined) {
                        configs[projectId] = [];
                    }
                    configs[projectId].push(config);
                });

                var totalExpense = 0;
                var expenses = [];
                $.each(configs, function (projectId, tconfigs) {
                    tconfigs.sort(function (c1, c2) {
                        return c1.createTime - c2.createTime;
                    });
                    var cost = sum(tconfigs.map((c) => c.actualCost || 0));
                    if (vm.expenseCategory != 2) {
                        totalExpense += cost;
                    }
                    expenses.push({
                        id: tconfigs[0].projectId,
                        type: 0,
                        configs: tconfigs,
                        projectIdNumber: tconfigs[0].projectIdNumber,
                        projectId: tconfigs[0].projectId,
                        projectName: tconfigs[0].projectName,
                        owner: tconfigs[0].owner,
                        createTime: tconfigs[tconfigs.length-1].createTime,
                        approvalRequest: tconfigs[tconfigs.length-1].approvalRequest,
                        budget: sum(tconfigs.map((c) => c.budget || 0)),
                        cost: cost,
                        payTime: tconfigs[tconfigs.length-1].lastModifiedTime,
                        category: '拍摄结算',
                        purpose: '拍摄'
                    });
                });
                // expenses.sort(function (c1, c2) {
                //     if (c1.approvalRequest != undefined && c2.approvalRequest != undefined
                //         && c1.approvalRequest.status != undefined && c2.approvalRequest.status != undefined
                //         && c1.approvalRequest.status.id != c2.approvalRequest.status.id) {
                //         return c1.approvalRequest.status.id - c2.approvalRequest.status.id;
                //     } else {
                //         c1.createTime - c2.createTime;
                //     }
                //     return 0;
                // });

                expenses = expenses.filter((e) => {
                    var valid = true;
                    if (shootCostQueryParams.payTimeFrom != undefined) {
                        valid = valid && e.lastModifiedTime != undefined && e.lastModifiedTime >= shootCostQueryParams.payTimeFrom;
                    }
                    if (shootCostQueryParams.payTimeTo != undefined) {
                        valid = valid && e.lastModifiedTime != undefined && e.lastModifiedTime <= shootCostQueryParams.payTimeTo;
                    }

                    //payMethodId
                    return valid;
                });

                $.each(otherCosts, function (index, cost) {
                    cost.category = '其他支出';
                    cost.cost = cost.total;
                    cost.type = 1;
                    if (vm.expenseCategory != 1) {
                        totalExpense += cost.total;
                    }
                    if (cost.items != undefined) {
                        cost.purpose = cost.items.map((item) => item.purpose).join(', ');
                    }
                });

                var allExpenses = [];
                if (vm.expenseCategory != 2) {
                    allExpenses = expenses;
                }
                if (vm.expenseCategory != 1) {
                    allExpenses = [...allExpenses, ...otherCosts];
                }

                allExpenses.sort((c1, c2) => {
                    if (c1.payTime == undefined) {
                        return 1;
                    }
                    if (c2.payTime == undefined) {
                        return -1;
                    }
                    return c2.payTime - c1.payTime;
                });

                vm.expenses = allExpenses;
                vm.totalExpense = totalExpense;

                vm.expensesTableParams.settings({
                    dataset: vm.expenses
                });
                vm.isLoading = false;

            });


            ShootCostAuditService.query($.extend(true,
                {},
                vm.searchParams
            ), (shootCosts) => {


            }, () => {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

        vm.initSearchDateRangePicker = function () {
            $("#field_pay_time").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.searchParams.payTimeFrom = start.valueOf();
                    vm.searchParams.payTimeTo = end.valueOf();
                }
            );
        };
    }
})();
