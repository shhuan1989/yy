(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('EquipmentDialogController', EquipmentDialogController);

    EquipmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Equipment', 'Dictionary'];

    function EquipmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Equipment, Dictionary) {
        var vm = this;

        vm.equipment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dictionaries = Dictionary.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.equipment.id !== null) {
                Equipment.update(vm.equipment, onSaveSuccess, onSaveError);
            } else {
                Equipment.save(vm.equipment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:equipmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
