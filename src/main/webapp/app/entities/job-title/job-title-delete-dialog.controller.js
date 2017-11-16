(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('JobTitleDeleteController',JobTitleDeleteController);

    JobTitleDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobTitle'];

    function JobTitleDeleteController($uibModalInstance, entity, JobTitle) {
        var vm = this;

        vm.jobTitle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JobTitle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
