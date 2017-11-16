(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('DirectorDemandService', DirectorDemandService);

    DirectorDemandService.$inject = ['$resource'];

    function DirectorDemandService ($resource) {
        var resourceUrl =  'api/director-needs/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    type: 0,
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
