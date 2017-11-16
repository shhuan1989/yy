(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('Dictionary', Dictionary);

    Dictionary.$inject = ['$resource'];

    function Dictionary ($resource) {
        var resourceUrl =  'api/dictionaries/:id';

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
