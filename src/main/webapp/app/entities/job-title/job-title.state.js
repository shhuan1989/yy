(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-title', {
            parent: 'entity',
            url: '/job-title?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTitles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-title/job-titles.html',
                    controller: 'JobTitleController',
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
        .state('job-title-detail', {
            parent: 'entity',
            url: '/job-title/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTitle'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-title/job-title-detail.html',
                    controller: 'JobTitleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'JobTitle', function($stateParams, JobTitle) {
                    return JobTitle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'job-title',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('job-title-detail.edit', {
            parent: 'job-title-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-title/job-title-dialog.html',
                    controller: 'JobTitleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTitle', function(JobTitle) {
                            return JobTitle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-title.new', {
            parent: 'job-title',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-title/job-title-dialog.html',
                    controller: 'JobTitleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job-title', null, { reload: 'job-title' });
                }, function() {
                    $state.go('job-title');
                });
            }]
        })
        .state('job-title.edit', {
            parent: 'job-title',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-title/job-title-dialog.html',
                    controller: 'JobTitleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTitle', function(JobTitle) {
                            return JobTitle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-title', null, { reload: 'job-title' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-title.delete', {
            parent: 'job-title',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-title/job-title-delete-dialog.html',
                    controller: 'JobTitleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobTitle', function(JobTitle) {
                            return JobTitle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-title', null, { reload: 'job-title' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
