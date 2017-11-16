(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProvinceDialogController', ProvinceDialogController);

    ProvinceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Province', 'City'];

    function ProvinceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Province, City) {
        var vm = this;

        vm.province = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cities = City.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.province.id !== null) {
                Province.update(vm.province, onSaveSuccess, onSaveError);
            } else {
                Province.save(vm.province, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:provinceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
