(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ShootCostManagementController', ShootCostManagementController);

    ShootCostManagementController.$inject = ['$scope', '$rootScope', '$state', 'ShootCostService', 'ApprovalService'];

    function ShootCostManagementController ($scope, $rootScope, $state, ShootCostService, ApprovalService) {
        var vm = this;

        vm.searchParams = {};
        vm.shootCostsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.shootCostsTableParams.count();
        vm.shootCosts = [];
        vm.isLoading = false;

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.shootCostsTableParams.count(vm.pageSize);
        });

        vm.deleteApplication = function deleteContract(shootCost) {

            bootboxConfirm("确认删除申请 <span class='text-aqua'>" + shootCost.projectName + "</span> ?", function () {
                ShootCostService.delete({id: shootCost.id},
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

        vm.submitApplication = function (shootCost) {
            bootboxConfirm("确认提交申请 <span class='text-aqua'>" + shootCost.projectName + "</span> ?", function () {
                ApprovalService.start(shootCost.approvalRequest.id).then(
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
            ShootCostService.query($.extend(true,
                {},
                vm.searchParams
            ), (data) => {
                vm.shootCosts = groupShootCost(data);
                
                vm.shootCostsTableParams.settings({
                    dataset: vm.shootCosts
                });
                vm.isLoading = false;
            }, () => {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }
    }
})();
