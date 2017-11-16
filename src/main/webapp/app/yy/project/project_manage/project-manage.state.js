(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_project_mgt', {
                parent: 'yy_project',
                url: '/project',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_PROJECT_MANAGE', 'EDIT_PROJECT_MANAGE','VIEW_PROJECT_MANAGE_ALL']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_manage/project-manage.html',
                        controller: 'ProjectManagementController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_project_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('yy_project_mgt.edit', {
                parent: 'yy_project_mgt',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_PROJECT_MANAGE'],
                    pageTitle: '编辑项目信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_manage/project-edit.html',
                        controller: 'ProjectManagementEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_project_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_project_mgt.view', {
                parent: 'yy_project_mgt',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_PROJECT_MANAGE', 'EDIT_PROJECT_MANAGE','VIEW_PROJECT_MANAGE_ALL'],
                    pageTitle: '查看项目信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_manage/project-edit.html',
                        controller: 'ProjectManagementEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return true;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_project_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_project_mgt.new', {
                parent: 'yy_project_mgt',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'NEW_PROJECT'],
                    pageTitle: '增加新的项目'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_manage/project-edit.html',
                        controller: 'ProjectManagementEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'project',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
