(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_contract_expense', {
                parent: 'yy_contract',
                url: '/expense',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_EXPENSE_MANAGE','EDIT_EXPENSE_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/expense/expense.html',
                        controller: 'ExpenseController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_contract_expense.view', {
                parent: 'yy_contract_expense',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_EXPENSE_MANAGE','EDIT_EXPENSE_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/expense/expense-view.html',
                        controller: 'ExpenseViewController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_contract_expense.new', {
                parent: 'yy_contract_expense',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_EXPENSE_MANAGE'],
                    pageTitle: '新增支出'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/expense/expense-edit.html',
                        controller: 'ExpenseEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_contract_expense.edit', {
                parent: 'yy_contract_expense',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_EXPENSE_MANAGE'],
                    pageTitle: '编辑支出'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/expense/expense-edit.html',
                        controller: 'ExpenseEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_contract_expense.viewx', {
                parent: 'yy_contract_expense',
                url: '/viewx_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_EXPENSE_MANAGE'],
                    pageTitle: '查看支出'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/expense/expense-edit.html',
                        controller: 'ExpenseEditController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
