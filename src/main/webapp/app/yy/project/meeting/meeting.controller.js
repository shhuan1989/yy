(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('MeetingController', MeetingController);

    MeetingController.$inject = ['$scope', '$rootScope', '$state', '$timeout', '$http', 'MeetingService', 'ProjectManagementService', 'DictionaryService'];

    function MeetingController ($scope, $rootScope, $state, $timeout, $http, MeetingService, ProjectManagementService, DictionaryService) {
        var vm = this;

        vm.meetings = [];
        vm.isLoading = false;
        vm.searchParams = {};
        vm.meetingsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.meetingsTableParams.count();
        vm.projectOptions = [];
        vm.search = search;

        loadAll();

        $scope.$watch('vm.pageSize', function () {
            vm.meetingsTableParams.count(vm.pageSize);
        });

        function loadAll() {
            vm.isLoading = true;
            refresh();
            var projectQuery = ProjectManagementService.query().$promise;
            var meetingStatusQuery = DictionaryService.meetingStatusOptions();

            $.when(projectQuery, meetingStatusQuery).then(function (projects, statusOptions) {
                var projectOptions = [];
                $.each(projects, function (i, p) {
                    projectOptions.push({
                        id: p.id,
                        name: p.name
                    });
                });
                vm.projectOptions = projectOptions;
                vm.statusOptions = statusOptions;
                search();
            }, function () {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

        function search() {
            vm.isLoading = true;
            MeetingService.query($.extend(true, {}, vm.searchParams),
                function (meetings) {
                    vm.meetings = meetings;
                    $.each(vm.meetings, function (index, meeting) {
                       if (meeting.info) {
                           meeting.digest = meeting.info.substring(0, 50);
                           if (meeting.info.length > 50) {
                               meeting.fullDesc = meeting.info;
                           }
                       }
                    });
                    updateMeetingStatus();
                    vm.meetingsTableParams.settings({
                        dataset: vm.meetings
                    });
                    vm.isLoading = false;
                },
                function () {
                    vm.isLoading = false;
                    PNotifySearchFail();
                }
            )

        }

        function updateMeetingStatus() {
            var t = moment().valueOf();
            $.each(vm.meetings, function (index, meeting) {
                if (meeting.status.id == 0) {
                    if (meeting.startTime < t) {
                        meeting.status = {
                            id: 1,
                            name: '进行中'
                        }
                    }
                }

                if (meeting.status.id < 2) {
                    if (meeting.endTime < t) {
                        meeting.status = {
                            name: '已结束',
                            id: 2
                        };
                    }
                }

                meeting.members = meeting.memberNames.join(',');
            });
        }

        function refresh() {
            updateMeetingStatus();
            $timeout(refresh, 10000);
        }

        vm.deleteMeeting = function deleteMeeting(meeting) {

            bootboxConfirm("确认删除会议 <span class='text-aqua'>" + meeting.name + "</span> ?", function () {
                MeetingService.delete({id: meeting.id},
                    function () {
                        vm.search();
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        };

        vm.exportMeetingMinutes = function (meeting) {
            waitingDialog.show('下载中...');
            $http.get('api/meetings/' + meeting.id + '/minutes', {responseType: 'arraybuffer'}).then(function (result) {
                var file = new File([result.data], meeting.name+moment(meeting.startTime).format('_YYYY年MM月DD日_')+'会议纪要.docx', {type: "application/octet-stream"});
                saveAs(file);
                waitingDialog.hide();
            }, function () {
                PNotifyDownloadFail();
                waitingDialog.hide();
            })
        };

        vm.cancelMeeting = function (meeting) {
            bootboxConfirm("确认取消会议 <span class='text-aqua'>" + meeting.name + "</span> ?", function () {
                meeting.status = {
                    id: 3
                };
                MeetingService.update(meeting,
                    function () {
                        vm.search();
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        };

        vm.initSearchDateRangePicker = function () {
            $("#field_meeting_date").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.searchParams.dateFrom = start.valueOf();
                    vm.searchParams.dateTo = end.valueOf();
                }
            );
        };

        vm.initJsComponents = function initJsComponents() {
            $('.select2').select2({
                language: "zh-CN"
            });
        };

    }
})();
