(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('WksBonusService', WksBonusService);

    WksBonusService.$inject = ['$resource'];

    function WksBonusService ($resource) {
        var resourceUrl =  'api/performance-bonuses/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                params: {
                    sort: ['status,asc', 'issueTime,desc', 'createTime,desc']
                },
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
        });
    }
})();
