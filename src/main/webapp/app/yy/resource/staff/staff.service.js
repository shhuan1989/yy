(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('StaffManageService', StaffManageService);

    StaffManageService.$inject = ['$resource'];

    function StaffManageService ($resource) {
        var resourceUrl =  'api/staff/:id';

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
