(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('EquipmentEditController', EquipmentEditController);

    EquipmentEditController.$inject = ['$timeout', '$scope', '$rootScope', '$state', '$stateParams', '$q', 'EquipmentManagementService', 'disableEdit', 'DictionaryService'];

    function EquipmentEditController ($timeout, $scope, $rootScope, $state, $stateParams, $q, EquipmentManagementService, disableEdit, DictionaryService) {
        var vm = this;

        vm.disableEdit = !(!disableEdit);
        vm.pageTitle = $state.current.data.pageTitle;
        vm.equipment = {};
        DictionaryService.setOptions(vm);
        vm.isLoading = false;

        vm.save = save;
        vm.cancel = cancel;
        vm.load = load;
        vm.load();

        function load() {
            if ($stateParams.id != undefined) {
                vm.isLoading = true;
                EquipmentManagementService.get(
                    {id: $stateParams.id},
                    function (entity) {
                        vm.equipment = entity;
                        vm.isLoading = false;
                    },
                    function () {
                        PNotifyLoadFail();
                        vm.isLoading = false;
                    }
                );
            }
        }

        function cancel() {
            $state.go("^");
        }

        function save () {
            var valid = $('.equipment-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            console.log("validate befor submit", valid)
            vm.isSaving = true;
            if (vm.equipment.id !== null) {
                EquipmentManagementService.update(vm.equipment, onSaveSuccess, onSaveError);
            } else {
                EquipmentManagementService.save(vm.equipment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:equipmentUpdate', result);
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("yy_resource_equipment");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        vm.initJsComponents = function initJsComponents() {
            $('.equipment-edit-content form').parsley();
        }
    }
})();
