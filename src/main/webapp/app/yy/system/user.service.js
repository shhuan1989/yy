(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .factory('UserService', UserService);

    UserService.$inject = ['$http', '$q'];

    function UserService ($http, $q) {
        var service = {};

        service.authList = authList;
        service.authDescs = authDescs;
        service.currentUser = currentUser;


        function currentUser() {
            var deferred = $q.defer();

            $http.get('/api/employees/current').then(function (result) {
                deferred.resolve(result.data);
            }, function (err) {
                deferred.reject(err);
            });

            return deferred.promise;
        }

        function authDescs() {
            var als = authList();
            var result = {};
            $.each(als, function (i, ad) {
                $.each(ad.auths, function (j, auth) {
                    result[auth.name] = auth.desc;
                })
            });

            return result;
        }

        function authList() {
            var authoritiesList = [
                {
                    'category': '通用',
                    'auths': [
                        {
                            'name': 'ROLE_ADMIN',
                            'desc': '管理员权限'
                        },
                        {
                            'name': 'ROLE_USER',
                            'desc': '普通权限',
                            'selected': true
                        }
                    ]
                },
                {
                    'category': '工作台',
                    'auths': [
                        {
                            'name': 'WKS_MY_MEETING',
                            'desc': '会议提醒'
                        },
                        {
                            'name': 'WKS_MY_TASK',
                            'desc': '任务提醒'
                        },
                        {
                            'name': 'WKS_PROJECT_RATE',
                            'desc': '我参加的项目评级'
                        },
                        {
                            'name': 'WKS_APPROVE_PROJECT_RATE',
                            'desc': '我审核的项目评级'
                        },
                        {
                            'name': 'WKS_APPROVE_BUDGET',
                            'desc': '待审批的预算'
                        },
                        {
                            'name': 'WKS_APPROVE_SHOOT_COST',
                            'desc': '待审批的决算'
                        },
                        {
                            'name': 'WKS_APPROVE_PERFORMANCE',
                            'desc': '待审批的绩效'
                        },
                        {
                            'name': 'WKS_APPROVE_VACATION',
                            'desc': '待审批的请假'
                        },
                        {
                            'name': 'WKS_APPROVE_COST',
                            'desc': '待审批的支出'
                        },
                        {
                            'name': 'WKS_APPROVE_INCOME',
                            'desc': '待审批的收入'
                        },
                        {
                            'name': 'WKS_APPROVE_OT_WORK',
                            'desc': '待审批的加班'
                        },
                        {
                            'name': 'WKS_APPROVE_GOODS_APP',
                            'desc': '待审批的物品领用申请'
                        },
                        {
                            'name': 'WKS_EMP_PROBATION',
                            'desc': '员工转正提醒'
                        },
                        {
                            'name': 'WKS_APPROVE_CONFIG',
                            'desc': '待审批的拍摄配置'
                        },
                        {
                            'name': 'WKS_MY_VACATION',
                            'desc': '我的请假'
                        },
                        {
                            'name': 'WKS_MY_GOODS',
                            'desc': '物品领用申请'
                        },
                        {
                            'name': 'WKS_MY_PERFORMANCE',
                            'desc': '我的绩效'
                        }

                    ]
                },
                {
                    'category': '项目管理',
                    'auths':[
                        {
                            'name': 'VIEW_PROJECT_MANAGE',
                            'desc': '查看项目'
                        },
                        {
                            'name': 'VIEW_PROJECT_MANAGE_ALL',
                            'desc': '查看所有人的项目'
                        },
                        {
                            'name': 'EDIT_PROJECT_MANAGE',
                            'desc': '编辑项目'
                        },
                        {
                            'name': 'NEW_PROJECT',
                            'desc': '新建项目'
                        },
                        {
                            'name': 'PAUSE_RESTART_PROJECT',
                            'desc': '暂停/重启项目'
                        },
                        {
                            'name': 'ACCEPT_PROJECT',
                            'desc': '验收项目'
                        },
                        {
                            'name': 'CLOSE_PROJECT',
                            'desc': '关闭项目'
                        },
                        {
                            'name': 'VIEW_MEETING',
                            'desc': '查看所有的会议'
                        },
                        {
                            'name': 'VIEW_OWNED_MEETING',
                            'desc': '查看自己的会议'
                        },
                        {
                            'name': 'EDIT_MEETING',
                            'desc': '项目会议管理'
                        },
                        {
                            'name': 'VIEW_ONGOING_PROJECT',
                            'desc': '查看进行中的项目'
                        },
                        {
                            'name': 'EDIT_ONGOING_PROJECT',
                            'desc': '编辑进行中的项目'
                        },
                        {
                            'name': 'EDIT_PROJECT_MEMBER',
                            'desc': '添加/删除项目成员'
                        },
                        {
                            'name': 'EDIT_PROJECT_PROGRESS_TABLE',
                            'desc': '编辑进度表'
                        },
                        {
                            'name': 'VIEW_PROJECT_RATE',
                            'desc': '查看项目评级'
                        },
                        {
                            'name': 'CLOSE_PROJECT_RATE',
                            'desc': '关闭项目评级'
                        },
                        {
                            'name': 'VIEW_ARCHIVED_PROJECT',
                            'desc': '查看已归档的项目'
                        },
                    ]
                },
                {
                    'category': '客户管理',
                    'auths': [
                        {
                            'name': 'VIEW_CLIENT',
                            'desc': '查看客户管理'
                        },
                        {
                            'name': 'EDIT_CLIENT',
                            'desc': '编辑客户管理'
                        },
                    ]
                },
                {
                    'category': '绩效管理',
                    'auths': [
                        {
                            'name': 'VIEW_DEDUCT_SETTING',
                            'desc': '查看项目提成比例设置'
                        },
                        {
                            'name': 'EDIT_DEDUCT_SETTING',
                            'desc': '编辑项目提成比例设置'
                        },
                        {
                            'name': 'VIEW_PERFORMANCE_MANAGE',
                            'desc': '查看绩效管理'
                        },
                        {
                            'name': 'EDIT_PERFORMANCE_MANAGE',
                            'desc': '编辑项目绩效'
                        },
                        {
                            'name': 'VIEW_BONUS_ISSUE',
                            'desc': '查看绩效发放'
                        },
                        {
                            'name': 'EDIT_BONUS_ISSUE',
                            'desc': '编辑绩效发放'
                        }
                    ]
                },
                {
                    'category': '行政管理',
                    'auths': [
                        {
                            'name': 'VIEW_STAFF_MANAGE',
                            'desc': '查看员工'
                        },
                        {
                            'name': 'EDIT_STAFF_MANAGE',
                            'desc': '编辑员工'
                        },
                        {
                            'name': 'VIEW_VACATION_RECORDS',
                            'desc': '查看请假记录'
                        },
                        {
                            'name': 'EDIT_VACATION_RECORDS',
                            'desc': '编辑请假记录'
                        },
                        {
                            'name': 'VIEW_NOTICE',
                            'desc': '查看公告'
                        },
                        {
                            'name': 'EDIT_NOTICE',
                            'desc': '编辑公告'
                        },
                        {
                            'name': 'VIEW_UTIL_APPLICATION_RECORDS',
                            'desc': '查看物品领用记录'
                        },
                        {
                            'name': 'EDIT_UTIL_APPLICATION_RECORDS',
                            'desc': '编辑物品领用记录'
                        },
                        {
                            'name': 'VIEW_ASSET_MANAGE',
                            'desc': '查看资产管理'
                        },
                        {
                            'name': 'EDIT_ASSET_MANAGE',
                            'desc': '编辑资产管理'
                        },
                    ]
                },
                {
                    'category': '财务管理',
                    'auths': [
                        {
                            'name': 'VIEW_CONTRACT_MANAGE',
                            'desc': '查看合同管理'
                        },
                        {
                            'name': 'VIEW_CONTRACT_MANAGE_ALL',
                            'desc': '查看所有人的合同'
                        },
                        {
                            'name': 'EDIT_CONTRACT_MANAGE',
                            'desc': '编辑合同管理'
                        },
                        {
                            'name': 'VIEW_INCOME_MANAGE',
                            'desc': '查看收入'
                        },
                        {
                            'name': 'EDIT_INCOME_MANAGE',
                            'desc': '编辑收入'
                        },
                        {
                            'name': 'CONTRACT_MONEY_COLLECT',
                            'desc': '合同收款'
                        },
                        {
                            'name': 'VIEW_INVOICE_MANAGE',
                            'desc': '查看发票'
                        },
                        {
                            'name': 'EDIT_INVOICE_MANAGE',
                            'desc': '开具发票'
                        },
                        {
                            'name': 'VIEW_EXPENSE_MANAGE',
                            'desc': '查看支出'
                        },
                        {
                            'name': 'EDIT_EXPENSE_MANAGE',
                            'desc': '新增支出'
                        },
                    ]
                },
                {
                    'category': '拍摄管理',
                    'auths': [
                        {
                            'name': 'VIEW_SHOOT_CONFIG',
                            'desc': '查看所有的需求表'
                        },
                        {
                            'name': 'EDIT_SHOOT_CONFIG',
                            'desc': '编辑所有的需求表'
                        },
                        {
                            'name': 'VIEW_OWNED_SHOOT_CONFIG',
                            'desc': '查看自己的需求表'
                        },
                        {
                            'name': 'EDIT_OWNED_SHOOT_CONFIG',
                            'desc': '编辑自己的需求表'
                        },
                        {
                            'name': 'VIEW_BUDGET',
                            'desc': '查看所有的拍摄预算'
                        },
                        {
                            'name': 'VIEW_OWNED_SHOOT_BUDGET',
                            'desc': '查看自己的拍摄预算'
                        },
                        {
                            'name': 'EDIT_BUDGET',
                            'desc': '编辑拍摄预算'
                        },
                        {
                            'name': 'VIEW_COST',
                            'desc': '查看所有的拍摄决算'
                        },
                        {
                            'name': 'VIEW_OWNED_SHOOT_COST',
                            'desc': '查看自己的拍摄决算'
                        },
                        {
                            'name': 'EDIT_COST',
                            'desc': '编辑拍摄决算'
                        },
                        {
                            'name': 'VIEW_BUDGET_VS_COST',
                            'desc': '查看所有的预算决算对比'
                        },
                        {
                            'name': 'VIEW_OWNED_SHOOT_AUDIT',
                            'desc': '查看自己的预算决算对比'
                        },
                        {
                            'name': 'VIEW_SHOOT_AGENDA',
                            'desc': '查看所有的拍摄排期'
                        },
                        {
                            'name': 'EDIT_SHOOT_AGENDA',
                            'desc': '编辑所有的拍摄排期'
                        },
                        {
                            'name': 'VIEW_OWNED_AGENDA',
                            'desc': '查看自己的拍摄排期'
                        },
                        {
                            'name': 'EDIT_OWNED_AGENDA',
                            'desc': '编辑自己的拍摄排期'
                        },
                    ]
                },
                {
                    'category': '资源库管理',
                    'auths': [
                        {
                            'name': 'VIEW_INSTRUMENTS',
                            'desc': '查看器材库'
                        },
                        {
                            'name': 'EDIT_INSTRUMENTS',
                            'desc': '编辑器材库'
                        },
                        {
                            'name': 'VIEW_ACTORS',
                            'desc': '查看演员库'
                        },
                        {
                            'name': 'EDIT_ACTORS',
                            'desc': '编辑演员库'
                        },
                        {
                            'name': 'VIEW_STAFFS',
                            'desc': '查看工作人员库'
                        },
                        {
                            'name': 'EDIT_STAFFS',
                            'desc': '编辑工作人员库'
                        }
                    ]
                },
                {
                    'category': '系统管理',
                    'auths': [
                        {
                            'name': 'VIEW_USER_MANAGE',
                            'desc': '查看用户'
                        },
                        {
                            'name': 'EDIT_USER_MANAGE',
                            'desc': '编辑用户'
                        },
                        {
                            'name': 'VIEW_SYS_DIC',
                            'desc': '查看字典'
                        },
                        {
                            'name': 'EDIT_SYS_DIC',
                            'desc': '编辑字典'
                        },
                        {
                            'name': 'VIEW_SYS_ROLE',
                            'desc': '查看角色'
                        },
                        {
                            'name': 'EDIT_SYS_ROLE',
                            'desc': '编辑角色'
                        },
                    ]
                }
            ];

            return authoritiesList;
        }

        return service;
    }
})();
