(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('WksWorkEditController', WksWorkEditController);

    WksWorkEditController.$inject = ['$timeout', '$scope', '$q', '$rootScope', '$state', 'WksWorkService', 'DictionaryService', 'EmployeeService'];

    function WksWorkEditController ($timeout, $scope, $q, $rootScope, $state, WksWorkService, DictionaryService, EmployeeService) {
        let vm = this;
        vm.isLoading = false;
        vm.pageTitle = '新增加班申请';
        vm.employeesNotIncluded = [];
        vm.auditors = [];
        vm.isLoading = false;
        vm.newAuditors = [];
        vm.work = {};

        vm.save = save;
        vm.cancel = cancel;
        vm.load = load;
        vm.load();

        function load() {
            vm.isLoading = true;
            let empQuery = EmployeeService.query().$promise;
            $.when(empQuery).then(
                (empResp) => {
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

            let valid = $('.work-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (!notEmptyArray(vm.auditors)) {
                PNotifyErrorNeedApprover();
                return;
            }

            if (vm.work.endTime < vm.work.startTime) {
                PNotifyTimeRangeError();
                return;
            }

            vm.isSaving = true;

            let approvalRequest = {
                name: '加班 ' + formatTimestampDmyHms(vm.work.startTime) + '-' + formatTimestampDmyHms(vm.work.endTime)
            };
            approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
            vm.work.approvalRequest = approvalRequest;
            vm.work.autoStart = true;

            WksWorkService.save(vm.work, onSaveSuccess, onSaveError);
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

        $scope.$watchGroup(['vm.work.startTime', 'vm.work.endTime'], () => {
            if (vm.work.startTime != undefined && vm.work.endTime != undefined) {
                vm.work.hours = Math.floor(moment.duration(moment(vm.work.endTime).diff(moment(vm.work.startTime))).asHours());
            }
        });

        vm.initJsComponents = function initJsComponents() {
            $('.work-edit-content form').parsley();

            $('#field_start_time').datetimepicker({
                sideBySide: true,
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.work.startTime)
            }).on('dp.change', function(ev){
                vm.work.startTime = moment(ev.date).valueOf();
            });

            $('#field_end_time').datetimepicker({
                sideBySide: true,
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.work.endTime)
            }).on('dp.change', function(ev){
                vm.work.endTime = moment(ev.date).valueOf();
            });

            if (isMobileBrowser()) {
                $('#babys_due_date_id').find('input').prop('readonly', true);
            }
        };

        /****************************************** Auditor ****************************/

        const modalIdAddAduditor = '#modal-add-work-auditor';

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
