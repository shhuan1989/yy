(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectPaymentDeleteController',ProjectPaymentDeleteController);

    ProjectPaymentDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectPayment'];

    function ProjectPaymentDeleteController($uibModalInstance, entity, ProjectPayment) {
        var vm = this;

        vm.projectPayment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectPayment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
