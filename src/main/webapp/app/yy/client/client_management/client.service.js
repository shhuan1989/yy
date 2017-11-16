(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ClientService', ClientService);

    ClientService.$inject = ['$resource'];

    function ClientService ($resource) {
        var resourceUrl =  'api/clients/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['status,asc', 'createTime,desc']
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
