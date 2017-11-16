(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('RoleController', RoleController);

    RoleController.$inject = ['$scope', '$state', 'Role'];

    function RoleController ($scope, $state, Role) {
        var vm = this;
        
        vm.roles = [];

        loadAll();

        function loadAll() {
            Role.query(function(result) {
                vm.roles = result;
            });
        }
    }
})();
