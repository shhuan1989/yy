(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('FileInfo', FileInfo);

    FileInfo.$inject = ['$resource'];

    function FileInfo ($resource) {
        var resourceUrl =  'api/file-infos/:id';

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
