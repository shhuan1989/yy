(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_project_ongoing', {
                parent: 'yy_project',
                url: '/ongoing_project',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_ONGOING_PROJECT', 'EDIT_ONGOING_PROJECT','VIEW_ONGOING_PROJECT_ALL']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_ongoing/project-ongoing.html',
                        controller: 'ProjectOngoingController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'home',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_project_ongoing.view', {
                parent: 'yy_project_ongoing',
                url: '/ongoing_project/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_ONGOING_PROJECT', 'EDIT_ONGOING_PROJECT','VIEW_ONGOING_PROJECT_ALL']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_ongoing/project-detail.html',
                        controller: 'ProjectDetailsController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_project_ongoing',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_project_ongoing.timeline', {
                parent: 'yy_project_ongoing',
                url: '/ongoing_project/{id}/timeline',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_ONGOING_PROJECT', 'EDIT_ONGOING_PROJECT','VIEW_ONGOING_PROJECT_ALL']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_ongoing/project-timeline.html',
                        controller: 'ProjectTimelineController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_project_ongoing',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_project_task_view', {
                parent: 'yy_project_ongoing',
                url: '/ongoing_project/{projectId}/tasks/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_ONGOING_PROJECT', 'EDIT_ONGOING_PROJECT','VIEW_ONGOING_PROJECT_ALL']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_ongoing/task-detail.html',
                        controller: 'ProjectTaskDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    editable: ['$state', function($state) {
                        return !$state.current.data.projectArchived;
                    }],
                    previousState: ["$state", '$stateParams', function($state, $stateParams) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_project_ongoing.view',
                            params: $stateParams,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_project_task_new', {
                parent: 'yy_project_ongoing',
                url: '/project/{id}/task/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_ONGOING_PROJECT'],
                    pageTitle: '增加新的任务'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_ongoing/task-detail-edit.html',
                        controller: 'ProjectTaskController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_project_ongoing',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_project_ongoing.progress', {
                parent: 'yy_project_ongoing',
                url: '/project/{id}/progress_table',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_PROJECT_PROGRESS_TABLE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_ongoing/progress-table.html',
                        controller: 'ProjectProgressTableController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_project_ongoing',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
        ;
    }
})();
