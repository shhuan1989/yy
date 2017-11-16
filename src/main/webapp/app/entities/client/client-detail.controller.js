(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ClientDetailController', ClientDetailController);

    ClientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Client', 'Dictionary'];

    function ClientDetailController($scope, $rootScope, $stateParams, previousState, entity, Client, Dictionary) {
        var vm = this;

        vm.client = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:clientUpdate', function(event, result) {
            vm.client = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
