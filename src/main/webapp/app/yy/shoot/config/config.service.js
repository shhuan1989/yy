(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ShootConfigService', ShootConfigService);

    ShootConfigService.$inject = ['$resource'];

    function ShootConfigService ($resource) {
        var resourceUrl =  'api/shoot-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    type: 0,
                    sort: ['approvalRequest.status,asc']
                }
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
