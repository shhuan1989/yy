(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('yy_user-management', {
            parent: 'yy_system',
            url: '/user-management?page&sort',
            data: {
                authorities: ['ROLE_ADMIN','VIEW_USER_MANAGE','EDIT_USER_MANAGE'],
                pageTitle: 'userManagement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/yy/system/user-management/user-management.html',
                    controller: 'UserManagementController',
                    controllerAs: 'vm'
                }
            },            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user-management');
                    return $translate.refresh();
                }]

            }
        })
        .state('yy_user-management.new', {
            parent: 'yy_user-management',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','EDIT_USER_MANAGE'],
                pageTitle: '创建新用户'
            },
            views: {
                'content@': {
                    templateUrl: 'app/yy/system/user-management/user-management-edit.html',
                    controller: 'UserManagementEditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                previousState: ["$state", function($state) {
                    var currentStateData = {
                        name: $state.current.name || 'yy_user-management',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('yy_user-management.edit', {
                parent: 'yy_user-management',
                url: '/{login}/edit',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_USER_MANAGE'],
                    pageTitle: '编辑用户'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/system/user-management/user-management-edit.html',
                        controller: 'UserManagementEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_user-management',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
        .state('yy_user-management.view', {
                parent: 'yy_user-management',
                url: '/{login}/view',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_USER_MANAGE','EDIT_USER_MANAGE'],
                    pageTitle: '查看用户信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/system/user-management/user-management-edit.html',
                        controller: 'UserManagementEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_user-management',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
