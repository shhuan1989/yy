(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ContractInvoiceController', ContractInvoiceController);

    ContractInvoiceController.$inject = ['$scope', '$state', '$rootScope', 'ContractInvoiceService', 'NgTableParams', 'DictionaryService', 'ContractManagementService'];

    function ContractInvoiceController ($scope, $state, $rootScope, ContractInvoiceService, NgTableParams, DictionaryService, ContractManagementService) {
        var vm = this;

        vm.searchParams = {};
        vm.contractsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.contractsTableParams.count();
        vm.contracts = [];
        vm.invoiceContract = {};
        vm.isLoading = false;
        vm.newInvoiceAmount = 0.0;
        vm.search = search;

        vm.search();

        $scope.$watch('vm.pageSize', function () {
            vm.contractsTableParams.count(vm.pageSize);
        });

        function search() {
            vm.isLoading = true;

            ContractManagementService.query($.extend(true,
                {},
                vm.searchParams
            ), (data) => {
                vm.contracts = data;

                if (vm.invoiceStatus == 1) {
                    vm.contracts = vm.contracts.filter((c) => !isNaN(c.invoicedAmount) && c.invoicedAmount > 0);
                } else if (vm.invoiceStatus == 2) {
                    vm.contracts = vm.contracts.filter((c) => isNaN(c.invoicedAmount) || c.invoicedAmount <= 0.0001);
                }

                vm.contractsTableParams.settings({
                    dataset: vm.contracts
                });

                vm.totalInvoiced = 0;
                vm.totalNotInvoiced = 0;
                vm.totalAmount = 0;
                $.each(vm.contracts, function (index, contract) {
                    vm.totalInvoiced += contract.invoicedAmount || 0;
                    vm.totalNotInvoiced += contract.notInvoicedAmount || 0;
                    vm.totalAmount += contract.moneyAmount || 0;
                });

                vm.isLoading = false;
            }, () => {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

        /******************************** new invoice****************************************/
        const PaymentModalId = '#modal-new-invoice';
        vm.newInvoice = function (contract) {
            vm.newInvoiceAmount = 0.0;
            vm.invoiceContract = contract;
            $(PaymentModalId).modal("show");
        };

        $(PaymentModalId).on('shown.bs.modal', function (e) {
            $(PaymentModalId+' form').parsley();
        });

        $scope.$watch('vm.newInvoiceAmount', () => {
           if (!isNaN(vm.newInvoiceAmount) && vm.invoiceContract != undefined) {
               if (!isNaN(vm.invoiceContract.notInvoicedAmount) && vm.newInvoiceAmount > vm.invoiceContract.notInvoicedAmount) {
                   vm.newInvoiceAmount = vm.invoiceContract.notInvoicedAmount;
               }
               return;
           }
           vm.newInvoiceAmount = 0.0;
        });

        vm.saveInvoice = function () {
            let valid = $(PaymentModalId+' form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            ContractInvoiceService.save({
                amount: vm.newInvoiceAmount,
                createTime: moment().valueOf(),
                contractId: vm.invoiceContract.id,
            },() => {
                PNotifySaveSuccess();
                vm.invoiceContract.notInvoicedAmount -= vm.newInvoiceAmount;
                vm.invoiceContract.invoicedAmount += vm.newInvoiceAmount;
                $(PaymentModalId).modal("hide");
            }, () => {
                PNotifySaveFail();
            })
        }
    }
})();
