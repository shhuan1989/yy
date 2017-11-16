(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ProjectPayment', ProjectPayment);

    ProjectPayment.$inject = ['$resource'];

    function ProjectPayment ($resource) {
        var resourceUrl =  'api/project-payments/:id';

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
