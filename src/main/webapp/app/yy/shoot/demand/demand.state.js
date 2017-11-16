(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_director_needs', {
                parent: 'yy_shoot',
                url: '/director_needs',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_SHOOT_CONFIG','VIEW_OWNED_SHOOT_CONFIG','EDIT_SHOOT_CONFIG','EDIT_OWNED_SHOOT_CONFIG']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/demand/demand.html',
                        controller: 'DirectorDemandController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_director_needs.new', {
                parent: 'yy_director_needs',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_SHOOT_CONFIG'],
                    pageTitle: '新增拍摄配置'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/demand/demand-edit.html',
                        controller: 'DirectorDemandEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function disableEdit() {
                        return false;
                    }
                }
            })
            .state('yy_director_needs.view', {
                parent: 'yy_director_needs',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_SHOOT_CONFIG','VIEW_OWNED_SHOOT_CONFIG','EDIT_SHOOT_CONFIG','EDIT_OWNED_SHOOT_CONFIG'],
                    pageTitle: '拍摄配置详情'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/demand/demand-edit.html',
                        controller: 'DirectorDemandEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function disableEdit() {
                        return true;
                    }
                }
            })
            .state('yy_director_needs.edit', {
                parent: 'yy_director_needs',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_SHOOT_CONFIG','EDIT_OWNED_SHOOT_CONFIG'],
                    pageTitle: '编辑拍摄配置'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/demand/demand-edit.html',
                        controller: 'DirectorDemandEditController',
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
