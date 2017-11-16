(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('Notice', Notice);

    Notice.$inject = ['$resource'];

    function Notice ($resource) {
        var resourceUrl =  'api/notices/:id';

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
