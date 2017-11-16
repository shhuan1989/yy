(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('IncomeApprovalController', IncomeApprovalController);

    IncomeApprovalController.$inject = ['$scope', '$state', '$stateParams', 'OtherIncomeService', 'ApprovalService', 'UserService'];

    function IncomeApprovalController ($scope, $state, $stateParams, OtherIncomeService, ApprovalService, UserService) {
        var vm = this;
        vm.incomeId = $stateParams.id;
        vm.income = {};
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
            OtherIncomeService.get({id: vm.incomeId},
                function (income) {
                    vm.income = income;
                    vm.approval = currentApproval(vm.income, vm.currentUserId);
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

