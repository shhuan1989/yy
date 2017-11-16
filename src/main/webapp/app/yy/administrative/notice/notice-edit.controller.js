(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('AdminNoticeEditController', AdminNoticeEditController);

    AdminNoticeEditController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'AdminNoticeService', 'Dept', 'ProjectManagementService', 'EmployeeService'];

    function AdminNoticeEditController ($timeout, $scope, $stateParams, $state, AdminNoticeService, Dept, ProjectManagementService, EmployeeService) {
        let vm = this;
        vm.notice = {};
        vm.isLoading = false;
        vm.noticeId = $stateParams.id;
        vm.pageTitle = $state.current.data.pageTitle;
        vm.disableEdit = $state.$current.toString() === 'yy_admin_notice.view';

        vm.deptIds = [];
        vm.projectIds = [];
        vm.employeeIds = [];

        vm.cancel = cancel;
        vm.load = load;
        vm.save = save;

        vm.load();

        function load() {
            vm.isLoading = true;

            var empQuery = EmployeeService.query().$promise;
            var deptQuery = Dept.query().$promise;
            var projectQuery = ProjectManagementService.query().$promise;

            if (vm.noticeId != undefined) {
                var noticeQuery = AdminNoticeService.get({id: vm.noticeId}).$promise;

                $.when(empQuery, deptQuery, projectQuery, noticeQuery).then(function (employees, depts, projects, notice) {
                    vm.employees = employees;
                    vm.depts = [{id: -1, name: '全公司'}, ...depts];
                    vm.projects = projects;
                    vm.notice = notice;

                    vm.deptIds = notice.depts.map((dept) => dept.id);
                    vm.projectIds = notice.projects.map((project) => project.id);
                    vm.employeeIds = notice.employees.map((employee) => employee.id);

                    initDateTimePicker();
                    vm.isLoading = false;
                }, function () {
                    vm.isLoading = false;
                    PNotifyLoadFail();
                });

            } else {
                $.when(empQuery, deptQuery, projectQuery).then(function (employees, depts, projects) {
                    vm.employees = employees;
                    vm.depts = [{id: -1, name: '全公司'}, ...depts];
                    vm.projects = projects;
                    initDateTimePicker();
                    vm.isLoading = false;
                }, function () {
                    vm.isLoading = false;
                    PNotifyLoadFail();
                })
            }
        }

        function initDateTimePicker() {
            $('#field_start_time').datetimepicker({
                sideBySide: true,
                format: dateFormatDmyHms(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.notice.startTime)
            }).on('dp.change', function(ev){
                vm.notice.startTime = moment(ev.date).valueOf();
            });
            $('#field_end_time').datetimepicker({
                sideBySide: true,
                format: dateFormatDmyHms(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.notice.expireTime)
            }).on('dp.change', function(ev){
                vm.notice.expireTime = moment(ev.date).valueOf();
            });
        }

        function save() {
            var valid = $('.notice-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (vm.deptIds != undefined && vm.deptIds.length > 0) {
                vm.notice.depts =
                    vm.deptIds.map(function (id) {
                        return {
                            id: id
                        }
                    })
            }

            if (vm.projectIds != undefined && vm.projectIds.length > 0) {
                vm.notice.projects =
                    vm.projectIds.map(function (id) {
                        return {
                            id: id
                        }
                    })
            }

            if (vm.employeeIds != undefined && vm.employeeIds.length > 0) {
                vm.notice.employees =
                    vm.employeeIds.map(function (id) {
                        return {
                            id: id
                        }
                    })
            }

            if (vm.noticeId == undefined) {
                AdminNoticeService.save(vm.notice, onSaveSuccess, onSaveError);
            } else {
                AdminNoticeService.update(vm.notice, onSaveSuccess, onSaveError);
            }

        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        function cancel() {
            $state.go("^");
        }

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });
            $('.notice-edit-content form').parsley();
        };
    }
})();
