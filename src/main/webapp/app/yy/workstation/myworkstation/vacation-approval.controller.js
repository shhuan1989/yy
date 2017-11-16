(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('VacationAprovalController', VacationAprovalController);

    VacationAprovalController.$inject = ['$scope', '$state', '$stateParams', 'WksVacationService', 'ApprovalService', 'UserService'];

    function VacationAprovalController ($scope, $state, $stateParams, WksVacationService, ApprovalService, UserService) {
        var vm = this;
        vm.vacationId = $stateParams.id;
        vm.vacation = {};
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
            WksVacationService.get({id: vm.vacationId},
                function (vacation) {
                    vm.vacation = vacation;
                    vm.approval = currentApproval(vm.vacation, vm.currentUserId);
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

