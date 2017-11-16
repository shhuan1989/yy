(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('SysRoleService', SysRoleService);

    SysRoleService.$inject = ['$resource'];

    function SysRoleService ($resource) {
        var resourceUrl =  'api/roles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true, cache: true},
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
