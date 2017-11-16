(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorNeedsController', DirectorNeedsController);

    DirectorNeedsController.$inject = ['$scope', '$state', 'DirectorNeeds'];

    function DirectorNeedsController ($scope, $state, DirectorNeeds) {
        var vm = this;
        
        vm.directorNeeds = [];

        loadAll();

        function loadAll() {
            DirectorNeeds.query(function(result) {
                vm.directorNeeds = result;
            });
        }
    }
})();
