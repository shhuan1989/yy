(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('IncomeDeleteController',IncomeDeleteController);

    IncomeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Income'];

    function IncomeDeleteController($uibModalInstance, entity, Income) {
        var vm = this;

        vm.income = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Income.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
