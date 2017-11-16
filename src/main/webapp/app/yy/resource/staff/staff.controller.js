(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('StaffManageControler', StaffManageControler);

    StaffManageControler.$inject = ['$scope', '$rootScope', '$state', 'StaffManageService', 'NgTableParams', 'DictionaryService'];

    function StaffManageControler ($scope, $rootScope, $state, StaffManageService, NgTableParams, DictionaryService) {
        var vm = this;

        DictionaryService.setOptions(vm);
        vm.searchParams = {};
        vm.staffsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.staffsTableParams.count();
        vm.staffs = [];
        vm.isLoading = false;

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.staffsTableParams.count(vm.pageSize);
        });

        vm.deleteStaff = function deleteStaff(staff) {

            bootboxConfirm("确认删除工作人员 <span class='text-aqua'>" + staff.name + "</span> ?", function () {
                StaffManageService.delete({id: staff.id},
                    function () {
                        vm.search();
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        };

        function search() {
            console.log("Search with params", vm.searchParams);
            vm.isLoading = true;
            StaffManageService.query($.extend(true,
                {
                    page: 0,
                    size: 10000000,
                    sort: ['id']
                },
                vm.searchParams
            ), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.staffs = data;
                vm.staffsTableParams.settings({
                    dataset: vm.staffs
                });
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }
    }
})();
