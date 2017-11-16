(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ProjectManagementService', ProjectManagementService);

    ProjectManagementService.$inject = ['$resource'];

    function ProjectManagementService ($resource) {
        var resourceUrl =  'api/projects/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['status,asc', 'createTime,desc'],
                    onlyActiveApproval: true
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
            'update': { method:'PUT' },
            'close': {
                method: 'POST',
                url: 'api/projects/:id/close'
            },
            'archive': {
                method: 'POST',
                url: 'api/projects/:id/archive'
            }
        });
    }
})();
