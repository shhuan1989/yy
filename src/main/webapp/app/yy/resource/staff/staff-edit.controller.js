(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('StaffEditController', StaffEditController);

    StaffEditController.$inject = ['$timeout', '$scope', '$rootScope', '$state', '$stateParams', '$q', 'StaffManageService', 'disableEdit', 'DictionaryService'];

    function StaffEditController ($timeout, $scope, $rootScope, $state, $stateParams, $q, StaffManageService, disableEdit, DictionaryService) {
        var vm = this;

        vm.disableEdit = !(!disableEdit);
        DictionaryService.setOptions(vm);
        vm.pageTitle = $state.current.data.pageTitle;
        vm.staff = {};
        vm.isLoading = false;

        setDefaultsOFStaff();

        vm.save = save;
        vm.cancel = cancel;
        vm.load = load;
        vm.load();

        function load() {
            if ($stateParams.id != undefined) {
                vm.isLoading = true;
                StaffManageService.get(
                    {id: $stateParams.id},
                    function (entity) {
                        vm.staff = entity;
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
            var valid = $('.staff-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            vm.isSaving = true;
            if (vm.staff.id !== null) {
                StaffManageService.update(vm.staff, onSaveSuccess, onSaveError);
            } else {
                StaffManageService.save(vm.staff, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:staffUpdate', result);
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        function setDefaultsOFStaff() {

        }

        vm.initJsComponents = function initJsComponents() {
            $('.staff-edit-content form').parsley();
        }
    }
})();
