(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('FileInfoDialogController', FileInfoDialogController);

    FileInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FileInfo'];

    function FileInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FileInfo) {
        var vm = this;

        vm.fileInfo = entity;
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
            if (vm.fileInfo.id !== null) {
                FileInfo.update(vm.fileInfo, onSaveSuccess, onSaveError);
            } else {
                FileInfo.save(vm.fileInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:fileInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
