(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectPaymentDialogController', ProjectPaymentDialogController);

    ProjectPaymentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectPayment', 'Project'];

    function ProjectPaymentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectPayment, Project) {
        var vm = this;

        vm.projectPayment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectPayment.id !== null) {
                ProjectPayment.update(vm.projectPayment, onSaveSuccess, onSaveError);
            } else {
                ProjectPayment.save(vm.projectPayment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:projectPaymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
