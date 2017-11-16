(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ShootCostAuditController', ShootCostAuditController);

    ShootCostAuditController.$inject = ['$scope', '$rootScope', '$state', 'ShootCostAuditService', 'ApprovalService'];

    function ShootCostAuditController ($scope, $rootScope, $state, ShootCostAuditService, ApprovalService) {
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

        function search() {
            vm.isLoading = true;
            ShootCostAuditService.query($.extend(true,
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
