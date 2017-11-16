(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ContractDetailController', ContractDetailController);

    ContractDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contract', 'FileInfo'];

    function ContractDetailController($scope, $rootScope, $stateParams, previousState, entity, Contract, FileInfo) {
        var vm = this;

        vm.contract = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:contractUpdate', function(event, result) {
            vm.contract = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
