(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('WksVacationEditController', WksVacationEditController);

    WksVacationEditController.$inject = ['$timeout', '$scope', '$q', '$rootScope', '$state', 'WksVacationService', 'DictionaryService', 'EmployeeService'];

    function WksVacationEditController ($timeout, $scope, $q, $rootScope, $state, WksVacationService, DictionaryService, EmployeeService) {
        let vm = this;
        vm.vacation = {};
        vm.isLoading = false;
        vm.pageTitle = '新增请假申请';
        vm.employeesNotIncluded = [];
        vm.auditors = [];
        vm.isLoading = false;
        vm.newAuditors = [];

        vm.save = save;
        vm.cancel = cancel;
        vm.load = load;
        vm.load();


        function load() {
            vm.isLoading = true;
            let empQuery = EmployeeService.query().$promise;
            let typeQuery = DictionaryService.vacationTypeOptions();

            $.when(empQuery, typeQuery).then(
                (empResp, typeRsp) => {
                    vm.vacationTypeOptions = typeRsp;
                    vm.employees = empResp;
                    vm.isLoading = false;
                },
                () => {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                }
            );
        }


        function cancel() {
            $state.go("^");
        }

        function save () {
            if (vm.isSaving) {
                return;
            }

            let valid = $('.vacation-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (!notEmptyArray(vm.auditors)) {
                PNotifyErrorNeedApprover();
                return;
            }

            if (vm.vacation.endTime < vm.vacation.startTime) {
                PNotifyTimeRangeError();
                return;
            }

            vm.isSaving = true;

            let approvalRequest = {
                name: vm.vacation.type.name + ' ' + formatTimestampDMY(vm.vacation.startTime) + '-' + formatTimestampDMY(vm.vacation.endTime)
            };
            approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
            vm.vacation.approvalRequest = approvalRequest;
            vm.vacation.autoStart = true;

            WksVacationService.save(vm.vacation, onSaveSuccess, onSaveError);
            function onSaveSuccess (result) {
                vm.isSaving = false;
                PNotifySaveSuccess();
                $state.go("^");
            }

            function onSaveError () {
                vm.isSaving = false;
                PNotifySaveFail();
            }
        }

        vm.initJsComponents = function initJsComponents() {
            $('.vacation-edit-content form').parsley();

            $('#field_start_time').datetimepicker({
                format: dateFormatDMY(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.vacation.startTime)
            }).on('dp.change', function(ev){
                vm.vacation.startTime = moment(ev.date).valueOf();
            });

            $('#field_end_time').datetimepicker({
                format: dateFormatDMY(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.vacation.endTime)
            }).on('dp.change', function(ev){
                vm.vacation.endTime = moment(ev.date).valueOf();
            });
        };

        /****************************************** Auditor ****************************/

        const modalIdAddAduditor = '#modal-add-vacation-auditor';

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
