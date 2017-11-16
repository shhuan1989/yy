(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ActorDetailController', ActorDetailController);

    ActorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Actor', 'PictureInfo', 'Dictionary'];

    function ActorDetailController($scope, $rootScope, $stateParams, previousState, entity, Actor, PictureInfo, Dictionary) {
        var vm = this;

        vm.actor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:actorUpdate', function(event, result) {
            vm.actor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
