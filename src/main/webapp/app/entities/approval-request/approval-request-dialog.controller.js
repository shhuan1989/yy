(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ApprovalRequestDialogController', ApprovalRequestDialogController);

    ApprovalRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApprovalRequest', 'Employee'];

    function ApprovalRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ApprovalRequest, Employee) {
        var vm = this;

        vm.approvalRequest = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.approvalRequest.id !== null) {
                ApprovalRequest.update(vm.approvalRequest, onSaveSuccess, onSaveError);
            } else {
                ApprovalRequest.save(vm.approvalRequest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:approvalRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
