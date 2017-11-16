(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('JobTitleDialogController', JobTitleDialogController);

    JobTitleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobTitle'];

    function JobTitleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobTitle) {
        var vm = this;

        vm.jobTitle = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.jobTitle.id !== null) {
                JobTitle.update(vm.jobTitle, onSaveSuccess, onSaveError);
            } else {
                JobTitle.save(vm.jobTitle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:jobTitleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
