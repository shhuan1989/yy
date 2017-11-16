(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('MeetingEditController', MeetingEditController);

    MeetingEditController.$inject = ['$scope', '$rootScope', '$state', 'MeetingService', 'ProjectManagementService', 'EmployeeService', 'DictionaryService'];

    function MeetingEditController ($scope, $rootScope, $state, MeetingService, ProjectManagementService, EmployeeService, DictionaryService) {
        var vm = this;

        vm.projectOptions = [];
        vm.employees = [];
        vm.employeesNotIncluded = [];
        vm.meeting = {};
        vm.members = [];
        vm.roomOptions = [];

        load();

        function load() {
            vm.isLoading = true;

            var projectQuery = ProjectManagementService.query().$promise;
            var employeeQuery = EmployeeService.query().$promise;
            var roomQuery = DictionaryService.roomOptions();

            $.when(projectQuery, employeeQuery, roomQuery).then(function (projects, employees, roomOptions) {

                var projectOptions = [];
                $.each(projects, function (i, p) {
                    projectOptions.push({
                        id: p.id,
                        name: p.name
                    });
                });
                vm.projectOptions = projectOptions;

                vm.employees = employees;
                vm.roomOptions = roomOptions;
                vm.isLoading = false;
            }, function () {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

        vm.cancel = function() {
            $state.go("^");
        };

        vm.save  = function() {
            var valid = $('.meeting-edit-content form').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (!notEmptyArray(vm.members)) {
                PNotifyError('请选择参会人员！');
                return;
            }

            if (vm.meeting.startTime > vm.meeting.endTime) {
                PNotifyTimeRangeError();
                return;
            }

            var memberIds = vm.members.map(function (m) {
                return m.id
            });

            var name = vm.projectOptions.find(function (project) {
                return project.id == vm.meeting.projectId;
            }).name;
            MeetingService.save($.extend(true, {memberIds: memberIds, name: name}, vm.meeting),
                function () {
                    PNotifySaveSuccess();
                    $state.go("^");
                },
                function () {
                    PNotifySaveFail();
                }
            )

        };

        vm.initJsComponents = function initJsComponents() {
            $('.meeting-edit-content form').parsley();

            $('#field_meeting_start_time').datetimepicker({
                sideBySide: true,
                locale: 'zh-CN',
            }).on('dp.change', function(ev){
                vm.meeting.startTime = moment(ev.date).valueOf();
            });

            $('#field_meeting_end_time').datetimepicker({
                sideBySide: true,
                locale: 'zh-CN',
            }).on('dp.change', function(ev){
                vm.meeting.endTime = moment(ev.date).valueOf();
            });

            $('.select2').select2({
                language: "zh-CN"
            });
        };

        /***************************** members *********************/
        $('#modal-add-member').on('shown.bs.modal', function (e) {

            vm.employeesNotIncluded = $.grep(vm.employees, function (item) {
                var exists = vm.members.find(function (exitem) {
                    return exitem.id == item.id;
                });
                return !exists;
            });

            $('.select2').select2({
                language: "zh-CN"
            });
        });

        vm.addNewMember = function () {
            $('#modal-add-member').modal('show');
        };

        vm.saveNewMember = function () {
            if (vm.newMembers != undefined) {
                vm.members = vm.members.concat(vm.newMembers);
                vm.newMembers = null;
            }
            $('#modal-add-member').modal('hide');
        };

        vm.deleteMember = function (member) {
            vm.members = vm.members.filter(function (item) {
                return item.id != member.id;
            });
        };
    }
})();
