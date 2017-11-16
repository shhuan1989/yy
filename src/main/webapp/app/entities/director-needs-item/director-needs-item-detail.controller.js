(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorNeedsItemDetailController', DirectorNeedsItemDetailController);

    DirectorNeedsItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DirectorNeedsItem', 'DirectorNeeds'];

    function DirectorNeedsItemDetailController($scope, $rootScope, $stateParams, previousState, entity, DirectorNeedsItem, DirectorNeeds) {
        var vm = this;

        vm.directorNeedsItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:directorNeedsItemUpdate', function(event, result) {
            vm.directorNeedsItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
