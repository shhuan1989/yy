(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('AssetBorrowRecordDialogController', AssetBorrowRecordDialogController);

    AssetBorrowRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AssetBorrowRecord', 'Employee', 'Asset'];

    function AssetBorrowRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AssetBorrowRecord, Employee, Asset) {
        var vm = this;

        vm.assetBorrowRecord = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.assets = Asset.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.assetBorrowRecord.id !== null) {
                AssetBorrowRecord.update(vm.assetBorrowRecord, onSaveSuccess, onSaveError);
            } else {
                AssetBorrowRecord.save(vm.assetBorrowRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:assetBorrowRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
