(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('WksVacationService', WksVacationService);

    WksVacationService.$inject = ['$resource'];

    function WksVacationService ($resource) {
        var resourceUrl =  'api/vacations/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['approvalRequest.status,asc', 'startTime,desc', 'createTime,desc']
                },
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
