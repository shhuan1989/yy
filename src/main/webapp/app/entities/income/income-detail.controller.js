(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('IncomeDetailController', IncomeDetailController);

    IncomeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Income', 'Dictionary'];

    function IncomeDetailController($scope, $rootScope, $stateParams, previousState, entity, Income, Dictionary) {
        var vm = this;

        vm.income = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:incomeUpdate', function(event, result) {
            vm.income = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
