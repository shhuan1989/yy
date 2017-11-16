(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('AdminWorkViewController', AdminWorkViewController);

    AdminWorkViewController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'AdminWorkService', 'DictionaryService', 'EmployeeService'];

    function AdminWorkViewController ($timeout, $scope, $state, $stateParams, AdminWorkService, DictionaryService, EmployeeService) {
        let vm = this;
        vm.isLoading = false;
        vm.auditors = [];
        vm.isLoading = false;
        vm.work = {};

        vm.cancel = cancel;
        vm.load = load;
        vm.load();

        function load() {
            vm.isLoading = true;

            let workQuery = AdminWorkService.get({id: $stateParams.id}).$promise;
            let empsQuery = EmployeeService.query().$promise;

            $.when(workQuery, empsQuery).then((work, employees) => {
                vm.work = work;

                let employeesById = {};
                $.each(employees, (index, employee) => {
                    employeesById[employee.id] = employee;
                });

                if (vm.work && vm.work.approvalRequest) {
                    vm.auditors = approvalChainToApprovers(vm.work.approvalRequest.approvalRoot, employeesById);
                } else {
                    vm.auditors = [];
                }
                vm.isLoading = false;
            }, () => {
                PNotifyLoadFail();
                vm.isLoading = false;
            });
        }


        function cancel() {
            $state.go("^");
        }
    }
})();
