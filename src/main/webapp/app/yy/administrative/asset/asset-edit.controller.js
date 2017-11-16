(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('AdminAssetEditController', AdminAssetEditController);

    AdminAssetEditController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'AdminAssetService', 'EmployeeService'];

    function AdminAssetEditController ($timeout, $scope, $stateParams, $state, AdminAssetService, EmployeeService) {
        let vm = this;

        vm.employees = [];
        vm.isLoading = false;
        vm.status = "view";
        vm.assetId = $stateParams.id;
        vm.asset = {};
        vm.pageTitle = $state.current.data.pageTitle;
        vm.disableEdit = $state.$current.toString() === 'yy_admin_asset.view';
        vm.yesNoOptions = [{
            name: '是',
            value: true,
        }, {
            name: '否',
            value: false,
        }];

        vm.cancel = cancel;
        vm.save = save;
        vm.load = load;

        load();

        function load() {
            vm.isLoading = true;

            if (vm.assetId != undefined) {
                let assetQuery = AdminAssetService.get({id: $stateParams.id}).$promise;
                let empsQuery = EmployeeService.query().$promise;

                $.when(assetQuery, empsQuery).then((asset, employees) => {
                    vm.asset = asset;
                    vm.employees = employees;
                    vm.isLoading = false;
                }, () => {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                });
            } else {
                EmployeeService.query({}, function (employees) {
                    vm.employees = employees;
                    vm.isLoading = false;
                }, function () {
                    vm.isLoading = false;
                    PNotifyLoadFail();
                })
            }
        }

        function cancel() {
            $state.go("^");
        }

        function save() {
            var validate = $('.asset-edit-content form').parsley().validate();
            if (!validate) {
                PNotifyInvalidInput();
                return;
            }

            if (vm.asset.id != undefined) {
                AdminAssetService.update(vm.asset, onSaveSuccess, onSaveError)
            } else {
                AdminAssetService.save(vm.asset, onSaveSuccess, onSaveError)
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        function initJsComponents() {
            $('.asset-edit-content form').parsley();

            $('.select2').select2({
                language: "zh-CN"
            });
        }
    }
})();
