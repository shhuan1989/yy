(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectManagementController', ProjectManagementController);

    ProjectManagementController.$inject = ['$scope', '$state', '$rootScope', 'ProjectManagementService', 'EmployeeService', 'DictionaryService'];

    // 项目管理
    function ProjectManagementController ($scope, $state, $rootScope, ProjectManagementService, EmployeeService, DictionaryService) {
        var vm = this;

        DictionaryService.setOptions(vm);

        vm.employees = EmployeeService.query();
        vm.memberOptions = vm.employees; 

        vm.projectsTableParams = $rootScope.defaultNgTableParams();
        vm.isLoading = false;
        vm.pageSize = vm.projectsTableParams.count();
        vm.searchParams = {};
        vm.workLink = "";
        vm.projectToArchive = null;
        vm.projects = [];


        $scope.$watch('vm.pageSize', function () {
            vm.projectsTableParams.count(vm.pageSize);
        });

        vm.search = search;
        search();

        function search() {
            console.log("Search with params", vm.searchParams);
            vm.isLoading = true;
            vm.projects = ProjectManagementService.query($.extend(true, {}, vm.searchParams), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.projectsTableParams.settings({
                    dataset: vm.projects
                });
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }

        vm.initJsComponents = function initJsComponents() {
            $('.select2').select2({
                language: "zh-CN"
            });
        };

        /****************************update project*******************************/
        vm.pauseProject = function (project) {
            bootboxConfirm("确认暂停项目 <span class='text-aqua'>" + project.name + "</span> ?", function () {
                vm.projectToArchive = $.extend(true, {}, project);
                vm.projectToArchive.status.id = 1;
                ProjectManagementService.update(
                    vm.projectToArchive,
                    function (result) {
                        PNotifySaveSuccess();
                        updateProjectStatus(result);
                    },
                    function () {
                        PNotifySaveFail();
                    }
                );
            });
        };

        vm.restartProject = function (project) {
            bootboxConfirm("确认重启项目 <span class='text-aqua'>" + project.name + "</span> ?", function () {
                vm.projectToArchive = $.extend(true, {}, project);
                vm.projectToArchive.status.id = 0;
                ProjectManagementService.update(
                    vm.projectToArchive,
                    function (result) {
                        PNotifySaveSuccess();
                        updateProjectStatus(result);
                    },
                    function () {
                        PNotifySaveFail();
                    }
                );
            });
        };

        vm.closeProject = function (project) {

            bootboxConfirm("确认关闭项目 <span class='text-aqua'>" + project.name + "</span> ?", function () {
                vm.projectToArchive = $.extend(true, {}, project);
                vm.projectToArchive.status.id = 3;
                ProjectManagementService.update(
                    vm.projectToArchive,
                    function (result) {
                        PNotifySaveSuccess();
                        updateProjectStatus(result);
                    },
                    function () {
                        PNotifySaveFail();
                    }
                );
            });
        };

        $('#modal-project-archive').on('shown.bs.modal', function (e) {
            $('#modal-project-archive form').parsley();
        });

        vm.archiveProjectConfirm = function (project) {
            vm.workLink = "";
            vm.projectToArchive = $.extend(true, {}, project);
            $('#modal-project-archive').modal("show");
        };

        vm.archiveProject = function () {
            var valid = $('#modal-project-archive form').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                return;
            }
            if (vm.projectToArchive != undefined) {
                vm.projectToArchive.workLink = vm.workLink;
                vm.projectToArchive.status.id = 2;
                vm.projectToArchive.archiveTime = moment().valueOf();

                ProjectManagementService.update(
                    vm.projectToArchive,
                    function (result) {
                        $('#modal-project-archive').modal("hide");
                        PNotifySaveSuccess();
                        updateProjectStatus(result);
                    },
                    function (resp) {
                        var headers = resp.headers();
                        if (headers['x-yy-error'] == 'error.unfinished_tasks_exits') {
                            PNotifyError("此项目有未完成的任务，不能验收");
                            return;
                        }
                        PNotifySaveFail();
                    }
                )
            }
        };

        function updateProjectStatus(project) {
            if (project == undefined) {
                return;
            }
            $.each(vm.projects, function (index, item) {
                if (item.id == project.id) {
                    item.status = project.status;
                    item.archived = project.archived;
                    item.closed = project.closed;
                }
            })
        }
    }
})();
