(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ApprovalService', ApprovalService);

    ApprovalService.$inject = ['$http'];

    function ApprovalService ($http) {
        var service = {};

        service.approve = approve;
        service.reject = reject;
        service.start = start;

        function approve(id) {
            return $http.post('/api/approvals/' + id + '/approve');
        }

        function reject(id, reason) {
            return $http.post('/api/approvals/' + id + '/reject', reason);
        }

        function start(id) {
            return $http.post('/api/approval-requests/' + id + '/start');
        }

        return service;
    }
})();
