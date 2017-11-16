(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('AdminNoticeService', AdminNoticeService);

    AdminNoticeService.$inject = ['$resource'];

    function AdminNoticeService ($resource) {
        var resourceUrl =  'api/notices/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['startTime,desc','createTime,desc']
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
