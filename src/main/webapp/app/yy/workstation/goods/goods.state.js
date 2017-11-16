(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_wks_goods', {
                parent: 'yy_workstation',
                url: '/util_apply',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/goods/goods.html',
                        controller: 'WksGoodsController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_wks_goods.new', {
                parent: 'yy_wks_goods',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/goods/goods-edit.html',
                        controller: 'WksGoodsEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_wks_goods.view', {
                parent: 'yy_wks_goods',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/goods/goods-edit.html',
                        controller: 'WksGoodsEditController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
