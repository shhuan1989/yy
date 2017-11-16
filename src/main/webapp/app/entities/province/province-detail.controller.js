(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProvinceDetailController', ProvinceDetailController);

    ProvinceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Province', 'City'];

    function ProvinceDetailController($scope, $rootScope, $stateParams, previousState, entity, Province, City) {
        var vm = this;

        vm.province = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:provinceUpdate', function(event, result) {
            vm.province = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
