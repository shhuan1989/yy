(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ShootCostAuditService', ShootCostAuditService);

    ShootCostAuditService.$inject = ['$resource'];

    function ShootCostAuditService ($resource) {
        var resourceUrl =  'api/shoot-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    type: 2,
                    sort: ['project.idNumber', 'createTime']
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
