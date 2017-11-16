(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectRateDetailController', ProjectRateDetailController);

    ProjectRateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectRate', 'Employee', 'Project'];

    function ProjectRateDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectRate, Employee, Project) {
        var vm = this;

        vm.projectRate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:projectRateUpdate', function(event, result) {
            vm.projectRate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
