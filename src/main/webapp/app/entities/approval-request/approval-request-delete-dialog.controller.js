(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ApprovalRequestDeleteController',ApprovalRequestDeleteController);

    ApprovalRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApprovalRequest'];

    function ApprovalRequestDeleteController($uibModalInstance, entity, ApprovalRequest) {
        var vm = this;

        vm.approvalRequest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApprovalRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
