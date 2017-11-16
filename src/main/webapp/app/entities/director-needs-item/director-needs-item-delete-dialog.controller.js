(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorNeedsItemDeleteController',DirectorNeedsItemDeleteController);

    DirectorNeedsItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'DirectorNeedsItem'];

    function DirectorNeedsItemDeleteController($uibModalInstance, entity, DirectorNeedsItem) {
        var vm = this;

        vm.directorNeedsItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DirectorNeedsItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
