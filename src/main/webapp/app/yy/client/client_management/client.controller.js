(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ClientManagementController', ClientManagementController);

    ClientManagementController.$inject = ['$scope', '$rootScope', '$state', 'ClientService', 'NgTableParams', 'DictionaryService'];

    function ClientManagementController ($scope, $rootScope, $state, ClientService, NgTableParams, DictionaryService) {
        var vm = this;

        DictionaryService.setOptions(vm);
        vm.searchParams = {};
        vm.clientsTableParams = $rootScope.defaultNgTableParams();
        vm.clients = [];
        vm.isLoading = false;
        vm.pageSize = vm.clientsTableParams.count();

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.clientsTableParams.count(vm.pageSize);
        });

        vm.deleteClient = function deleteClient(client) {

            bootboxConfirm("确认删除客户 <span class='text-aqua'>" + client.name + "</span> ?", function () {
                ClientService.delete({id: client.id},
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
            vm.isLoading = true;
            ClientService.query($.extend(true, {}, vm.searchParams), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.clients = data;
                vm.clientsTableParams.settings({
                    dataset: vm.clients
                });
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });
        };
    }
})();
