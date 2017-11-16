(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DeductManagementController', DeductManagementController);

    DeductManagementController.$inject = ['$scope', '$rootScope', '$state', 'DeductManagementService', 'NgTableParams'];

    function DeductManagementController ($scope, $rootScope, $state, DeductManagementService, NgTableParams) {
        var vm = this;

        vm.searchParams = {};
        vm.employeesTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.employeesTableParams.count();
        vm.employees = [];
        vm.isLoading = false;

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.employeesTableParams.count(vm.pageSize);
        });

        vm.deleteDeduct = function deleteDeduct(employee) {

            bootboxConfirm("确认删除 <span class='text-aqua'>" + employee.name + "</span> 的提成信息?", function () {
                employee.employeeDeduction = null;
                employee.probationDeduction = null;
                DeductManagementService.update(employee,
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

        function search() {
            vm.isLoading = true;
            DeductManagementService.query($.extend(true,
                {
                    hasDeduct: 1
                },
                vm.searchParams
            ), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.employees = data;
                vm.employeesTableParams.settings({
                    dataset: vm.employees
                });
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }
    }
})();
