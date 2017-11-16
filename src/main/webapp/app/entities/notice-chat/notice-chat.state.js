(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notice-chat', {
            parent: 'entity',
            url: '/notice-chat',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.noticeChat.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notice-chat/notice-chats.html',
                    controller: 'NoticeChatController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('noticeChat');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('notice-chat-detail', {
            parent: 'entity',
            url: '/notice-chat/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.noticeChat.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notice-chat/notice-chat-detail.html',
                    controller: 'NoticeChatDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('noticeChat');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NoticeChat', function($stateParams, NoticeChat) {
                    return NoticeChat.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notice-chat',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('notice-chat-detail.edit', {
            parent: 'notice-chat-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notice-chat/notice-chat-dialog.html',
                    controller: 'NoticeChatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NoticeChat', function(NoticeChat) {
                            return NoticeChat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notice-chat.new', {
            parent: 'notice-chat',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notice-chat/notice-chat-dialog.html',
                    controller: 'NoticeChatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                text: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notice-chat', null, { reload: 'notice-chat' });
                }, function() {
                    $state.go('notice-chat');
                });
            }]
        })
        .state('notice-chat.edit', {
            parent: 'notice-chat',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notice-chat/notice-chat-dialog.html',
                    controller: 'NoticeChatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NoticeChat', function(NoticeChat) {
                            return NoticeChat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notice-chat', null, { reload: 'notice-chat' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notice-chat.delete', {
            parent: 'notice-chat',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notice-chat/notice-chat-delete-dialog.html',
                    controller: 'NoticeChatDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NoticeChat', function(NoticeChat) {
                            return NoticeChat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notice-chat', null, { reload: 'notice-chat' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
