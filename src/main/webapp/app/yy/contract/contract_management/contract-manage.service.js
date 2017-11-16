(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ContractManagementService', ContractManagementService);

    ContractManagementService.$inject = ['$resource'];

    function ContractManagementService ($resource) {
        var resourceUrl =  'api/contracts/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['paymentStatus,asc', 'signTime,desc']
                },
            },
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
