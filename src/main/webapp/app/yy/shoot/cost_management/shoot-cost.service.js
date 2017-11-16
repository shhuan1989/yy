(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ShootCostService', ShootCostService);

    ShootCostService.$inject = ['$resource'];

    function ShootCostService ($resource) {
        var resourceUrl =  'api/shoot-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    type: 2,
                    sort: ['createTime,asc']
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
