(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ApprovalDetailController', ApprovalDetailController);

    ApprovalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Approval'];

    function ApprovalDetailController($scope, $rootScope, $stateParams, previousState, entity, Approval) {
        var vm = this;

        vm.approval = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:approvalUpdate', function(event, result) {
            vm.approval = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
