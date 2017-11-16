(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ExpenseApprovalController', ExpenseApprovalController);

    ExpenseApprovalController.$inject = ['$scope', '$state', '$stateParams', 'ExpenseService', 'ApprovalService', 'UserService'];

    function ExpenseApprovalController ($scope, $state, $stateParams, ExpenseService, ApprovalService, UserService) {
        var vm = this;
        vm.expenseId = $stateParams.id;
        vm.expense = {};
        vm.isLoading = false;
        vm.currentUserId = -1;
        vm.approval = {};
        vm.load = load;


        UserService.currentUser().then(
            function (user) {
                vm.currentUserId = user != undefined ? user.id : -1;
                load();
            }
        );


        function load() {
            vm.isLoading = true;
            ExpenseService.get({id: vm.expenseId},
                function (expense) {
                    vm.expense = expense;
                    vm.approval = currentApproval(vm.expense, vm.currentUserId);
                    vm.isLoading = false;
                },
                function () {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                }
            );
        }

        vm.approve = function () {
            ApprovalService.approve(vm.approval.id)
                .then(function () {
                    PNotifyApproveSuccess();
                    $state.go("^");
                }, function () {
                    PNotifyApproveFail();
                });
        };

        vm.reject = function () {
            bootboxConfirmReject(function(reason){
                ApprovalService.reject(vm.approval.id, reason)
                    .then(function () {
                        PNotifyRejectSuccess();
                        $state.go("^");
                    }, function () {
                        PNotifyApproveFail();
                    });
            });
        };
    }
})();

