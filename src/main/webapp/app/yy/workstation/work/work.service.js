(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('WksWorkService', WksWorkService);

    WksWorkService.$inject = ['$resource'];

    function WksWorkService ($resource) {
        var resourceUrl =  'api/overtime-works/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['approvalRequest.status,asc', 'startTime,desc', 'createTime,desc']
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
