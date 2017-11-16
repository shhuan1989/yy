(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('Vacation', Vacation);

    Vacation.$inject = ['$resource'];

    function Vacation ($resource) {
        var resourceUrl =  'api/vacations/:id';

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
