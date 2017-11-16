(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('director-needs-comment', {
            parent: 'entity',
            url: '/director-needs-comment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.directorNeedsComment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/director-needs-comment/director-needs-comments.html',
                    controller: 'DirectorNeedsCommentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('directorNeedsComment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('director-needs-comment-detail', {
            parent: 'entity',
            url: '/director-needs-comment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.directorNeedsComment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/director-needs-comment/director-needs-comment-detail.html',
                    controller: 'DirectorNeedsCommentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('directorNeedsComment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DirectorNeedsComment', function($stateParams, DirectorNeedsComment) {
                    return DirectorNeedsComment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'director-needs-comment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('director-needs-comment-detail.edit', {
            parent: 'director-needs-comment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs-comment/director-needs-comment-dialog.html',
                    controller: 'DirectorNeedsCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DirectorNeedsComment', function(DirectorNeedsComment) {
                            return DirectorNeedsComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('director-needs-comment.new', {
            parent: 'director-needs-comment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs-comment/director-needs-comment-dialog.html',
                    controller: 'DirectorNeedsCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                content: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('director-needs-comment', null, { reload: 'director-needs-comment' });
                }, function() {
                    $state.go('director-needs-comment');
                });
            }]
        })
        .state('director-needs-comment.edit', {
            parent: 'director-needs-comment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs-comment/director-needs-comment-dialog.html',
                    controller: 'DirectorNeedsCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DirectorNeedsComment', function(DirectorNeedsComment) {
                            return DirectorNeedsComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('director-needs-comment', null, { reload: 'director-needs-comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('director-needs-comment.delete', {
            parent: 'director-needs-comment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director-needs-comment/director-needs-comment-delete-dialog.html',
                    controller: 'DirectorNeedsCommentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DirectorNeedsComment', function(DirectorNeedsComment) {
                            return DirectorNeedsComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('director-needs-comment', null, { reload: 'director-needs-comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
