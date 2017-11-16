(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('AssetBorrowRecord', AssetBorrowRecord);

    AssetBorrowRecord.$inject = ['$resource'];

    function AssetBorrowRecord ($resource) {
        var resourceUrl =  'api/asset-borrow-records/:id';

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
