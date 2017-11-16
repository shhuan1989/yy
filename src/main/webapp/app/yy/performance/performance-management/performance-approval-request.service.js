(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('PerformanceApprovalRequest', PerformanceApprovalRequest);

    PerformanceApprovalRequest.$inject = ['$resource'];

    function PerformanceApprovalRequest ($resource) {
        var resourceUrl =  'api/performance-approval-requests/:id';

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
