(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_admin_goods', {
                parent: 'yy_admin',
                url: '/asset_borrow_record',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_UTIL_APPLICATION_RECORDS','EDIT_UTIL_APPLICATION_RECORDS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/goods/goods.html',
                        controller: 'AdminGoodsController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_goods.view', {
                parent: 'yy_admin_goods',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_UTIL_APPLICATION_RECORDS','EDIT_UTIL_APPLICATION_RECORDS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/goods/goods-view.html',
                        controller: 'AdmGoodsViewController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
