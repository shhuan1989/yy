(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('RoleDetailController', RoleDetailController);

    RoleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Role', 'Employee'];

    function RoleDetailController($scope, $rootScope, $stateParams, previousState, entity, Role, Employee) {
        var vm = this;

        vm.role = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:roleUpdate', function(event, result) {
            vm.role = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
