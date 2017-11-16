(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ProjectRate', ProjectRate);

    ProjectRate.$inject = ['$resource'];

    function ProjectRate ($resource) {
        var resourceUrl =  'api/project-rates/:id';

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
