(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_shoot_cost', {
                parent: 'yy_shoot',
                url: '/cost_management',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_COST','VIEW_OWNED_SHOOT_COST','EDIT_COST'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/cost_management/shoot-cost.html',
                        controller: 'ShootCostManagementController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_shoot_cost.view', {
                parent: 'yy_shoot_cost',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_COST','VIEW_OWNED_SHOOT_COST','EDIT_COST'],
                    pageTitle: '项目决算详情',
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/cost_management/shoot-cost-edit.html',
                        controller: 'ShootCostEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function disableEdit() {
                        return true;
                    }
                }
            })
            .state('yy_shoot_cost.edit', {
                parent: 'yy_shoot_cost',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_COST','VIEW_OWNED_SHOOT_COST','EDIT_COST'],
                    pageTitle: '项目决算',
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/cost_management/shoot-cost-edit.html',
                        controller: 'ShootCostEditController',
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
