(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DirectorDemandController', DirectorDemandController);

    DirectorDemandController.$inject = ['$scope', '$rootScope', '$state', 'DirectorDemandService', 'EmployeeService'];

    function DirectorDemandController ($scope, $rootScope, $state, DirectorDemandService, EmployeeService) {
        var vm = this;

        vm.searchParams = {};
        vm.needsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.needsTableParams.count();
        vm.shootConfigs = [];
        vm.isLoading = false;
        vm.employees = EmployeeService.query();

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.needsTableParams.count(vm.pageSize);
        });

        function search() {
            vm.isLoading = true;
            DirectorDemandService.query($.extend(true,
                {},
                vm.searchParams
            ), (data) => {
                vm.shootConfigs = data;
                vm.needsTableParams.settings({
                    dataset: vm.shootConfigs
                });
                vm.isLoading = false;
            }, () => {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });
        };

    }
})();
