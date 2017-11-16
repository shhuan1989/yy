(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ProjectAuditService', ProjectAuditService);

    ProjectAuditService.$inject = ['$http'];

    function ProjectAuditService ($http) {
        var service = {};
        service.closeProjectRates = closeProjectRates;

        function closeProjectRates(projectId) {
            var url = '/api/projects/' + projectId + '/close_rates';
            return $http.post(url);
        }

        return service;
    }
})();
