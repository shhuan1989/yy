(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('StatisticsService', StatisticsService);

    StatisticsService.$inject = ['$http'];

    function StatisticsService ($http) {
        var service = {};
        service.paymentStatistics = paymentStatistics;
        service.invoiceStatistics = invoiceStatistics;

        function paymentStatistics(timeRange) {
            var url = '/api/contract-payment';

            if (timeRange != undefined) {
                if (timeRange.timeFrom != undefined || timeRange.timeTo != undefined) {
                    url += '?';
                    if (timeRange.timeFrom != undefined) {
                        url += 'timeFrom=' + timeRange.timeFrom;
                    }
                    if (timeRange.timeTo != undefined) {
                        if (!url.endsWith('?')) {
                            url += '&';
                        }
                        url += 'timeTo=' + timeRange.timeTo;
                    }
                }
            }

            return $http.get(url);
        }

        function invoiceStatistics(timeRange) {
            var url = '/api/invoice-statistics';

            if (timeRange != undefined) {
                if (timeRange.timeFrom != undefined || timeRange.timeTo != undefined) {
                    url += '?';
                    if (timeRange.timeFrom != undefined) {
                        url += 'timeFrom=' + timeRange.timeFrom;
                    }
                    if (timeRange.timeTo != undefined) {
                        if (!url.endsWith('?')) {
                            url += '&';
                        }
                        url += 'timeTo=' + timeRange.timeTo;
                    }
                }
            }

            return $http.get(url);
        }

        return service;
    }
})();
