(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('WorkAprovalController', WorkAprovalController);

    WorkAprovalController.$inject = ['$scope', '$state', '$stateParams', 'WksWorkService', 'ApprovalService', 'UserService'];

    function WorkAprovalController ($scope, $state, $stateParams, WksWorkService, ApprovalService, UserService) {
        var vm = this;
        vm.workId = $stateParams.id;
        vm.work = {};
        vm.approval = {};
        vm.isLoading = false;
        vm.currentUserId = -1;
        vm.load = load;


        UserService.currentUser().then(
            function (user) {
                vm.currentUserId = user != undefined ? user.id : -1;
                load();
            }
        );


        function load() {
            vm.isLoading = true;
            WksWorkService.get({id: vm.workId},
                function (work) {
                    vm.work = work;
                    vm.approval = currentApproval(vm.work, vm.currentUserId);
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

