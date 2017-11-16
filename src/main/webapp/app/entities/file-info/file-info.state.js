(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('file-info', {
            parent: 'entity',
            url: '/file-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.fileInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file-info/file-infos.html',
                    controller: 'FileInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fileInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('file-info-detail', {
            parent: 'entity',
            url: '/file-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.fileInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file-info/file-info-detail.html',
                    controller: 'FileInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fileInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FileInfo', function($stateParams, FileInfo) {
                    return FileInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'file-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('file-info-detail.edit', {
            parent: 'file-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-info/file-info-dialog.html',
                    controller: 'FileInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FileInfo', function(FileInfo) {
                            return FileInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file-info.new', {
            parent: 'file-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-info/file-info-dialog.html',
                    controller: 'FileInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                originName: null,
                                name: null,
                                createTime: null,
                                creator: null,
                                lastModifier: null,
                                lastModifiedTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('file-info', null, { reload: 'file-info' });
                }, function() {
                    $state.go('file-info');
                });
            }]
        })
        .state('file-info.edit', {
            parent: 'file-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-info/file-info-dialog.html',
                    controller: 'FileInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FileInfo', function(FileInfo) {
                            return FileInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file-info', null, { reload: 'file-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file-info.delete', {
            parent: 'file-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-info/file-info-delete-dialog.html',
                    controller: 'FileInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FileInfo', function(FileInfo) {
                            return FileInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file-info', null, { reload: 'file-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
