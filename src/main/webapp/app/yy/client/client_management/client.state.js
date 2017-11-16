(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_client_mgt', {
                parent: 'yy_client',
                url: '/client_management',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_CLIENT', 'EDIT_CLIENT']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/client/client_management/client.html',
                        controller: 'ClientManagementController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_client_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_client_mgt.edit', {
                parent: 'yy_client_mgt',
                url: '/client_management/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_CLIENT'],
                    pageTitle: '编辑客户信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/client/client_management/client-edit.html',
                        controller: 'ClientEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_client_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_client_mgt.view', {
                parent: 'yy_client_mgt',
                url: '/client_management/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_CLIENT', 'EDIT_CLIENT'],
                    pageTitle: '查看客户信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/client/client_management/client-edit.html',
                        controller: 'ClientEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return true;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_client_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_client_mgt.new', {
                parent: 'yy_client_mgt',
                url: '/client_management/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_CLIENT'],
                    pageTitle: '增加新的客户'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/client/client_management/client-edit.html',
                        controller: 'ClientEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_client_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
