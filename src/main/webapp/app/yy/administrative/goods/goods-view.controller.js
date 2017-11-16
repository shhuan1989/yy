(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('AdmGoodsViewController', AdmGoodsViewController);

    AdmGoodsViewController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'AdminGoodsService', 'EmployeeService'];

    function AdmGoodsViewController ($timeout, $scope, $stateParams, $state, AdminGoodsService, EmployeeService) {
        let vm = this;
        vm.borrowRecord = {};
        vm.auditors = [];
        vm.isLoading = false;

        vm.cancel = cancel;
        vm.load = load;
        vm.load();

        function load() {
            vm.isLoading = true;

            let borrowRecordQuery = AdminGoodsService.get({id: $stateParams.id}).$promise;
            let empsQuery = EmployeeService.query().$promise;

            $.when(borrowRecordQuery, empsQuery).then((borrowRecord, employees) => {
                vm.borrowRecord = borrowRecord;

                let employeesById = {};
                $.each(employees, (index, employee) => {
                    employeesById[employee.id] = employee;
                });

                if (vm.borrowRecord && vm.borrowRecord.approvalRequest) {
                    vm.auditors = approvalChainToApprovers(vm.borrowRecord.approvalRequest.approvalRoot, employeesById);
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
