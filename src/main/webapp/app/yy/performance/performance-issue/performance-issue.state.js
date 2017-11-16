(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_perf_issue', {
                parent: 'yy_perf',
                url: '/bonus',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_BONUS_ISSUE', 'EDIT_BONUS_ISSUE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/performance/performance-issue/performance-issue.html',
                        controller: 'PerformanceIssueController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_perf_issue',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_perf_issue.view', {
                parent: 'yy_perf_issue',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_BONUS_ISSUE', 'EDIT_BONUS_ISSUE'],
                    pageTitle: '绩效详情'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/performance/performance-issue/bonus-detail.html',
                        controller: 'PerformanceBonusDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_perf_issue',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
