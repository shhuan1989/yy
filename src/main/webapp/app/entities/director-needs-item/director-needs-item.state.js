(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('director-needs-item', {
            parent: 'entity',
            url: '/director-needs-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.directorNeedsItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/director-needs-item/director-needs-items.html',
                    controller: 'DirectorNeedsItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('directorNeedsItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('director-needs-item-detail', {
            parent: 'entity',
            url: '/director-needs-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.directorNeedsItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/director-needs-item/director-needs-item-detail.html',
                    controller: 'DirectorNeedsItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('directorNeedsItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DirectorNeedsItem', function($stateParams, DirectorNeedsItem) {
                    return DirectorNeedsItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'director-needs-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('director-needs-item-detail.edit', {
            parent: 'director-needs-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs-item/director-needs-item-dialog.html',
                    controller: 'DirectorNeedsItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DirectorNeedsItem', function(DirectorNeedsItem) {
                            return DirectorNeedsItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('director-needs-item.new', {
            parent: 'director-needs-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs-item/director-needs-item-dialog.html',
                    controller: 'DirectorNeedsItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                amount: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('director-needs-item', null, { reload: 'director-needs-item' });
                }, function() {
                    $state.go('director-needs-item');
                });
            }]
        })
        .state('director-needs-item.edit', {
            parent: 'director-needs-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs-item/director-needs-item-dialog.html',
                    controller: 'DirectorNeedsItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DirectorNeedsItem', function(DirectorNeedsItem) {
                            return DirectorNeedsItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('director-needs-item', null, { reload: 'director-needs-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('director-needs-item.delete', {
            parent: 'director-needs-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs-item/director-needs-item-delete-dialog.html',
                    controller: 'DirectorNeedsItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DirectorNeedsItem', function(DirectorNeedsItem) {
                            return DirectorNeedsItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('director-needs-item', null, { reload: 'director-needs-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
