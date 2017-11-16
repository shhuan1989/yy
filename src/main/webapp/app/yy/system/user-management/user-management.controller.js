(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('UserManagementController', UserManagementController);

    UserManagementController.$inject = ['Principal', 'User', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'JhiLanguageService', 'NgTableParams', '$rootScope'];

    function UserManagementController(Principal, User, ParseLinks, AlertService, $state, pagingParams, paginationConstants, JhiLanguageService, NgTableParams, $rootScope) {
        var vm = this;

        vm.usersTableParams = $rootScope.defaultNgTableParams();
        vm.currentAccount = null;
        vm.languages = null;
        vm.setActive = setActive;
        vm.users = [];
        vm.page = 1;
        vm.totalItems = null;
        vm.clear = clear;
        vm.links = null;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.itemsPerPage = 2000;
        vm.transition = transition;
        vm.search = search;
        vm.searchParams = {};

        vm.authorities = {
            'ROLE_ADMIN': '管理员',
            'ROLE_USER': '普通用户',
            'VIEW_PROJECT_MANAGE': '查看项目管理',
            'VIEW_PROJECT_MANAGE_ALL': '查看所有人的项目',
            'EDIT_PROJECT_MANAGE': '编辑项目管理',
            'VIEW_MEETING': '查看会议',
            'EDIT_MEETING': '项目会议管理',
            'VIEW_ONGOING_PROJECT': '查看进行中的项目',
            // 'VIEW_ONGOING_PROJECT_ALL': '查看所有人进行中的项目',
            'EDIT_ONGOING_PROJECT': '编辑进行中的项目',
            'VIEW_PROJECT_RATE': '查看项目评级',
            // 'VIEW_PROJECT_RATE_ALL': '查看所有人的项目评级',
            'EDIT_PROJECT_RATE': '编辑项目评级',
            'VIEW_ARCHIVED_PROJECT': '查看已归档的项目',
            // 'VIEW_ARCHIVED_PROJECT_ALL': '查看所有已归档的项目',
            'EDIT_ARCHIVED_PROJECT': '编辑已归档的项目',
            'VIEW_CLIENT': '查看客户管理',
            'EDIT_CLIENT': '编辑客户管理',
            'VIEW_DEDUCT_SETTING': '查看项目提成',
            'EDIT_DEDUCT_SETTING': '编辑项目提成',
            'VIEW_PERFORMANCE_MANAGE': '查看绩效管理',
            'EDIT_PERFORMANCE_MANAGE': '编辑项目绩效',
            'VIEW_STAFF_MANAGE': '查看工作人员',
            'EDIT_STAFF_MANAGE': '编辑工作人员',
            'VIEW_VACATION_RECORDS': '查看请假记录',
            'EDIT_VACATION_RECORDS': '编辑请假记录',
            'VIEW_OTWORK_RECORDS': '查看加班记录',
            'EDIT_OTWORK_RECORDS': '编辑加班记录',
            'VIEW_UTIL_APPLICATION_RECORDS': '查看物品领用记录',
            'EDIT_UTIL_APPLICATION_RECORDS': '编辑物品领用记录',
            'VIEW_ASSET_MANAGE': '查看资产管理',
            'EDIT_ASSET_MANAGE': '编辑资产管理',
            'VIEW_CONTRACT_MANAGE': '查看合同管理',
            'EDIT_CONTRACT_MANAGE': '编辑合同管理',
            'VIEW_INCOME_MANAGE': '查看收款管理',
            'EDIT_INCOME_MANAGE': '编辑收款管理',
            'VIEW_INVOICE_MANAGE': '查看发票管理',
            'EDIT_INVOICE_MANAGE': '编辑发票管理',
            'VIEW_SHOOT_CONFIG': '查看拍摄配置',
            'EDIT_SHOOT_CONFIG': '编辑拍摄配置',
            'VIEW_BUDGET': '查看拍摄预算',
            'EDIT_BUDGET': '编辑拍摄预算',
            'VIEW_COST': '查看拍摄决算',
            'EDIT_COST': '编辑拍摄决算',
            'VIEW_BUDGET_VS_COST': '查看预算决算对比',
            'VIEW_SHOOT_AGENDA': '查看拍摄排期',
            'EDIT_SHOOT_AGENDA': '编辑拍摄排期',
            'VIEW_INSTRUMENTS': '查看器材库',
            'EDIT_INSTRUMENTS': '编辑器材库',
            'VIEW_ACTORS': '查看演员库',
            'EDIT_ACTORS': '编辑演员库',
            'VIEW_STAFFS': '查看工作人员库',
            'EDIT_STAFFS': '编辑工作人员库',
            'VIEW_USER_MANAGE': '查看用户管理',
            'EDIT_USER_MANAGE': '编辑用户管理'
        };

        vm.search();

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });

        function setActive (user, isActivated) {
            user.activated = isActivated;
            User.update(user, function () {
                vm.loadAll();
                vm.clear();
            });
        }

        vm.deleteUser = function (userLogin) {

            bootboxConfirm("确认删除用户 <span class='text-aqua'>" + userLogin + "</span> ？", function () {
                User.delete(
                    {
                        login: userLogin},
                    function () {
                        vm.loadAll();
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        };


        function search () {
            User.query($.extend(true,
                {
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                },
                vm.searchParams
            ), onSuccess, onError);
        }

        function onSuccess(data, headers) {
            //hide anonymous user from user management: it's a required user for Spring Security
            for (var i in data) {
                if (data[i]['login'] === 'anonymoususer') {
                    data.splice(i, 1);
                }
            }
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.users = data;

            $.each(vm.users, function (index, user) {
               var auths = [];
                $.each(user.authorities, function (ai, auth) {
                    auths.push(vm.authorities[auth]);
                });
                user.authDescs = auths;
            });

            vm.usersTableParams.settings({
                dataset: vm.users
            });
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function clear () {
            vm.user = {
                id: null, login: null, firstName: null, lastName: null, email: null,
                activated: null, langKey: null, createdBy: null, createdDate: null,
                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                resetKey: null, authorities: null
            };
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
