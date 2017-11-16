(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('AssetBorrowRecordDeleteController',AssetBorrowRecordDeleteController);

    AssetBorrowRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'AssetBorrowRecord'];

    function AssetBorrowRecordDeleteController($uibModalInstance, entity, AssetBorrowRecord) {
        var vm = this;

        vm.assetBorrowRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AssetBorrowRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
