(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('DirectorNeedsItem', DirectorNeedsItem);

    DirectorNeedsItem.$inject = ['$resource'];

    function DirectorNeedsItem ($resource) {
        var resourceUrl =  'api/director-needs-items/:id';

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
