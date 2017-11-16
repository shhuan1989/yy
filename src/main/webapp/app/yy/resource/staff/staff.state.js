(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_resource_staff', {
                parent: 'yy_resource',
                url: '/staff',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_STAFFS','EDIT_STAFFS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/staff/staff.html',
                        controller: 'StaffManageControler',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_staff',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_resource_staff.edit', {
                parent: 'yy_resource_staff',
                url: '/edit__{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_STAFFS'],
                    pageTitle: '编辑工作人员信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/staff/staff-edit.html',
                        controller: 'StaffEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_staff',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_resource_staff.view', {
                parent: 'yy_resource_staff',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_STAFFS','EDIT_STAFFS'],
                    pageTitle: '查看工作人员信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/staff/staff-edit.html',
                        controller: 'StaffEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return true;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_staff',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_resource_staff.new', {
                parent: 'yy_resource_staff',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_STAFFS'],
                    pageTitle: '增加新的工作人员'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/staff/staff-edit.html',
                        controller: 'StaffEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_staff',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
