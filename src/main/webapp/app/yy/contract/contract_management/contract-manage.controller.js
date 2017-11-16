(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ContractManagementController', ContractManagementController);

    ContractManagementController.$inject = ['$scope', '$rootScope', '$state', 'ContractManagementService', 'NgTableParams', 'DictionaryService'];

    function ContractManagementController ($scope, $rootScope, $state, ContractManagementService, NgTableParams, DictionaryService) {
        var vm = this;

        DictionaryService.setOptions(vm);
        vm.searchParams = {};
        vm.contractsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.contractsTableParams.count();
        vm.contracts = [];
        vm.isLoading = false;

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.contractsTableParams.count(vm.pageSize);
        });

        vm.deleteContract = function deleteContract(contract) {

            bootboxConfirm("确认删除合同 <span class='text-aqua'>" + contract.name + "</span> ?", function () {
                ContractManagementService.delete({id: contract.id},
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
            ContractManagementService.query($.extend(true,
                {},
                vm.searchParams
            ), (data) => {
                vm.contracts = data;
                $.each(vm.contracts, (i, contract) => {
                   if (notEmptyArray(contract.installments)) {
                       contract.installments.sort((a, b) => Number(a.eta) - Number(b.eta));
                   }
                });
                vm.contractsTableParams.settings({
                    dataset: vm.contracts
                });
                vm.isLoading = false;
            }, () => {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

        vm.initSearchDateRangePicker = function () {
            $("#field_sign_date").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.searchParams.signDateFrom = start.valueOf();
                    vm.searchParams.signDateTo = end.valueOf();
                }
            );
        };
    }
})();
