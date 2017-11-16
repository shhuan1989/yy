(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('picture-info', {
            parent: 'entity',
            url: '/picture-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.pictureInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/picture-info/picture-infos.html',
                    controller: 'PictureInfoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pictureInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('picture-info-detail', {
            parent: 'entity',
            url: '/picture-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.pictureInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/picture-info/picture-info-detail.html',
                    controller: 'PictureInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pictureInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PictureInfo', function($stateParams, PictureInfo) {
                    return PictureInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'picture-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('picture-info-detail.edit', {
            parent: 'picture-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/picture-info/picture-info-dialog.html',
                    controller: 'PictureInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PictureInfo', function(PictureInfo) {
                            return PictureInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('picture-info.new', {
            parent: 'picture-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/picture-info/picture-info-dialog.html',
                    controller: 'PictureInfoDialogController',
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
                    $state.go('picture-info', null, { reload: 'picture-info' });
                }, function() {
                    $state.go('picture-info');
                });
            }]
        })
        .state('picture-info.edit', {
            parent: 'picture-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/picture-info/picture-info-dialog.html',
                    controller: 'PictureInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PictureInfo', function(PictureInfo) {
                            return PictureInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('picture-info', null, { reload: 'picture-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('picture-info.delete', {
            parent: 'picture-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/picture-info/picture-info-delete-dialog.html',
                    controller: 'PictureInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PictureInfo', function(PictureInfo) {
                            return PictureInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('picture-info', null, { reload: 'picture-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
