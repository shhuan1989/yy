(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorNeedsCommentDetailController', DirectorNeedsCommentDetailController);

    DirectorNeedsCommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DirectorNeedsComment', 'Employee'];

    function DirectorNeedsCommentDetailController($scope, $rootScope, $stateParams, previousState, entity, DirectorNeedsComment, Employee) {
        var vm = this;

        vm.directorNeedsComment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:directorNeedsCommentUpdate', function(event, result) {
            vm.directorNeedsComment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
