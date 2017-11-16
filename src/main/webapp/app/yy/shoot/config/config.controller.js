(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ShootConfigController', ShootConfigController);

    ShootConfigController.$inject = ['$scope', '$rootScope', '$state', 'ShootConfigService', 'ApprovalService'];

    function ShootConfigController ($scope, $rootScope, $state, ShootConfigService, ApprovalService) {
        var vm = this;

        vm.searchParams = {};
        vm.shootConfigsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.shootConfigsTableParams.count();
        vm.shootConfigs = [];
        vm.isLoading = false;

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.shootConfigsTableParams.count(vm.pageSize);
        });

        vm.deleteApplication = function deleteContract(shootConfig) {

            bootboxConfirm("确认删除申请 <span class='text-aqua'>" + shootConfig.projectName + "</span> ?", function () {
                ShootConfigService.delete({id: shootConfig.id},
                    function () {
                        vm.search();
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        };

        vm.submitApplication = function (shootConfig) {
            bootboxConfirm("确认提交申请 <span class='text-aqua'>" + shootConfig.projectName + "</span> ?", function () {
                ApprovalService.start(shootConfig.approvalRequest.id).then(
                    function () {
                        vm.search();
                        PNotifySaveSuccess();
                    },
                    function () {
                        PNotifySaveFail();
                    }
                );
            });
        };

        function search() {
            vm.isLoading = true;
            ShootConfigService.query($.extend(true,
                {},
                vm.searchParams
            ), (data) => {
                vm.shootConfigs = groupShootCost(data);
                vm.shootConfigsTableParams.settings({
                    dataset: vm.shootConfigs
                });
                vm.isLoading = false;
            }, () => {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

    }
})();
