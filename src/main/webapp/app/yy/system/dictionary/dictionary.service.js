(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('SystemDictionaryService', SystemDictionaryService);

    SystemDictionaryService.$inject = ['$resource'];

    function SystemDictionaryService ($resource) {
        var resourceUrl =  'api/dictionaries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true, cache: true},
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
