(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('IncomeDetailController', IncomeDetailController);

    IncomeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Income', 'Dictionary'];

    function IncomeDetailController($scope, $rootScope, $stateParams, previousState, entity, Income, Dictionary) {
        var vm = this;

        vm.income = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:incomeUpdate', function(event, result) {
            vm.income = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
