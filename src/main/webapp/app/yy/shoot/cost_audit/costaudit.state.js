(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_shoot_cost_audit', {
                parent: 'yy_shoot',
                url: '/cost_audit',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_BUDGET_VS_COST','VIEW_OWNED_SHOOT_AUDIT']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/cost_audit/costaudit.html',
                        controller: 'ShootCostAuditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_shoot_cost_audit.view', {
                parent: 'yy_shoot_cost_audit',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_BUDGET_VS_COST','VIEW_OWNED_SHOOT_AUDIT']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/cost_audit/costaudit-view.html',
                        controller: 'ShootCostAuditEditController',
                        controllerAs: 'vm'
                    }
                }
            }) ;
    }
})();
