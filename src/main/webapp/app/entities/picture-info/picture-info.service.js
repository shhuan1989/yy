(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('PictureInfo', PictureInfo);

    PictureInfo.$inject = ['$resource'];

    function PictureInfo ($resource) {
        var resourceUrl =  'api/picture-infos/:id';

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
