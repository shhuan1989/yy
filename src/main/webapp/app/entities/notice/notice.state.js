(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notice', {
            parent: 'entity',
            url: '/notice',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.notice.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notice/notices.html',
                    controller: 'NoticeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notice');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('notice-detail', {
            parent: 'entity',
            url: '/notice/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.notice.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notice/notice-detail.html',
                    controller: 'NoticeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notice');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Notice', function($stateParams, Notice) {
                    return Notice.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notice',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('notice-detail.edit', {
            parent: 'notice-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notice/notice-dialog.html',
                    controller: 'NoticeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Notice', function(Notice) {
                            return Notice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notice.new', {
            parent: 'notice',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notice/notice-dialog.html',
                    controller: 'NoticeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                subject: null,
                                expireTime: null,
                                content: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notice', null, { reload: 'notice' });
                }, function() {
                    $state.go('notice');
                });
            }]
        })
        .state('notice.edit', {
            parent: 'notice',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notice/notice-dialog.html',
                    controller: 'NoticeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Notice', function(Notice) {
                            return Notice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notice', null, { reload: 'notice' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notice.delete', {
            parent: 'notice',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notice/notice-delete-dialog.html',
                    controller: 'NoticeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Notice', function(Notice) {
                            return Notice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notice', null, { reload: 'notice' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
