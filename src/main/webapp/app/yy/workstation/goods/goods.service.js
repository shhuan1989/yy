(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('WksGoodsService', WksGoodsService);

    WksGoodsService.$inject = ['$resource'];

    function WksGoodsService ($resource) {
        var resourceUrl =  'api/asset-borrow-records/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
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
            'update': { method:'PUT' },
            'delete': { method: 'DELETE' }
        });
    }
})();
