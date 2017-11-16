(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ProjectCost', ProjectCost);

    ProjectCost.$inject = ['$resource'];

    function ProjectCost ($resource) {
        var resourceUrl =  'api/project-costs/:id';

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
