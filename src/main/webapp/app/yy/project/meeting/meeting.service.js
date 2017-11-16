(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('MeetingService', MeetingService);

    MeetingService.$inject = ['$resource'];

    function MeetingService ($resource) {
        var resourceUrl =  'api/meetings/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['startTime,desc']
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
            'update': { method:'PUT' }
        });
    }
})();
