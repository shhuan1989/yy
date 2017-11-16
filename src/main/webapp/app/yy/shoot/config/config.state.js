(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_shoot_config', {
                parent: 'yy_shoot',
                url: '/config',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_SHOOT_CONFIG','VIEW_OWNED_SHOOT_CONFIG','EDIT_SHOOT_CONFIG','EDIT_OWNED_SHOOT_CONFIG']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/config/config.html',
                        controller: 'ShootConfigController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_shoot_config.new', {
                parent: 'yy_shoot_config',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_SHOOT_CONFIG'],
                    pageTitle: '新增拍摄配置申请'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/config/config-edit.html',
                        controller: 'ShootConfigEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function disableEdit() {
                        return false;
                    }
                }
            })
            .state('yy_shoot_config.view', {
                parent: 'yy_shoot_config',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_SHOOT_CONFIG','VIEW_OWNED_SHOOT_CONFIG','EDIT_SHOOT_CONFIG','EDIT_OWNED_SHOOT_CONFIG'],
                    pageTitle: '拍摄配置申请详情'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/config/config-edit.html',
                        controller: 'ShootConfigEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function disableEdit() {
                        return true;
                    }
                }
            })
            .state('yy_shoot_config.edit', {
                parent: 'yy_shoot_config',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_SHOOT_CONFIG','EDIT_OWNED_SHOOT_CONFIG'],
                    pageTitle: '编辑拍摄配置申请'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/config/config-edit.html',
                        controller: 'ShootConfigEditController',
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
