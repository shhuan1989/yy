(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('EmployeeManagementController', EmployeeManagementController);

    EmployeeManagementController.$inject = ['$scope', '$rootScope', '$state', 'AlertService', 'EmployeeService', 'NgTableParams', 'DictionaryService'];

    function EmployeeManagementController ($scope, $rootScope, $state, AlertService, EmployeeService, NgTableParams, DictionaryService) {
        var vm = this;

        vm.searchParams = {};

        DictionaryService.setOptions(vm);
        vm.empoyeesTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = $rootScope.employeeManagementPageSize != undefined ? $rootScope.employeeManagementPageSize : vm.empoyeesTableParams.count();
        vm.employees = [];
        vm.isLoading = false;

        vm.search = search;
        vm.deleteEmp = deleteEmp;

        search();

        $scope.$watch('vm.pageSize', function () {
            vm.empoyeesTableParams.count(vm.pageSize);
            $rootScope.employeeManagementPageSize = vm.pageSize;
        });

        function deleteEmp(employee) {
            bootboxConfirm("确认删除员工 <span class='text-aqua'>" + employee.name + "</span> ?", function () {
                EmployeeService.delete({id: employee.id},
                    function () {
                        vm.search();
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        }


        function search() {
            console.log("Search with params", vm.searchParams);
            vm.isLoading = true;
            EmployeeService.query($.extend(true,
                {},
                vm.searchParams
            ), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.employees = data;
                vm.empoyeesTableParams.settings({
                   dataset: vm.employees
                });
                vm.empoyeesTableParams.count(vm.pageSize);
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }

        vm.initSearchDateRangePicker = function () {
            $("#field_hire_date").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.searchParams.hireDateFrom = start.valueOf();
                    vm.searchParams.hireDateTo = end.valueOf();
                }
            );
        };

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });
        };
    }
})();
