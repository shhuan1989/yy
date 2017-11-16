(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('role', {
            parent: 'entity',
            url: '/role',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.role.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/role/roles.html',
                    controller: 'RoleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('role');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('role-detail', {
            parent: 'entity',
            url: '/role/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.role.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/role/role-detail.html',
                    controller: 'RoleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('role');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Role', function($stateParams, Role) {
                    return Role.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'role',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('role-detail.edit', {
            parent: 'role-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/role/role-dialog.html',
                    controller: 'RoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Role', function(Role) {
                            return Role.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('role.new', {
            parent: 'role',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/role/role-dialog.html',
                    controller: 'RoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createTime: null,
                                lastModifiedTime: null,
                                auths: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('role', null, { reload: 'role' });
                }, function() {
                    $state.go('role');
                });
            }]
        })
        .state('role.edit', {
            parent: 'role',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/role/role-dialog.html',
                    controller: 'RoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Role', function(Role) {
                            return Role.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('role', null, { reload: 'role' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('role.delete', {
            parent: 'role',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/role/role-delete-dialog.html',
                    controller: 'RoleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Role', function(Role) {
                            return Role.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('role', null, { reload: 'role' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
