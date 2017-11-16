(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('CityDetailController', CityDetailController);

    CityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'City', 'Province'];

    function CityDetailController($scope, $rootScope, $stateParams, previousState, entity, City, Province) {
        var vm = this;

        vm.city = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:cityUpdate', function(event, result) {
            vm.city = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
