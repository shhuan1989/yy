(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('UserManagementEditController',UserManagementEditController);

    UserManagementEditController.$inject = ['$state', '$stateParams', 'User', 'JhiLanguageService', 'EmployeeService', 'UserService', 'Role'];

    function UserManagementEditController ($state, $stateParams, User, JhiLanguageService, EmployeeService, UserService, Role) {
        var vm = this;
        vm.pageTitle = $state.current.data.pageTitle;


        vm.languages = null;
        vm.modifyPassword = false;
        vm.save = save;
        vm.user = {
            employee: {},
            id: null, login: null, firstName: null, lastName: null, email: null,
            activated: true, langKey: null, createdBy: null, createdDate: null,
            lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
            resetKey: null, authorities: null
        };
        vm.employees = [];

        vm.isLoading = true;

        vm.login = $stateParams.login;
        vm.authoritiesList = UserService.authList();

        vm.load = load;

        load();

        function load() {
            vm.status = "new";
            if ($state.$current.toString() == "yy_user-management.edit") {
                vm.status = "edit";
            } else if ($state.$current.toString() == "yy_user-management.view") {
                vm.status = "view";
            }

            if (vm.status != 'new') {
                var userQuery = User.get({login : $stateParams.login}).$promise;
                var roleQuery = Role.query().$promise;

                $.when(userQuery, roleQuery).then(function (user, roles) {
                    vm.user = user;
                    $.each(vm.user.authorities, function (i1, a1) {
                        $.each(vm.authoritiesList, function (ii, a) {
                            $.each(a.auths, function (i2, a2) {
                                if (a2.name == a1 || a2.name=='ROLE_USER') {
                                    a2.selected = true;
                                }
                            })
                        })
                    });

                    vm.roles = roles;

                    $.each(vm.user.roles, function (ui, urole) {
                       $.each(vm.roles, function (i, role) {
                           role.selected = role.selected || role.name == urole.name;
                       })
                    });


                    vm.isLoading = false;
                }, function () {
                    vm.isLoading = false;
                    PNotifyLoadFail();
                });
            } else {
                vm.modifyPassword = true;
                EmployeeService.query({hasLogin: false}, function (employees) {
                    vm.employees = employees;
                    if (employees == undefined || employees.length <= 0) {
                        PNotifyError('所有员工都已经有账户!');
                    }
                }, function () {
                    PNotifyLoadFail();
                });
                vm.roles = Role.query();
                vm.isLoading = false;
            }
        }

        vm.cancel = function () {
            $state.go("yy_user-management");
        };

        function onSaveSuccess (result) {
            vm.isSaving = false;
            if (vm.status == 'new') {
                PNotifySuccess('用户创建成功！');
            } else {
                PNotifySuccess('保存用户信息成功！');
            }
            $state.go("yy_user-management");
        }

        function onSaveError (resp) {
            var headers = resp.headers();

            var err = '内部错误，请重试！';
            if (headers['x-yy-error'] == 'error.userexists') {
                err = '用户名已经存在';
            } else if(headers['x-yy-error'] == 'error.emailexists') {
                err = '邮箱已经存在！';
            } else if(headers['x-yy-error'] == 'error.employeeaccountexists') {
                err = '此员工已经有登陆账号！';
            } else if(headers['x-yy-error'] == 'error.employeerequired') {
                err = '账户必须指定一个员工！';
            } else if(headers['x-yy-error'] == 'error.employeenoneexist') {
                err = '未找到指定的员工！';
            }
            PNotifyError(err);
            vm.isSaving = false;
        }

        function save () {
            var valid = $('.user-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (!vm.user.employee.id) {
                PNotifyError('请选择员工！');
                return
            }

            var auths = ['ROLE_USER'];
            $.each(vm.authoritiesList, function (ai, as) {
                $.each(as.auths, function (i, auth) {
                    if (auth.selected) {
                        auths.push(auth.name);
                    }
                });
            });
            // $.each(vm.roles, function (index, role) {
            //     if (role.selected) {
            //         auths = [...auths, ...role.auths.split(",")];
            //     }
            // });

            vm.user.authorities = _.uniq(auths);
            vm.user.roles = vm.roles.filter((role) => role.selected);

            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }

        vm.initJsComponents = function initJsComponents() {
            $('.user-edit-content form').parsley();
            $('.select2').select2({
                language: "zh-CN"
            });
        };
    }
})();
