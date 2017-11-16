(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('AssetBorrowRecordDetailController', AssetBorrowRecordDetailController);

    AssetBorrowRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AssetBorrowRecord', 'Employee', 'Asset'];

    function AssetBorrowRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, AssetBorrowRecord, Employee, Asset) {
        var vm = this;

        vm.assetBorrowRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:assetBorrowRecordUpdate', function(event, result) {
            vm.assetBorrowRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
