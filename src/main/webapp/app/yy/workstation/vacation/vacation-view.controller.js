(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('WksVacationViewController', WksVacationViewController);

    WksVacationViewController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'WksVacationService', 'EmployeeService'];

    function WksVacationViewController ($timeout, $scope, $stateParams, $state, WksVacationService, EmployeeService) {
        let vm = this;
        vm.vacation = {};
        vm.auditors = [];
        vm.isLoading = false;

        vm.cancel = cancel;
        vm.load = load;
        vm.load();

        function load() {
            vm.isLoading = true;

            let vacationQuery = WksVacationService.get({id: $stateParams.id}).$promise;
            let empsQuery = EmployeeService.query().$promise;

            $.when(vacationQuery, empsQuery).then((vacation, employees) => {
                vm.vacation = vacation;

                let employeesById = {};
                $.each(employees, (index, employee) => {
                   employeesById[employee.id] = employee;
                });

                if (vm.vacation && vm.vacation.approvalRequest) {
                    vm.auditors = approvalChainToApprovers(vm.vacation.approvalRequest.approvalRoot, employeesById);
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
