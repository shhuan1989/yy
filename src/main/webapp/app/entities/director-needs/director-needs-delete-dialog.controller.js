(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DirectorNeedsDeleteController',DirectorNeedsDeleteController);

    DirectorNeedsDeleteController.$inject = ['$uibModalInstance', 'entity', 'DirectorNeeds'];

    function DirectorNeedsDeleteController($uibModalInstance, entity, DirectorNeeds) {
        var vm = this;

        vm.directorNeeds = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DirectorNeeds.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
