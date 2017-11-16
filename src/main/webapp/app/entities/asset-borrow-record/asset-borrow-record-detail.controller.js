(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('AssetBorrowRecordDetailController', AssetBorrowRecordDetailController);

    AssetBorrowRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AssetBorrowRecord', 'Employee', 'Asset'];

    function AssetBorrowRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, AssetBorrowRecord, Employee, Asset) {
        var vm = this;

        vm.assetBorrowRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:assetBorrowRecordUpdate', function(event, result) {
            vm.assetBorrowRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
