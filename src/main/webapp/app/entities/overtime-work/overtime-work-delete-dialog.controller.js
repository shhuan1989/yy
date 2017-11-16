(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('OvertimeWorkDeleteController',OvertimeWorkDeleteController);

    OvertimeWorkDeleteController.$inject = ['$uibModalInstance', 'entity', 'OvertimeWork'];

    function OvertimeWorkDeleteController($uibModalInstance, entity, OvertimeWork) {
        var vm = this;

        vm.overtimeWork = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OvertimeWork.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
