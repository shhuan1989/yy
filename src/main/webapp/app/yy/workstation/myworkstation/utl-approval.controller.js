(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('UtilAprovalController', UtilAprovalController);

    UtilAprovalController.$inject = ['$scope', '$state', '$stateParams', 'WksGoodsService', 'ApprovalService', 'UserService'];

    function UtilAprovalController ($scope, $state, $stateParams, WksGoodsService, ApprovalService, UserService) {
        var vm = this;
        vm.utilApplicationId = $stateParams.id;
        vm.utilApplication = {};
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
            WksGoodsService.get({id: vm.utilApplicationId},
                function (utilApplication) {
                    vm.utilApplication = utilApplication;
                    vm.approval = currentApproval(vm.utilApplication, vm.currentUserId);
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
                }, function (resp) {
                    var headers = resp.headers();

                    var err = '内部错误，请重试！';
                    if (headers['x-yy-error'] == 'error.outofstock') {
                        err = '库存不足！';
                    }
                    PNotifyError(err);
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

