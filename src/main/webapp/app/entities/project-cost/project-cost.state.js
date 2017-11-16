(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-cost', {
            parent: 'entity',
            url: '/project-cost',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.projectCost.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-cost/project-costs.html',
                    controller: 'ProjectCostController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectCost');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-cost-detail', {
            parent: 'entity',
            url: '/project-cost/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.projectCost.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-cost/project-cost-detail.html',
                    controller: 'ProjectCostDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectCost');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectCost', function($stateParams, ProjectCost) {
                    return ProjectCost.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-cost',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-cost-detail.edit', {
            parent: 'project-cost-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-cost/project-cost-dialog.html',
                    controller: 'ProjectCostDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectCost', function(ProjectCost) {
                            return ProjectCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-cost.new', {
            parent: 'project-cost',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-cost/project-cost-dialog.html',
                    controller: 'ProjectCostDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createTime: null,
                                amount: null,
                                info: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-cost', null, { reload: 'project-cost' });
                }, function() {
                    $state.go('project-cost');
                });
            }]
        })
        .state('project-cost.edit', {
            parent: 'project-cost',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-cost/project-cost-dialog.html',
                    controller: 'ProjectCostDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectCost', function(ProjectCost) {
                            return ProjectCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-cost', null, { reload: 'project-cost' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-cost.delete', {
            parent: 'project-cost',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-cost/project-cost-delete-dialog.html',
                    controller: 'ProjectCostDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectCost', function(ProjectCost) {
                            return ProjectCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-cost', null, { reload: 'project-cost' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
