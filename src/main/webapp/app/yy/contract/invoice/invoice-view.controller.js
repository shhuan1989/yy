(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ContractInvoiceViewController', ContractInvoiceViewController);

    ContractInvoiceViewController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'ContractManagementService', 'ContractInvoiceService'];

    function ContractInvoiceViewController ($timeout, $scope, $state, $stateParams, ContractManagementService, ContractInvoiceService) {
        const vm = this;

        vm.isLoading = false;
        vm.contract = {};

        vm.cancel = cancel;
        vm.load = load;

        vm.load();

        function load() {
            if ($stateParams.id != null) {
                vm.isLoading = true;

                let contractQuery = ContractManagementService.get({id: $stateParams.id}).$promise;
                let invoicesQuery = ContractInvoiceService.query({contractId: $stateParams.id}).$promise;

                $.when(contractQuery, invoicesQuery).then((contract, invoices) => {
                    vm.invoices = invoices;
                    vm.contract = contract;
                    vm.isLoading = false;
                }, () => {
                    vm.isLoading = false;
                });
            }
        }

        function cancel() {
            $state.go("^");
        }
    }
})();
