(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('DirectorNeeds', DirectorNeeds);

    DirectorNeeds.$inject = ['$resource'];

    function DirectorNeeds ($resource) {
        var resourceUrl =  'api/director-needs/:id';

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
