(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('FileInfoDeleteController',FileInfoDeleteController);

    FileInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'FileInfo'];

    function FileInfoDeleteController($uibModalInstance, entity, FileInfo) {
        var vm = this;

        vm.fileInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FileInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
