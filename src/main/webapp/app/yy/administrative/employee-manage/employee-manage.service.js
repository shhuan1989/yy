(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('EmployeeService', EmployeeService);

    EmployeeService.$inject = ['$resource'];

    function EmployeeService ($resource) {
        var resourceUrl =  'api/employees/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                params: {
                    sort: ['id']
                },
                isArray: true,
            },
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
