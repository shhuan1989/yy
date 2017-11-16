(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ProjectProgressTableService', ProjectProgressTableService);

    ProjectProgressTableService.$inject = ['$resource'];

    function ProjectProgressTableService ($resource) {
        var resourceUrl =  'api/project-progress-tables/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
