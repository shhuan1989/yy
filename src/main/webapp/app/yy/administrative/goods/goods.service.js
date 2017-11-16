(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('AdminGoodsService', AdminGoodsService);

    AdminGoodsService.$inject = ['$resource'];

    function AdminGoodsService ($resource) {
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
            'update': { method:'PUT' }
        });
    }
})();
