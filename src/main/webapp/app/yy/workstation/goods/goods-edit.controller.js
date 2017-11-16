(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('WksGoodsEditController', WksGoodsEditController);

    WksGoodsEditController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'WksGoodsService', 'EmployeeService', 'WksAssetService'];

    function WksGoodsEditController ($timeout, $scope, $stateParams, $state, WksGoodsService, EmployeeService, WksAssetService) {
        let vm = this;
        vm.utilApplication = {startTime: moment().valueOf()};
        vm.auditors = [];
        vm.isLoading = false;
        vm.employees = [];
        vm.employeesNotIncluded = [];
        vm.assets = [];
        vm.selectedAsset = null;
        const MAX_INT = 10000000;
        vm.restAmountOfSelectedAsset = MAX_INT;
        vm.amoutPlaceHolder = '';
        vm.isSaving = false;

        vm.cancel = cancel;
        vm.save = save;
        vm.load = load;
        vm.load();

        const FormSelector = '.util-apply-edit-content form';

        function load() {
            vm.isLoading = true;

            let empQuery = EmployeeService.query().$promise;
            let assetsQuery = WksAssetService.query().$promise;

            $.when(empQuery, assetsQuery).then(function (employees, assets) {
                vm.employees = employees;
                // vm.assets = assets.filter((item) => item.amount != undefined && item.amount  > 0);
                vm.assets = assets;
                $(FormSelector).parsley();
                vm.isLoading = false;
            }, function () {
                vm.isLoading = false;
                PNotifyLoadFail();
            });
        }


        function cancel() {
            $state.go("^");
        }

        function save () {
            if (vm.isSaving) {
                return;
            }

            let valid = $(FormSelector).parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (!notEmptyArray(vm.auditors)) {
                PNotifyErrorNeedApprover();
                return;
            }

            vm.isSaving = true;

            if (vm.selectedAsset == undefined) {
                PNotifySaveFail();
                return;
            }

            let approvalRequest = {};
            approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
            vm.utilApplication.approvalRequest = approvalRequest;
            vm.utilApplication.autoStart = true;
            if (!vm.selectedAsset.needReturn) {
                vm.utilApplication.endTime = moment('9999-01-01', 'YYYY-MM-DD').valueOf();
            }

            WksGoodsService.save(vm.utilApplication, onSaveSuccess, onSaveError);
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

        $scope.$watch('vm.utilApplication.asset.id', function () {
            if (vm.assets != undefined) {
                let asset = vm.assets.find((item) => item.id == vm.utilApplication.asset.id);
                vm.selectedAsset = asset;
                if (asset != undefined) {
                    vm.restAmountOfSelectedAsset = asset.amount;
                    vm.amoutPlaceHolder = '库存： ' + asset.amount;
                } else {
                    vm.restAmountOfSelectedAsset = MAX_INT;
                    vm.amoutPlaceHolder = '';
                }
            } else {
                vm.selectedAsset = null;
            }
        });

        vm.initJsComponents = function initJsComponents() {
            $('.select2').select2({
                language: "zh-CN"
            });

            $('#field_start_time').datetimepicker({
                format: dateFormatDMY(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.utilApplication.startTime)
            }).on('dp.change', function(ev){
                vm.utilApplication.startTime = moment(ev.date).valueOf();
            });

            $('#field_end_time').datetimepicker({
                format: dateFormatDMY(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.utilApplication.endTime)
            }).on('dp.change', function(ev){
                vm.utilApplication.endTime = moment(ev.date).valueOf();
            });
        };

        /****************************************** Auditor ****************************/

        const modalIdAddAduditor = '#modal-add-utilapply-auditor';

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
