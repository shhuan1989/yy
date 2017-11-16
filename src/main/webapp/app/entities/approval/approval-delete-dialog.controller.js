(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ApprovalDeleteController',ApprovalDeleteController);

    ApprovalDeleteController.$inject = ['$uibModalInstance', 'entity', 'Approval'];

    function ApprovalDeleteController($uibModalInstance, entity, Approval) {
        var vm = this;

        vm.approval = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Approval.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
