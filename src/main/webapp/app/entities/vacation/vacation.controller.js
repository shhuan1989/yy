(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('VacationController', VacationController);

    VacationController.$inject = ['$scope', '$state', 'Vacation'];

    function VacationController ($scope, $state, Vacation) {
        var vm = this;
        
        vm.vacations = [];

        loadAll();

        function loadAll() {
            Vacation.query(function(result) {
                vm.vacations = result;
            });
        }
    }
})();
