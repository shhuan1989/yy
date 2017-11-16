(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ProjectTimelineService', ProjectTimelineService);

    ProjectTimelineService.$inject = ['$resource'];

    function ProjectTimelineService ($resource) {
        var resourceUrl =  'api/project-timelines/:id';

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
