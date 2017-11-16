(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('staff', {
            parent: 'entity',
            url: '/staff?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.staff.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/staff/staff.html',
                    controller: 'StaffController',
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
                    $translatePartialLoader.addPart('staff');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('staff-detail', {
            parent: 'entity',
            url: '/staff/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.staff.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/staff/staff-detail.html',
                    controller: 'StaffDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('staff');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Staff', function($stateParams, Staff) {
                    return Staff.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'staff',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('staff-detail.edit', {
            parent: 'staff-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/staff/staff-dialog.html',
                    controller: 'StaffDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Staff', function(Staff) {
                            return Staff.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('staff.new', {
            parent: 'staff',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/staff/staff-dialog.html',
                    controller: 'StaffDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                tel: null,
                                price: null,
                                inputOperator: null,
                                createTime: null,
                                lastModifier: null,
                                lastModifiedTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('staff', null, { reload: 'staff' });
                }, function() {
                    $state.go('staff');
                });
            }]
        })
        .state('staff.edit', {
            parent: 'staff',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/staff/staff-dialog.html',
                    controller: 'StaffDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Staff', function(Staff) {
                            return Staff.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('staff', null, { reload: 'staff' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('staff.delete', {
            parent: 'staff',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/staff/staff-delete-dialog.html',
                    controller: 'StaffDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Staff', function(Staff) {
                            return Staff.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('staff', null, { reload: 'staff' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
