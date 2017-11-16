(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('FileInfoDetailController', FileInfoDetailController);

    FileInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FileInfo'];

    function FileInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, FileInfo) {
        var vm = this;

        vm.fileInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:fileInfoUpdate', function(event, result) {
            vm.fileInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
