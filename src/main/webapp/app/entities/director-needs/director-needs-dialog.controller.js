(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DirectorNeedsDialogController', DirectorNeedsDialogController);

    DirectorNeedsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DirectorNeeds', 'Project'];

    function DirectorNeedsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DirectorNeeds, Project) {
        var vm = this;

        vm.directorNeeds = entity;
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
            if (vm.directorNeeds.id !== null) {
                DirectorNeeds.update(vm.directorNeeds, onSaveSuccess, onSaveError);
            } else {
                DirectorNeeds.save(vm.directorNeeds, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:directorNeedsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
