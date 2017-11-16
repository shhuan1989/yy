(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProjectRateDeleteController',ProjectRateDeleteController);

    ProjectRateDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectRate'];

    function ProjectRateDeleteController($uibModalInstance, entity, ProjectRate) {
        var vm = this;

        vm.projectRate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectRate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
