(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('ExpenseService', ExpenseService);

    ExpenseService.$inject = ['$resource'];

    function ExpenseService ($resource) {
        var resourceUrl =  'api/expenses/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['payTime,desc', 'createTime,desc']
                }
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
