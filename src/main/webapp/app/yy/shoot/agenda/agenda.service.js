(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ShootAgendaService', ShootAgendaService);

    ShootAgendaService.$inject = ['$resource'];

    function ShootAgendaService ($resource) {
        var resourceUrl =  'api/shoot-agenda/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['startTime,desc']
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
