(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ApprovalRequestDetailController', ApprovalRequestDetailController);

    ApprovalRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ApprovalRequest', 'Employee'];

    function ApprovalRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, ApprovalRequest, Employee) {
        var vm = this;

        vm.approvalRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:approvalRequestUpdate', function(event, result) {
            vm.approvalRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
