(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectCostController', ProjectCostController);

    ProjectCostController.$inject = ['$scope', '$state', 'ProjectCost'];

    function ProjectCostController ($scope, $state, ProjectCost) {
        var vm = this;
        
        vm.projectCosts = [];

        loadAll();

        function loadAll() {
            ProjectCost.query(function(result) {
                vm.projectCosts = result;
            });
        }
    }
})();
