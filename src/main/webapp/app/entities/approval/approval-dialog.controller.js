(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ApprovalDialogController', ApprovalDialogController);

    ApprovalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Approval'];

    function ApprovalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Approval) {
        var vm = this;

        vm.approval = entity;
        vm.clear = clear;
        vm.save = save;
        vm.approvals = Approval.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.approval.id !== null) {
                Approval.update(vm.approval, onSaveSuccess, onSaveError);
            } else {
                Approval.save(vm.approval, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:approvalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
