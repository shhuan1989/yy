(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('WksShootBudgetService', WksShootBudgetService);

    WksShootBudgetService.$inject = ['$resource'];

    function WksShootBudgetService ($resource) {
        var resourceUrl =  'api/shoot-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    type: 1,
                    sort: ['createTime,desc']
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
