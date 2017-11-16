(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('WksShootConfigService', WksShootConfigService);

    WksShootConfigService.$inject = ['$resource'];

    function WksShootConfigService ($resource) {
        var resourceUrl =  'api/shoot-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    type: 0,
                    sort:['createTime,desc']
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
