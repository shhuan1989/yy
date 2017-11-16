(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_contract_income', {
                parent: 'yy_contract',
                url: '/income',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_INCOME_MANAGE','EDIT_INCOME_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/income/income.html',
                        controller: 'ContractIncomeController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_contract_income.view', {
                parent: 'yy_contract_income',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_INCOME_MANAGE','EDIT_INCOME_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/income/income-view.html',
                        controller: 'ContractEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function () {
                        return true;
                    }
                }
            })
            .state('yy_contract_income.edit', {
                parent: 'yy_contract_income',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_INCOME_MANAGE','EDIT_INCOME_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/income/income-edit.html',
                        controller: 'IncomeEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_contract_income.new', {
                parent: 'yy_contract_income',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_INCOME_MANAGE','EDIT_INCOME_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/income/other-income-new.html',
                        controller: 'OtherIncomeEditController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
