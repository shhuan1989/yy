(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('OtherIncomeEditController', OtherIncomeEditController);

    OtherIncomeEditController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'OtherIncomeService', 'EmployeeService', 'DictionaryService'];

    function OtherIncomeEditController ($scope, $rootScope, $state, $stateParams, OtherIncomeService, EmployeeService, DictionaryService) {
        var vm = this;

        vm.incomeId = $stateParams.id;
        vm.isLoading = true;
        vm.newAuditors = [];
        vm.auditors = [];
        vm.employeesNotIncluded = [];
        vm.employees = EmployeeService.query({deleted: false});
        vm.isSaving = false;
        vm.payMethodOptions = [];
        vm.income = {};
        vm.newIncome = {};

        DictionaryService.payMethodOptions().then(function (options) {
            vm.payMethodOptions = options;
        });

        vm.load = load;
        vm.cancel = cancel;
        vm.save = save;
        vm.initJsComponents = initJsComponents;

        load();

        function load() {
            if (vm.incomeId != undefined) {
                vm.income = OtherIncomeService.get({id: vm.incomeId}, function(){
                    vm.isLoading = false;
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

            var validate = $('.other-income-edit-content form').parsley().validate();
            if (!validate) {
                PNotifyInvalidInput();
                return;
            }

            if (!notEmptyArray(vm.auditors)) {
                PNotifyErrorNeedApprover();
                return;
            }

            vm.isSaving = true;

            var name = '收入, ' + vm.income.amount;
            let approvalRequest = {
                name: name
            };
            approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
            vm.income.approvalRequest = approvalRequest;
            vm.income.autoStart = true;

            if (vm.income.id != undefined) {
                OtherIncomeService.update(vm.income, onSaveSuccess, onSaveError);
            } else {
                OtherIncomeService.save(vm.income, onSaveSuccess, onSaveError);
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

        function initJsComponents() {
            $('#field_pay_time').datetimepicker({
                format: dateFormatDmyHms(),
                locale: 'zh-CN',
                sideBySide: true,
                defaultDate: datetimePickerDefaultTime(vm.income.incomeTime)
            }).on('dp.change', function(ev){
                vm.income.incomeTime = moment(ev.date).valueOf();
            });

            $('.other-income-edit-content form').parsley();

            $('.select2').select2({
                language: "zh-CN"
            });
        }

        /****************************************** Auditor ****************************/

        const modalIdAddAduditor = '#modal-add-income-auditor';

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
