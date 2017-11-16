(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('VacationDetailController', VacationDetailController);

    VacationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vacation', 'Employee'];

    function VacationDetailController($scope, $rootScope, $stateParams, previousState, entity, Vacation, Employee) {
        var vm = this;

        vm.vacation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:vacationUpdate', function(event, result) {
            vm.vacation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
