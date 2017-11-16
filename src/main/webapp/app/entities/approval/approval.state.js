(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('approval', {
            parent: 'entity',
            url: '/approval',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.approval.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/approval/approvals.html',
                    controller: 'ApprovalController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('approval');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('approval-detail', {
            parent: 'entity',
            url: '/approval/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.approval.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/approval/approval-detail.html',
                    controller: 'ApprovalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('approval');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Approval', function($stateParams, Approval) {
                    return Approval.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'approval',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('approval-detail.edit', {
            parent: 'approval-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/approval/approval-dialog.html',
                    controller: 'ApprovalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Approval', function(Approval) {
                            return Approval.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('approval.new', {
            parent: 'approval',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/approval/approval-dialog.html',
                    controller: 'ApprovalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createTime: null,
                                lastModifiedTime: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('approval', null, { reload: 'approval' });
                }, function() {
                    $state.go('approval');
                });
            }]
        })
        .state('approval.edit', {
            parent: 'approval',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/approval/approval-dialog.html',
                    controller: 'ApprovalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Approval', function(Approval) {
                            return Approval.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('approval', null, { reload: 'approval' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('approval.delete', {
            parent: 'approval',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/approval/approval-delete-dialog.html',
                    controller: 'ApprovalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Approval', function(Approval) {
                            return Approval.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('approval', null, { reload: 'approval' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
