(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DirectorNeedsDetailController', DirectorNeedsDetailController);

    DirectorNeedsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DirectorNeeds', 'Project'];

    function DirectorNeedsDetailController($scope, $rootScope, $stateParams, previousState, entity, DirectorNeeds, Project) {
        var vm = this;

        vm.directorNeeds = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:directorNeedsUpdate', function(event, result) {
            vm.directorNeeds = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
