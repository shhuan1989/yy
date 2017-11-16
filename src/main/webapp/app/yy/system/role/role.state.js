(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_sys_role', {
                parent: 'yy_system',
                url: '/role',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_SYS_ROLE','EDIT_SYS_ROLE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/system/role/role.html',
                        controller: 'SysRoleController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_sys_role',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_sys_role.edit', {
                parent: 'yy_sys_role',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_SYS_ROLE'],
                    pageTitle: '编辑角色信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/system/role/role-edit.html',
                        controller: 'SysRoleEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_sys_role',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_sys_role.view', {
                parent: 'yy_sys_role',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_SYS_ROLE','EDIT_SYS_ROLE'],
                    pageTitle: '查看角色信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/system/role/role-edit.html',
                        controller: 'SysRoleEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return true;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_sys_role',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_sys_role.new', {
                parent: 'yy_sys_role',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_SYS_ROLE'],
                    pageTitle: '增加新的角色'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/system/role/role-edit.html',
                        controller: 'SysRoleEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_sys_role',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
