(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_workstation_mystation', {
                parent: 'yy_workstation',
                url: '/workstation',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/workstation.html',
                        controller: 'WorkstationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('project_rate', {
                parent: 'yy_workstation_mystation',
                url: '/project_rate/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/project-rate.html',
                        controller: 'ProjectRateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('perf_approval', {
                parent: 'yy_workstation_mystation',
                url: '/performance_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/performance-approval.html',
                        controller: 'PerformanceAprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('vacation_approval', {
                parent: 'yy_workstation_mystation',
                url: '/vacation_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/vacation-approval.html',
                        controller: 'VacationAprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('work_approval', {
                parent: 'yy_workstation_mystation',
                url: '/work_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/work-approval.html',
                        controller: 'WorkAprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('util_approval', {
                parent: 'yy_workstation_mystation',
                url: '/util_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/util-approval.html',
                        controller: 'UtilAprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('shoot_config_approval', {
                parent: 'yy_workstation_mystation',
                url: '/shoot_config_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/shoot-config-approval.html',
                        controller: 'ShootConfigApprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('budget_approval', {
                parent: 'yy_workstation_mystation',
                url: '/budget_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/shoot-budget-approval.html',
                        controller: 'ShootBudgetApprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('shootcost_approval', {
                parent: 'yy_workstation_mystation',
                url: '/shoot_cost_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/shoot-cost-approval.html',
                        controller: 'ShootCostApprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('expense_approval', {
                parent: 'yy_workstation_mystation',
                url: '/expense_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/expense-approval.html',
                        controller: 'ExpenseApprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('income_approval', {
                parent: 'yy_workstation_mystation',
                url: '/income_approval/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/income-approval.html',
                        controller: 'IncomeApprovalController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                }
            })
            .state('notice_more', {
                parent: 'yy_workstation_mystation',
                url: '/notices',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/notice-list.html',
                        controller: 'WksNoticeController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                }
            })
            .state('notice_more.view', {
                parent: 'notice_more',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_NOTICE','EDIT_NOTICE'],
                    pageTitle: '查看公告'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/notice-detail.html',
                        controller: 'AdminNoticeViewController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_task_more', {
                parent: 'yy_workstation_mystation',
                url: '/tasks',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/myworkstation/task-list.html',
                        controller: 'WksTaskController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                }
            });
    }
})();
