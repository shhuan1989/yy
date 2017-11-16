(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('TaskDialogController', TaskDialogController);

    TaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'Employee', 'FileInfo', 'PictureInfo', 'Dictionary', 'Comment', 'Project'];

    function TaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Task, Employee, FileInfo, PictureInfo, Dictionary, Comment, Project) {
        var vm = this;

        vm.task = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.fileinfos = FileInfo.query();
        vm.pictureinfos = PictureInfo.query();
        vm.dictionaries = Dictionary.query();
        vm.comments = Comment.query();
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.task.id !== null) {
                Task.update(vm.task, onSaveSuccess, onSaveError);
            } else {
                Task.save(vm.task, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:taskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
