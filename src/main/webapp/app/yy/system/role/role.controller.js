(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('SysRoleController', SysRoleController);

    SysRoleController.$inject = ['$scope', '$rootScope', '$state', 'SysRoleService', 'NgTableParams', 'UserService'];

    function SysRoleController ($scope, $rootScope, $state, SysRoleService, NgTableParams, UserService) {
        var vm = this;

        vm.searchParams = {};
        vm.rolesTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.rolesTableParams.count();
        vm.roles = [];
        vm.isLoading = false;
        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.rolesTableParams.count(vm.pageSize);
        });

        function search() {
            console.log("Search with params", vm.searchParams);
            vm.isLoading = true;
            SysRoleService.query(vm.searchParams, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.roles = data;

                var authDescs = UserService.authDescs();
                $.each(vm.roles, function (index, role) {
                    role.authDesc = role.auths.split(',').map((auth) => authDescs[auth]).join(',');
                });

                vm.rolesTableParams.settings({
                    dataset: vm.roles
                });
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }
    }
})();
