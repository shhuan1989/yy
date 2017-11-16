(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('WksShootCostService', WksShootCostService);

    WksShootCostService.$inject = ['$resource'];

    function WksShootCostService ($resource) {
        var resourceUrl =  'api/shoot-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    type: 2,
                    sort: ['createTime,desc']
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
