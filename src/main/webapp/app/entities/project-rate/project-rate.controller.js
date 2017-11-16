(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProjectRateEntityController', ProjectRateEntityController);

    ProjectRateEntityController.$inject = ['$scope', '$state', 'ProjectRate'];

    function ProjectRateEntityController ($scope, $state, ProjectRate) {
        var vm = this;

        vm.projectRates = [];

        loadAll();

        function loadAll() {
            ProjectRate.query(function(result) {
                vm.projectRates = result;
            });
        }
    }
})();
