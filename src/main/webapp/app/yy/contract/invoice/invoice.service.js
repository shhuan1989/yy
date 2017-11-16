(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ContractInvoiceService', ContractInvoiceService);

    ContractInvoiceService.$inject = ['$resource'];

    function ContractInvoiceService ($resource) {
        var resourceUrl =  'api/contract-invoices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
