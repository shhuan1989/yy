(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ProjectRatePrivilegeService', ProjectRatePrivilegeService);

    ProjectRatePrivilegeService.$inject = ['$resource'];

    function ProjectRatePrivilegeService ($resource) {
        var resourceUrl =  'api/project-rates/privileges';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
        });
    }
})();
