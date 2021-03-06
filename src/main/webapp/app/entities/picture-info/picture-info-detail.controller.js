(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('PictureInfoDetailController', PictureInfoDetailController);

    PictureInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PictureInfo'];

    function PictureInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, PictureInfo) {
        var vm = this;

        vm.pictureInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:pictureInfoUpdate', function(event, result) {
            vm.pictureInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
