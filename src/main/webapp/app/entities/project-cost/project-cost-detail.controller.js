(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectCostDetailController', ProjectCostDetailController);

    ProjectCostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectCost', 'Employee', 'Project'];

    function ProjectCostDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectCost, Employee, Project) {
        var vm = this;

        vm.projectCost = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:projectCostUpdate', function(event, result) {
            vm.projectCost = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
