(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ApprovalController', ApprovalController);

    ApprovalController.$inject = ['$scope', '$state', 'Approval'];

    function ApprovalController ($scope, $state, Approval) {
        var vm = this;
        
        vm.approvals = [];

        loadAll();

        function loadAll() {
            Approval.query(function(result) {
                vm.approvals = result;
            });
        }
    }
})();
