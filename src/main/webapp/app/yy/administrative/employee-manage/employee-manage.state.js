(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_admin_emp', {
                parent: 'yy_admin',
                url: '/employee_management',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_STAFF_MANAGE','EDIT_STAFF_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/employee-manage/employee-manage.html',
                        controller: 'EmployeeManagementController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_admin_emp',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_admin_emp.view', {
                parent: 'yy_admin_emp',
                url: '/employee_management/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_STAFF_MANAGE','EDIT_STAFF_MANAGE'],
                    pageTitle: '查看员工信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/employee-manage/employee-edit.html',
                        controller: 'EmployeeEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return true;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_admin_emp',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_admin_emp.new', {
                parent: 'yy_admin_emp',
                url: '/employee_management/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_STAFF_MANAGE'],
                    pageTitle: '增加新的员工'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/employee-manage/employee-edit.html',
                        controller: 'EmployeeEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'employee',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_admin_emp.edit', {
                parent: 'yy_admin_emp',
                url: '/employee_management/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_STAFF_MANAGE'],
                    pageTitle: '编辑员工信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/employee-manage/employee-edit.html',
                        controller: 'EmployeeEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_admin_emp',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
