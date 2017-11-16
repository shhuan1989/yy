(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('AssetBorrowRecordController', AssetBorrowRecordController);

    AssetBorrowRecordController.$inject = ['$scope', '$state', 'AssetBorrowRecord'];

    function AssetBorrowRecordController ($scope, $state, AssetBorrowRecord) {
        var vm = this;
        
        vm.assetBorrowRecords = [];

        loadAll();

        function loadAll() {
            AssetBorrowRecord.query(function(result) {
                vm.assetBorrowRecords = result;
            });
        }
    }
})();
