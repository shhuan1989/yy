(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProjectCostDialogController', ProjectCostDialogController);

    ProjectCostDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectCost', 'Employee', 'Project'];

    function ProjectCostDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectCost, Employee, Project) {
        var vm = this;

        vm.projectCost = entity;
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
            if (vm.projectCost.id !== null) {
                ProjectCost.update(vm.projectCost, onSaveSuccess, onSaveError);
            } else {
                ProjectCost.save(vm.projectCost, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:projectCostUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
