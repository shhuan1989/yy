(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dictionary', {
            parent: 'entity',
            url: '/dictionary?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Dictionaries'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dictionary/dictionaries.html',
                    controller: 'DictionaryController',
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
                }]
            }
        })
        .state('dictionary-detail', {
            parent: 'entity',
            url: '/dictionary/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Dictionary'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dictionary/dictionary-detail.html',
                    controller: 'DictionaryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Dictionary', function($stateParams, Dictionary) {
                    return Dictionary.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dictionary',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dictionary-detail.edit', {
            parent: 'dictionary-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dictionary/dictionary-dialog.html',
                    controller: 'DictionaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dictionary', function(Dictionary) {
                            return Dictionary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dictionary.new', {
            parent: 'dictionary',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dictionary/dictionary-dialog.html',
                    controller: 'DictionaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                creator: null,
                                createTime: null,
                                comment: null,
                                isSystem: null,
                                lastModifiedTime: null,
                                lastModifier: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dictionary', null, { reload: 'dictionary' });
                }, function() {
                    $state.go('dictionary');
                });
            }]
        })
        .state('dictionary.edit', {
            parent: 'dictionary',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dictionary/dictionary-dialog.html',
                    controller: 'DictionaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dictionary', function(Dictionary) {
                            return Dictionary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dictionary', null, { reload: 'dictionary' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dictionary.delete', {
            parent: 'dictionary',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dictionary/dictionary-delete-dialog.html',
                    controller: 'DictionaryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dictionary', function(Dictionary) {
                            return Dictionary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dictionary', null, { reload: 'dictionary' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
