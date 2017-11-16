(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_perf_deductmgt', {
                parent: 'yy_perf',
                url: '/deduct_management',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_DEDUCT_SETTING', 'EDIT_DEDUCT_SETTING']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/performance/deduct_management/deduct-manage.html',
                        controller: 'DeductManagementController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_perf_deductmgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_perf_deductmgt.edit', {
                parent: 'yy_perf_deductmgt',
                url: '/deduct_management/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_DEDUCT_SETTING'],
                    pageTitle: '编辑提成信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/performance/deduct_management/deduct-edit.html',
                        controller: 'DeductEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'DeductManagementService', function($stateParams, DeductManagementService) {
                        return DeductManagementService.get({
                            id: $stateParams.id
                        }).$promise;
                    }],
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_perf_deductmgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_perf_deductmgt.new', {
                parent: 'yy_perf_deductmgt',
                url: '/deduct_management/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_DEDUCT_SETTING'],
                    pageTitle: '新增员工提成比例'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/performance/deduct_management/deduct-edit.html',
                        controller: 'DeductEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function() {
                        return {
                            id: null,
                            employee: {}
                        };
                    },
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_perf_deductmgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
