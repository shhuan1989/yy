(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('Approval', Approval);

    Approval.$inject = ['$resource'];

    function Approval ($resource) {
        var resourceUrl =  'api/approvals/:id';

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
