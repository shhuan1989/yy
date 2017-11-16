(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('EquipmentManagementService', EquipmentManagementService);

    EquipmentManagementService.$inject = ['$resource'];

    function EquipmentManagementService ($resource) {
        var resourceUrl =  'api/equipment/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                cache: true,
                params: {
                    size: 2000
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
