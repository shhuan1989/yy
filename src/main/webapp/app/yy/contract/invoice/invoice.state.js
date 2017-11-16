(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_contract_invoice', {
                parent: 'yy_contract',
                url: '/invoice',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_INVOICE_MANAGE','EDIT_INVOICE_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/invoice/invoice.html',
                        controller: 'ContractInvoiceController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_contract_invoice.view', {
                parent: 'yy_contract_invoice',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_INVOICE_MANAGE','EDIT_INVOICE_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/invoice/invoice-view.html',
                        controller: 'ContractInvoiceViewController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
