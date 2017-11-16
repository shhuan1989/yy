(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('AdminAssetController', AdminAssetController);

    AdminAssetController.$inject = ['$scope', '$state', '$rootScope', 'AdminAssetService', 'EmployeeService'];

    function AdminAssetController ($scope, $state, $rootScope, AdminAssetService, EmployeeService) {
        var vm = this;

        vm.searchParams = {};
        vm.assetsTableParams = $rootScope.defaultNgTableParams();
        vm.assets = [];
        vm.editingAsset = {};
        vm.isLoading = false;
        vm.pageSize = vm.assetsTableParams.count();
        vm.yesNoOptions = [{
            name: '是',
            value: true,
        }, {
            name: '否',
            value: false,
        }];
        vm.employees = [];
        EmployeeService.query(function (employees) {
           vm.employees = [{id: 0, uniqueName: '公用资产'}, ...employees];
        }, function () {

        });

        vm.search = search;
        vm.editAsset = editAsset;
        vm.saveEditingAsset = saveEditingAsset;
        vm.deleteAsset = deleteAsset;

        search();

        $scope.$watch('vm.pageSize', function () {
            vm.assetsTableParams.count(vm.pageSize);
        });

        function search() {
            vm.isLoading = true;

            if (vm.searchParams.ownerId == 0) {
                vm.searchParams.ownerId = null;
                vm.searchParams.onlyPublicAsset = true;
            } else {
                vm.searchParams.onlyPublicAsset = false;
            }
            
            AdminAssetService.query(vm.searchParams, (data) => {
                vm.assets = data;
                vm.isLoading = false;
            }, () => {
                PNotifySearchFail();
                vm.isLoading = false;
            })
        }

        $scope.$watch('vm.assets', function () {
            vm.assetsTableParams.settings({
                dataset: $.extend(true, [], vm.assets) // extend to update view
            });
        }, true);

        const EditAssetModalId = '#modal-edit-asset';
        function editAsset(asset) {
            vm.editingAsset = $.extend(true, {needReturn: true}, asset);
            $(EditAssetModalId).modal('show');
        }

        function saveEditingAsset() {
            if (vm.editingAsset.id == undefined) {
                AdminAssetService.save(vm.editingAsset, onSaveSuccess, onSaveFail)
            } else {
                AdminAssetService.update(vm.editingAsset, onUpdateSuccess, onSaveFail)
            }

            function onSaveSuccess(asset) {
                vm.assets.push(asset);
                PNotifySaveSuccess();
                $(EditAssetModalId).modal('hide');
            }

            function onUpdateSuccess(asset) {
                const index = vm.assets.findIndex((item) => item.id === asset.id);
                if (index >= 0) {
                    vm.assets.splice(index, 1, asset);
                } else {
                    vm.assets.push(asset);
                }
                PNotifySaveSuccess();
                $(EditAssetModalId).modal('hide');
            }

            function onSaveFail() {
                PNotifySaveFail()
            }
        }

        function deleteAsset(asset) {
            bootboxConfirm("确认删除 <span class='text-aqua'>" + asset.name + "</span> ?", function () {
                AdminAssetService.delete({id: asset.id},
                    function () {
                        vm.assets = vm.assets.filter((item) => item.id != asset.id);
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        }

        vm.initJsComponents = function() {
            $('.select2').select2({
                language: "zh-CN"
            });
        }
    }
})();
