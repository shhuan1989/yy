(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('PictureService', PictureService);

    PictureService.$inject = ['$resource'];

    function PictureService ($resource) {
        var resourceUrl =  'resource/pictures/:id';

        return $resource(resourceUrl, {id: '@id'}, {
            // 'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    var encoded = btoa (data);
                    var dataURL="data:image/jpeg;base64,"+encoded
                    return data;
                }
            }
        });
    }
})();
