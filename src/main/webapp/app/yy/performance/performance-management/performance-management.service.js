(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('PerformanceManagementService', PerformanceManagementService);

    PerformanceManagementService.$inject = ['$resource'];

    function PerformanceManagementService ($resource) {
        var resourceUrl =  'api/projects/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
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
