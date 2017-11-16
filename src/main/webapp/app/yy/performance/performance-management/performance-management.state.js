(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_perf_mgt', {
                parent: 'yy_perf',
                url: '/performance_management',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_PERFORMANCE_MANAGE', 'EDIT_PERFORMANCE_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/performance/performance-management/performance-management.html',
                        controller: 'PerformanceManagementController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_perf_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_perf_mgt.edit', {
                parent: 'yy_perf_mgt',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_PERFORMANCE_MANAGE'],
                    pageTitle: '项目绩效设置'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/performance/performance-management/performance-edit.html',
                        controller: 'PerformanceEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_perf_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_perf_mgt.view', {
                parent: 'yy_perf_mgt',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_PERFORMANCE_MANAGE', 'EDIT_PERFORMANCE_MANAGE'],
                    pageTitle: '项目绩效详情'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/performance/performance-management/performance-edit.html',
                        controller: 'PerformanceEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_perf_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
