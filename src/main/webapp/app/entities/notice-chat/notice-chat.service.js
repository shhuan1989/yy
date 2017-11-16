(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('NoticeChat', NoticeChat);

    NoticeChat.$inject = ['$resource'];

    function NoticeChat ($resource) {
        var resourceUrl =  'api/notice-chats/:id';

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
