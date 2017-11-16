(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('CommentDetailController', CommentDetailController);

    CommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Comment', 'PictureInfo', 'FileInfo'];

    function CommentDetailController($scope, $rootScope, $stateParams, previousState, entity, Comment, PictureInfo, FileInfo) {
        var vm = this;

        vm.comment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:commentUpdate', function(event, result) {
            vm.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
