(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('director-needs', {
            parent: 'entity',
            url: '/director-needs',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.directorNeeds.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/director-needs/director-needs.html',
                    controller: 'DirectorNeedsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('directorNeeds');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('director-needs-detail', {
            parent: 'entity',
            url: '/director-needs/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.directorNeeds.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/director-needs/director-needs-detail.html',
                    controller: 'DirectorNeedsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('directorNeeds');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DirectorNeeds', function($stateParams, DirectorNeeds) {
                    return DirectorNeeds.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'director-needs',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('director-needs-detail.edit', {
            parent: 'director-needs-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs/director-needs-dialog.html',
                    controller: 'DirectorNeedsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DirectorNeeds', function(DirectorNeeds) {
                            return DirectorNeeds.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('director-needs.new', {
            parent: 'director-needs',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs/director-needs-dialog.html',
                    controller: 'DirectorNeedsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('director-needs', null, { reload: 'director-needs' });
                }, function() {
                    $state.go('director-needs');
                });
            }]
        })
        .state('director-needs.edit', {
            parent: 'director-needs',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs/director-needs-dialog.html',
                    controller: 'DirectorNeedsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DirectorNeeds', function(DirectorNeeds) {
                            return DirectorNeeds.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('director-needs', null, { reload: 'director-needs' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('director-needs.delete', {
            parent: 'director-needs',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs/director-needs-delete-dialog.html',
                    controller: 'DirectorNeedsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DirectorNeeds', function(DirectorNeeds) {
                            return DirectorNeeds.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('director-needs', null, { reload: 'director-needs' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
