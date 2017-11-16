(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ShootAgendaController', ShootAgendaController);

    ShootAgendaController.$inject = ['$scope', '$state', 'ProjectManagementService', 'ShootAgendaService'];

    function ShootAgendaController ($scope, $state, ProjectManagementService, ShootAgendaService) {
        var vm = this;

        vm.project = {};
        vm.newEvent = {};
        vm.currColor = "#3c8dbc"; //Red by default
        vm.projects = [];
        vm.recentEvents = [];
        vm.editProject = {};
        vm.showingEvent = {};
        vm.isLoading = true;

        vm.load = load;

        vm.load();

        function load() {
            vm.isLoading = true;
            var projectQuery = ProjectManagementService.query().$promise;
            var agendaQuery = ShootAgendaService.query({}).$promise;

            $.when(projectQuery, agendaQuery).then(function (projects, events) {
                vm.projects = projects;

                vm.events = events;

                let ct = moment().valueOf();
                let rts = [];
                $.each(vm.events, function (index, event) {
                    if (event.startTime > ct) {
                        rts.push(event);
                    }
                });
                rts.sort(function (a, b) {
                    return a.startTime - b.startTime;
                });
                vm.recentEvents = rts.slice(0, 3);

                $.each(vm.events, function (index, event) {
                    event.start = moment(event.startTime).toDate();
                    event.end = moment(event.endTime).toDate();

                    if (event.tooltip == undefined) {
                        event.tooltip = '主题：' + event.title + '<br>'
                            + '地点：' + (event.location || '') + '<br>'
                            + '开始时间：' + formatTimestampDmyHms(event.startTime) + '<br>'
                            + '结束时间：' + formatTimestampDmyHms(event.endTime);
                    }
                });

                initCalendar();
                vm.isLoading = false;
            }, function () {
                PNotifyLoadFail();
                vm.isLoading = false;
            });
        }

        vm.addNewEvent = function () {
            let validate = $('.shoot-agenda-content form').parsley().validate();
            if (!validate) {
                return;
            }

            if (vm.newEvent.startTime > vm.newEvent.endTime) {
                PNotifyTimeRangeError();
                return;
            }

            let newEvent = $.extend(true, {}, vm.newEvent);
            tooltipEvent(newEvent, vm.project);
            newEvent.projectId = vm.project.id;

            ShootAgendaService.save(newEvent, function () {
                addEvent($.extend(true, {
                    start: moment(newEvent.startTime).toDate(),
                    end: moment(newEvent.endTime).toDate()
                }, newEvent));
                PNotifySaveSuccess();
                vm.newEvent = {};
                // initCalendar();
            }, function () {
                PNotifySaveFail();
            })
        };

        function tooltipEvent(event, project) {
            event.tooltip = '主题：' + event.title + '<br>'
                + '项目：' + (project == undefined ? '' : project.name) + '<br>'
                + '地点：' + (event.location || '') + '<br>'
                + '开始时间：' + formatTimestampDmyHms(event.startTime) + '<br>'
                + '结束时间：' + formatTimestampDmyHms(event.endTime);
        }

        function addEvent(event) {
            $('#calendar').fullCalendar('renderEvent', event, true);
        }

        function removeEvent(event) {
            $('#calendar').fullCalendar('removeEvents',event.id);
        }

        function initCalendar() {
            /* initialize the calendar */
            $('#calendar').fullCalendar({
                locale: 'zh-cn',
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                buttonText: {
                    today: '今天',
                    month: '月',
                    week: '周',
                    day: '日'
                },
                events: vm.events,
                eventRender: function(event, element) {
                    element.qtip({
                        content: event.tooltip,
                        position: {
                            my: 'bottom left',
                            at: 'top left'
                        }
                    });
                },
                eventClick: function(event) {
                    $scope.$apply(function () {
                        vm.updatingEvent = event;
                        vm.showingEvent = {
                            start: event.start,
                            end: event.end,
                            startTime: event.startTime,
                            endTime: event.endTime,
                            allDay: event.allDay,
                            backgroundColor: event.backgroundColor,
                            borderColor: event.borderColor,
                            creatorId: event.creatorId,
                            id: event.id,
                            location: event.location,
                            projectId: event.projectId,
                            title: event.title,
                            tooltip: event.tooltip,
                            url: event.url
                        };
                        vm.editProject = vm.projects.find(function (p) {
                            return p.id == event.projectId;
                        });

                        $('#edit_field_start_time').data("DateTimePicker").date(moment(event.startTime));
                        $('#edit_field_end_time').data("DateTimePicker").date(moment(event.endTime));
                        $('#modal-event-detail').show();
                    });
                },
                editable: false,
                droppable: false, // this allows things to be dropped onto the calendar !!!
            });
        }

        vm.closeEventEditModal = function () {
            $('#modal-event-detail').hide();
            vm.showingEvent = {};
            vm.editProject = {};
        };

        vm.saveEvent = function () {
            ShootAgendaService.update(vm.showingEvent, function () {

                removeEvent(vm.updatingEvent);
                var newEvent = {
                    startTime: vm.showingEvent.startTime,
                    endTime: vm.showingEvent.endTime,
                    allDay: vm.showingEvent.allDay,
                    backgroundColor: vm.showingEvent.backgroundColor,
                    borderColor: vm.showingEvent.borderColor,
                    creatorId: vm.showingEvent.creatorId,
                    location: vm.showingEvent.location,
                    projectId: vm.showingEvent.projectId,
                    title: vm.showingEvent.title,
                    tooltip: vm.showingEvent.tooltip,
                    url: vm.showingEvent.url
                };
                newEvent.start = moment(vm.showingEvent.startTime).toDate();
                newEvent.end = moment(vm.showingEvent.endTime).toDate();
                tooltipEvent(newEvent, vm.editProject);
                addEvent(newEvent);
                vm.showingEvent = {};

                PNotifySaveSuccess();
                vm.closeEventEditModal();
            }, function (resp) {
                var headers = resp.headers();
                if (headers['x-yy-error'] == 'error.unauthorized') {
                    PNotifyError("没有权限修改此记录");
                    return;
                }
                PNotifySaveFail();
            });
        };

        vm.deleteEvent = function () {
            ShootAgendaService.delete({id: vm.showingEvent.id}, function () {
                PNotifySaveSuccess();
                removeEvent(vm.showingEvent);
                vm.closeEventEditModal();
            }, function (resp) {
                var headers = resp.headers();
                if (headers['x-yy-error'] == 'error.unauthorized') {
                    PNotifyError("没有权限删除此记录");
                    return;
                }
                PNotifySaveFail();
            });
        };

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });

            $('#field_start_time').datetimepicker({
                sideBySide: true,
                locale: 'zh-CN'
            }).on('dp.change', function(ev){
                vm.newEvent.startTime = moment(ev.date).valueOf();
            });

            $('#field_end_time').datetimepicker({
                sideBySide: true,
                locale: 'zh-CN'
            }).on('dp.change', function(ev){
                vm.newEvent.endTime = moment(ev.date).valueOf();
            });

            $('#edit_field_start_time').datetimepicker({
                sideBySide: true,
                locale: 'zh-CN',
            }).on('dp.change', function (ev) {
                vm.showingEvent.startTime = moment(ev.date).valueOf();
            });

            $('#edit_field_end_time').datetimepicker({
                sideBySide: true,
                locale: 'zh-CN',
            }).on('dp.change', function (ev) {
                vm.showingEvent.endTime = moment(ev.date).valueOf();
            });

            $('.shoot-agenda-content form').parsley();

            //Color chooser button
            var colorChooser = $("#color-chooser-btn");
            $("#color-chooser > li > a").click(function (e) {
                e.preventDefault();
                //Save color
                vm.currColor = rgb2hex($(this).css("color"));
                //Add color effect to button
                $('#add-new-event').css({"background-color": vm.currColor, "border-color": vm.currColor});
                vm.newEvent.backgroundColor = vm.currColor;
                vm.newEvent.borderColor = vm.currColor;
            });
        };
    }
})();
