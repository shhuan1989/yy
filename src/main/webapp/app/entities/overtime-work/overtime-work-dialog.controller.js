(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('OvertimeWorkDialogController', OvertimeWorkDialogController);

    OvertimeWorkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OvertimeWork', 'Employee', 'ApprovalRequest'];

    function OvertimeWorkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, OvertimeWork, Employee, ApprovalRequest) {
        var vm = this;

        vm.overtimeWork = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.approvalrequests = ApprovalRequest.query({filter: 'overtimework-is-null'});
        $q.all([vm.overtimeWork.$promise, vm.approvalrequests.$promise]).then(function() {
            if (!vm.overtimeWork.approvalRequestId) {
                return $q.reject();
            }
            return ApprovalRequest.get({id : vm.overtimeWork.approvalRequestId}).$promise;
        }).then(function(approvalRequest) {
            vm.approvalrequests.push(approvalRequest);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.overtimeWork.id !== null) {
                OvertimeWork.update(vm.overtimeWork, onSaveSuccess, onSaveError);
            } else {
                OvertimeWork.save(vm.overtimeWork, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:overtimeWorkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
