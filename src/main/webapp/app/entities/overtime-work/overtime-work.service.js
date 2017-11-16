(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('OvertimeWork', OvertimeWork);

    OvertimeWork.$inject = ['$resource'];

    function OvertimeWork ($resource) {
        var resourceUrl =  'api/overtime-works/:id';

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
