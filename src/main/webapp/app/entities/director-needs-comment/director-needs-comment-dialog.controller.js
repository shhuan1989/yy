(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorNeedsCommentDialogController', DirectorNeedsCommentDialogController);

    DirectorNeedsCommentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DirectorNeedsComment', 'Employee'];

    function DirectorNeedsCommentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DirectorNeedsComment, Employee) {
        var vm = this;

        vm.directorNeedsComment = entity;
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
            if (vm.directorNeedsComment.id !== null) {
                DirectorNeedsComment.update(vm.directorNeedsComment, onSaveSuccess, onSaveError);
            } else {
                DirectorNeedsComment.save(vm.directorNeedsComment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:directorNeedsCommentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
