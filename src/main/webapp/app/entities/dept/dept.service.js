(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('Dept', Dept);

    Dept.$inject = ['$resource'];

    function Dept ($resource) {
        var resourceUrl =  'api/depts/:id';

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
