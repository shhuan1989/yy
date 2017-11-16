'use strict';

(function () {
    'use strict';

    angular.module('yiyingOaApp').factory('ProjectCommonService', ProjectCommonService);

    ProjectCommonService.$inject = ['$http'];

    function ProjectCommonService($http) {
        var service = {};
        service.memberHasTask = memberHasTask;

        function memberHasTask(projectId, memberId) {
            var url = '/api/projects/' + projectId + '/members/'+memberId+'/hastask';
            return $http.get(url);
        }

        return service;
    }
})();
