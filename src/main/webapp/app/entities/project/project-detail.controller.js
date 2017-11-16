(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project', 'Client', 'Contract', 'Employee'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project, Client, Contract, Employee) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
