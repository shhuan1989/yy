(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('AdminAssetService', AdminAssetService);

    AdminAssetService.$inject = ['$resource'];

    function AdminAssetService ($resource) {
        var resourceUrl =  'api/assets/:id';

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
