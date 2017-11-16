(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_admin_asset', {
                parent: 'yy_admin',
                url: '/asset',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_ASSET_MANAGE','EDIT_ASSET_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/asset/asset.html',
                        controller: 'AdminAssetController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_asset.new', {
                parent: 'yy_admin_asset',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_ASSET_MANAGE'],
                    pageTitle: '新增资产'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/asset/asset-edit.html',
                        controller: 'AdminAssetEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_asset.edit', {
                parent: 'yy_admin_asset',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_ASSET_MANAGE'],
                    pageTitle: '编辑资产'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/asset/asset-edit.html',
                        controller: 'AdminAssetEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_asset.view', {
                parent: 'yy_admin_asset',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_ASSET_MANAGE', 'EDIT_ASSET_MANAGE'],
                    pageTitle: '查看资产'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/asset/asset-edit.html',
                        controller: 'AdminAssetEditController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
