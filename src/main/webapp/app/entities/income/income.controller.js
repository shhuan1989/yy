(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('IncomeController', IncomeController);

    IncomeController.$inject = ['$scope', '$state', 'Income'];

    function IncomeController ($scope, $state, Income) {
        var vm = this;
        
        vm.incomes = [];

        loadAll();

        function loadAll() {
            Income.query(function(result) {
                vm.incomes = result;
            });
        }
    }
})();
