(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('approval-request', {
            parent: 'entity',
            url: '/approval-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.approvalRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/approval-request/approval-requests.html',
                    controller: 'ApprovalRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('approvalRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('approval-request-detail', {
            parent: 'entity',
            url: '/approval-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.approvalRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/approval-request/approval-request-detail.html',
                    controller: 'ApprovalRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('approvalRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApprovalRequest', function($stateParams, ApprovalRequest) {
                    return ApprovalRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'approval-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('approval-request-detail.edit', {
            parent: 'approval-request-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/approval-request/approval-request-dialog.html',
                    controller: 'ApprovalRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApprovalRequest', function(ApprovalRequest) {
                            return ApprovalRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('approval-request.new', {
            parent: 'approval-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/approval-request/approval-request-dialog.html',
                    controller: 'ApprovalRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createTime: null,
                                lastModifiedTime: null,
                                name: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('approval-request', null, { reload: 'approval-request' });
                }, function() {
                    $state.go('approval-request');
                });
            }]
        })
        .state('approval-request.edit', {
            parent: 'approval-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/approval-request/approval-request-dialog.html',
                    controller: 'ApprovalRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApprovalRequest', function(ApprovalRequest) {
                            return ApprovalRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('approval-request', null, { reload: 'approval-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('approval-request.delete', {
            parent: 'approval-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/approval-request/approval-request-delete-dialog.html',
                    controller: 'ApprovalRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApprovalRequest', function(ApprovalRequest) {
                            return ApprovalRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('approval-request', null, { reload: 'approval-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
