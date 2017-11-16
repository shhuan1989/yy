(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DictionaryDetailController', DictionaryDetailController);

    DictionaryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dictionary'];

    function DictionaryDetailController($scope, $rootScope, $stateParams, previousState, entity, Dictionary) {
        var vm = this;

        vm.dictionary = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:dictionaryUpdate', function(event, result) {
            vm.dictionary = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
