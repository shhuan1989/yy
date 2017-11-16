(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ActorRepoService', ActorRepoService);

    ActorRepoService.$inject = ['$resource'];

    function ActorRepoService ($resource) {
        var resourceUrl =  'api/actors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false, cache: true},
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
