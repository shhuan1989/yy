(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employee', 'JobTitle', 'City', 'Dept', 'Project'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, previousState, entity, Employee, JobTitle, City, Dept, Project) {
        var vm = this;

        vm.employee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
