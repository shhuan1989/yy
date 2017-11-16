(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-rate', {
            parent: 'entity',
            url: '/project-rate',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.projectRate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-rate/project-rates.html',
                    controller: 'ProjectRateEntityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectRate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-rate-detail', {
            parent: 'entity',
            url: '/project-rate/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.projectRate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-rate/project-rate-detail.html',
                    controller: 'ProjectRateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectRate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectRate', function($stateParams, ProjectRate) {
                    return ProjectRate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-rate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-rate-detail.edit', {
            parent: 'project-rate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-rate/project-rate-dialog.html',
                    controller: 'ProjectRateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectRate', function(ProjectRate) {
                            return ProjectRate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-rate.new', {
            parent: 'project-rate',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-rate/project-rate-dialog.html',
                    controller: 'ProjectRateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-rate', null, { reload: 'project-rate' });
                }, function() {
                    $state.go('project-rate');
                });
            }]
        })
        .state('project-rate.edit', {
            parent: 'project-rate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-rate/project-rate-dialog.html',
                    controller: 'ProjectRateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectRate', function(ProjectRate) {
                            return ProjectRate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-rate', null, { reload: 'project-rate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-rate.delete', {
            parent: 'project-rate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-rate/project-rate-delete-dialog.html',
                    controller: 'ProjectRateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectRate', function(ProjectRate) {
                            return ProjectRate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-rate', null, { reload: 'project-rate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
