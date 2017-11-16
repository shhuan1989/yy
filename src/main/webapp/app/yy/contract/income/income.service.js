(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('ContractIncomeService', ContractIncomeService);

    ContractIncomeService.$inject = ['$http'];

    function ContractIncomeService ($http) {
        var service = {};

        service.payNextInstallment = payNextInstallment;

        function payNextInstallment(id, income) {
            return $http.post('api/contracts/' + id + '/pay_next_installment', income);
        }

        return service;
    }
})();
