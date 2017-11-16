(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('AdmVacationService', AdmVacationService);

    AdmVacationService.$inject = ['$resource'];

    function AdmVacationService ($resource) {
        var resourceUrl =  'api/vacations/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['createTime,desc', 'startTime,desc', 'approvalRequest.status,asc']
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
