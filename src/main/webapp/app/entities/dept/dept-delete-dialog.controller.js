(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DeptDeleteController',DeptDeleteController);

    DeptDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dept'];

    function DeptDeleteController($uibModalInstance, entity, Dept) {
        var vm = this;

        vm.dept = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dept.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
