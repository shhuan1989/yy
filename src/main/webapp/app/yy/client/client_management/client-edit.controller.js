(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ClientEditController', ClientEditController);

    ClientEditController.$inject = ['$timeout', '$scope', '$rootScope', '$state', '$stateParams', '$q', 'ClientService', 'disableEdit', 'DictionaryService'];

    function ClientEditController ($timeout, $scope, $rootScope, $state, $stateParams, $q, ClientService, disableEdit, DictionaryService) {
        var vm = this;

        DictionaryService.setOptions(vm);
        vm.disableEdit = !(!disableEdit);
        vm.pageTitle = $state.current.data.pageTitle;
        vm.client = {};
        vm.isLoading = false;

        vm.save = save;
        vm.cancel = cancel;
        vm.load = load;

        vm.load();

        function load() {
            if ($stateParams.id == undefined) {
                setDefaultsOFClient();
            } else {
                vm.isLoading = true;
                ClientService.get(
                    {id: $stateParams.id},
                    function (entity) {
                        vm.client = entity;
                        setDefaultsOFClient();
                        vm.isLoading = false;
                    },
                    function () {
                        PNotifyLoadFail();
                        vm.isLoading = false;
                    }
                )
            }
        }

        function cancel() {
            $state.go("^");
        }

        function save () {
            var valid = true;
            $.each($('.client-edit-content form').parsley(), function (i, p) {
                if (!valid || !p.validate()) {
                    valid = false;
                }
            });

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            vm.isSaving = true;
            if (vm.client.id !== null) {
                ClientService.update(vm.client, onSaveSuccess, onSaveError);
            } else {
                ClientService.save(vm.client, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:clientUpdate', result);
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        function setDefaultsOFClient() {

        }

        vm.initJsComponents = function initJsComponents() {
            $('.client-edit-content form').parsley();
            $('.select2').select2({
                language: "zh-CN"
            });
        };
    }
})();
