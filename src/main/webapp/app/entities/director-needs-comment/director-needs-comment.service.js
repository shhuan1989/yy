(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('DirectorNeedsComment', DirectorNeedsComment);

    DirectorNeedsComment.$inject = ['$resource'];

    function DirectorNeedsComment ($resource) {
        var resourceUrl =  'api/director-needs-comments/:id';

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
