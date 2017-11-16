(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ContractDialogController', ContractDialogController);

    ContractDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contract', 'FileInfo'];

    function ContractDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Contract, FileInfo) {
        var vm = this;

        vm.contract = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fileinfos = FileInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contract.id !== null) {
                Contract.update(vm.contract, onSaveSuccess, onSaveError);
            } else {
                Contract.save(vm.contract, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:contractUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
