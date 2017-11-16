(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_shoot_budget', {
                parent: 'yy_shoot',
                url: '/budget',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_BUDGET','VIEW_OWNED_SHOOT_BUDGET','EDIT_BUDGET'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/budget/budget.html',
                        controller: 'ShootBudgetController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_shoot_budget.view', {
                parent: 'yy_shoot_budget',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_BUDGET','VIEW_OWNED_SHOOT_BUDGET','EDIT_BUDGET'],
                    pageTitle: '拍摄预算申请详情',
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/budget/budget-append.html',
                        controller: 'ShootBudgetAppendController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_shoot_budget.edit', {
                parent: 'yy_shoot_budget',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_BUDGET'],
                    pageTitle: '编辑拍摄预算申请',
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/budget/budget-append.html',
                        controller: 'ShootBudgetAppendController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_shoot_budget.append', {
                parent: 'yy_shoot_budget',
                url: '/append_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_BUDGET']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/budget/budget-append.html',
                        controller: 'ShootBudgetAppendController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_shoot_budget.new', {
                parent: 'yy_shoot_budget',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_BUDGET'],
                    pageTitle: '新增拍摄预算申请'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/budget/budget-new.html',
                        controller: 'BudgetNewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function disableEdit() {
                        return false;
                    }
                }
            });
    }
})();
