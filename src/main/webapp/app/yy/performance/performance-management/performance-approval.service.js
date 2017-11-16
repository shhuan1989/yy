(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('PerformanceApprovalService', PerformanceApprovalService);

    PerformanceApprovalService.$inject = ['$http'];

    function PerformanceApprovalService ($http) {
        var resourceUrl =  '/performance-approval-requests/{id}/start';
        var service = {};
        service.approve = approve;
        service.start = start;
        service.reject = reject;
        service.issue = issue;

        function approve(id) {
            return $http.post('/api/approvals/' + id + '/approve');
        }

        function start(id) {
            return $http.post('/api/performance-approval-requests/' + id + '/start');
        }

        function reject(id, reason) {
            return $http.post('/api/approvals/' + id + '/reject', reason);
        }

        function issue(id) {
            return $http.post('/api/performance-approval-requests/' + id + '/issue')
        }


        return service;
    }
})();
