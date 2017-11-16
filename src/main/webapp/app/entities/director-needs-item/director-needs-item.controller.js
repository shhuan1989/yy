(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DirectorNeedsItemController', DirectorNeedsItemController);

    DirectorNeedsItemController.$inject = ['$scope', '$state', 'DirectorNeedsItem'];

    function DirectorNeedsItemController ($scope, $state, DirectorNeedsItem) {
        var vm = this;
        
        vm.directorNeedsItems = [];

        loadAll();

        function loadAll() {
            DirectorNeedsItem.query(function(result) {
                vm.directorNeedsItems = result;
            });
        }
    }
})();
