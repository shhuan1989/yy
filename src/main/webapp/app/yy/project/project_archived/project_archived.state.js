(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_project_archived', {
                parent: 'yy_project',
                url: '/archived_project',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_ARCHIVED_PROJECT_ALL','VIEW_ARCHIVED_PROJECT']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_archived/project_archived.html',
                        controller: 'ProjectArchivedController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'archived_project',
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
            .state('yy_project_archived.view', {
                parent: 'yy_project',
                url: '/archived_project/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_ARCHIVED_PROJECT_ALL','VIEW_ARCHIVED_PROJECT']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_archived/archived_timeline.html',
                        controller: 'ArchivedTimelineController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'archived_project',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params),
                            pageTitle: '项目日志'
                        };
                        return currentStateData;
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    }
})();
