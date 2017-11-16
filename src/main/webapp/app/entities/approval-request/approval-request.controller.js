(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ApprovalRequestController', ApprovalRequestController);

    ApprovalRequestController.$inject = ['$scope', '$state', 'ApprovalRequest'];

    function ApprovalRequestController ($scope, $state, ApprovalRequest) {
        var vm = this;
        
        vm.approvalRequests = [];

        loadAll();

        function loadAll() {
            ApprovalRequest.query(function(result) {
                vm.approvalRequests = result;
            });
        }
    }
})();
