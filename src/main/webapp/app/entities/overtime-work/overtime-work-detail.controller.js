(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('OvertimeWorkDetailController', OvertimeWorkDetailController);

    OvertimeWorkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OvertimeWork', 'Employee', 'ApprovalRequest'];

    function OvertimeWorkDetailController($scope, $rootScope, $stateParams, previousState, entity, OvertimeWork, Employee, ApprovalRequest) {
        var vm = this;

        vm.overtimeWork = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:overtimeWorkUpdate', function(event, result) {
            vm.overtimeWork = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
