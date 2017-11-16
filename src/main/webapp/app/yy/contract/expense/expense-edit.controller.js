(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ExpenseEditController', ExpenseEditController);

    ExpenseEditController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'ExpenseService', 'ProjectManagementService', 'EmployeeService', 'DictionaryService'];

    function ExpenseEditController ($scope, $rootScope, $state, $stateParams, ExpenseService, ProjectManagementService, EmployeeService, DictionaryService) {
        var vm = this;

        vm.pageTitle = $state.current.data.pageTitle;
        vm.expenseId = $stateParams.id;
        vm.projects = ProjectManagementService.query();
        vm.isLoading = true;
        vm.newAuditors = [];
        vm.auditors = [];
        vm.employeesNotIncluded = [];
        vm.employees = EmployeeService.query({deleted: false});
        vm.isSaving = false;
        vm.yesnoOptions = [];
        vm.payMethodOptions = [];
        vm.expense = {
            items: []
        };
        vm.newExpense = {};
        vm.disableEdit = $state.$current.toString() === 'yy_contract_expense.viewx';


        DictionaryService.yesnoOptions().then(function (options) {
            vm.yesnoOptions = options;
        });

        DictionaryService.payMethodOptions().then(function (options) {
            vm.payMethodOptions = options;
        });

        vm.load = load;
        vm.cancel = cancel;
        vm.save = save;
        vm.initJsComponents = initJsComponents;

        load();

        function load() {
            if (vm.expenseId != undefined) {
                var expenseQuery = ExpenseService.get({id: vm.expenseId}).$promise;
                var empsQuery = EmployeeService.query().$promise;

                $.when(expenseQuery, empsQuery).then(function (expense, employees) {
                    vm.isLoading = false;
                    vm.expense = expense;

                    var employeesById = {};
                    $.each(employees, function (index, employee) {
                        employeesById[employee.id] = employee;
                    });

                    if (expense && expense.approvalRequest) {
                        vm.auditors = approvalChainToApprovers(expense.approvalRequest.approvalRoot, employeesById);
                    } else {
                        vm.auditors = [];
                    }
                },function () {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                })
            } else {
                vm.isLoading = false;
            }
        }

        function save() {
            if (vm.isSaving) {
                return;
            }

            var validate = $('.expense-edit-content form').parsley().validate();
            if (!validate) {
                PNotifyInvalidInput();
                return;
            }

            if (!notEmptyArray(vm.auditors)) {
                PNotifyErrorNeedApprover();
                return;
            }

            if (!notEmptyArray(vm.expense.items)) {
                PNotifyError('支出项目不能未空');
                return;
            }

            vm.isSaving = true;

            var total = sum(vm.expense.items.map((i) => i.amount || 0));
            var name = '其他支出, ' + total;
            if (vm.expense.projectId != undefined && vm.projects != undefined) {
                var project = vm.projects.find((item) => item.id == vm.expense.projectId);
                name = project.name + ' 的支出, ' + total;
            }

            let approvalRequest = {
                name: name
            };
            approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
            vm.expense.approvalRequest = approvalRequest;
            vm.expense.autoStart = true;

            if (vm.expense.id != undefined) {
                ExpenseService.update(vm.expense, onSaveSuccess, onSaveError);
            } else {
                ExpenseService.save(vm.expense, onSaveSuccess, onSaveError);
            }

            function onSaveSuccess (result) {
                vm.isSaving = false;
                PNotifySaveSuccess();
                $state.go("^");
            }

            function onSaveError (resp) {
                vm.isSaving = false;
                PNotifySaveFail();
            }
        }

        function cancel() {
            $state.go('^');
        }

        vm.addNewExpense = function () {
            vm.expense.items.push(vm.newExpense);
            vm.newExpense = {};
        };

        vm.removeExpense = function (index) {
            if (index >= 0 && index < vm.expense.items.length) {
                vm.expense.items.splice(index, 1);
            }
        };

        function initJsComponents() {
            $('#field_pay_time').datetimepicker({
                format: dateFormatDmyHms(),
                locale: 'zh-CN',
                sideBySide: true,
                defaultDate: datetimePickerDefaultTime(vm.expense.payTime)
            }).on('dp.change', function(ev){
                vm.expense.payTime = moment(ev.date).valueOf();
            });

            $('.expense-edit-content form').parsley();

            $('.select2').select2({
                language: "zh-CN"
            });
        }

        $scope.$watch('vm.expense', function () {
            if (vm.expense.items != undefined) {
                vm.expense.total = sum(vm.expense.items.map((e) => e.amount || 0));
            } else {
                vm.expense.total = '';
            }
        }, true);

        /****************************************** Auditor ****************************/

        const modalIdAddAduditor = '#modal-add-expense-auditor';

        $(modalIdAddAduditor).on('shown.bs.modal', function (e) {
            vm.employeesNotIncluded = $.grep(vm.employees, function (item) {
                let exists = vm.auditors.find(function (exitem) {
                    return exitem.id == item.id;
                });
                return !exists;
            });

            $('.select2').select2({
                language: "zh-CN"
            });
        });

        vm.addNewAuditor = function () {
            $(modalIdAddAduditor).modal('show');
        };

        vm.saveNewAuditor = function () {
            if (vm.auditors == undefined) {
                vm.auditors = [];
            }
            if (vm.newAuditors != undefined) {
                vm.auditors.push(vm.newAuditors);
                vm.newAuditors = null;
            }
            $(modalIdAddAduditor).modal('hide');
        };

        vm.deleteAuditor = function (auditor) {
            vm.auditors = vm.auditors.filter(function (item) {
                return item.id != auditor.id;
            });
        };
    }
})();
