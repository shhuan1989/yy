(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProjectRateDialogController', ProjectRateDialogController);

    ProjectRateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectRate', 'Employee', 'Project'];

    function ProjectRateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectRate, Employee, Project) {
        var vm = this;

        vm.projectRate = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectRate.id !== null) {
                ProjectRate.update(vm.projectRate, onSaveSuccess, onSaveError);
            } else {
                ProjectRate.save(vm.projectRate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:projectRateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
