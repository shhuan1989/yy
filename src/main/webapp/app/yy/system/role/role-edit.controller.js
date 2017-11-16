(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('SysRoleEditController', SysRoleEditController);

    SysRoleEditController.$inject = ['$timeout', '$scope', '$rootScope', '$state', '$stateParams', '$q', 'SysRoleService', 'disableEdit', 'UserService'];

    function SysRoleEditController ($timeout, $scope, $rootScope, $state, $stateParams, $q, SysRoleService, disableEdit, UserService) {
        var vm = this;

        vm.disableEdit = !(!disableEdit);
        vm.pageTitle = $state.current.data.pageTitle;
        vm.role = {};
        vm.isLoading = false;
        vm.authoritiesList = UserService.authList();

        vm.save = save;
        vm.cancel = cancel;
        vm.load = load;
        vm.load();

        function load() {
            if ($stateParams.id != undefined) {
                vm.isLoading = true;
                SysRoleService.get(
                    {id: $stateParams.id},
                    function (entity) {
                        vm.role = entity;

                        if (vm.role.auths != undefined && vm.role.auths != '') {
                            $.each(vm.role.auths.split(','), function (index, auth) {
                                $.each(vm.authoritiesList, function (i1, as1) {
                                    $.each(as1.auths, function (i1, auth2) {
                                        if (auth == auth2.name) {
                                            auth2.selected = true;
                                        }
                                    })
                                });
                            });
                        }

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
            var valid = $('.role-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            vm.isSaving = true;

            var auths = ['ROLE_USER'];
            $.each(vm.authoritiesList, function (ai, as) {
                $.each(as.auths, function (i, auth) {
                    if (auth.selected) {
                        auths.push(auth.name);
                    }
                });
            });

            vm.role.auths = auths.join(",");

            if (vm.role.id !== null) {
                SysRoleService.update(vm.role, onSaveSuccess, onSaveError);
            } else {
                SysRoleService.save(vm.role, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        vm.initJsComponents = function initJsComponents() {
            $('.role-edit-content form').parsley();
        }
    }
})();
