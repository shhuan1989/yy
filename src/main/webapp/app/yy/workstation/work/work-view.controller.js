(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('WksWorkViewController', WksWorkViewController);

    WksWorkViewController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'WksWorkService', 'DictionaryService', 'EmployeeService'];

    function WksWorkViewController ($timeout, $scope, $state, $stateParams, WksWorkService, DictionaryService, EmployeeService) {
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

            let workQuery = WksWorkService.get({id: $stateParams.id}).$promise;
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
