(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('FileService', FileService);

    FileService.$inject = ['$resource'];

    function FileService ($resource) {
        var resourceUrl =  'api/files/:id';

        return $resource(resourceUrl, {id: '@id'}, {
            'get': {
                method: 'GET',
                responseType: 'arraybuffer',
                transformResponse: function (data) {
                    return { data : data }
                }
            },
            'save': {
                method: 'POST',
                transformRequest: angular.identity,
                headers: { 'Content-Type': undefined }
            }
        });
    }
})();
