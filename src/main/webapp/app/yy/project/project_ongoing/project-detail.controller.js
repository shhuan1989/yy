(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProjectDetailsController', ProjectDetailsController);

    ProjectDetailsController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', '$timeout', 'ProjectManagementService', 'Task', 'EmployeeService', 'ProjectCost', 'Principal', 'User', 'DictionaryService', 'ProjectProgressTableService', 'ProjectCommonService'];

    function ProjectDetailsController ($scope, $rootScope, $state, $stateParams, $timeout, ProjectManagementService, Task, EmployeeService, ProjectCost, Principal, User, DictionaryService, ProjectProgressTableService, ProjectCommonService) {
        var vm = this;

        console.log("ProjectDetailsController initialized!");

        vm.project = null;
        $state.$current.data.projectArchived = true;

        vm.tasks = [];
        vm.projectProgressTable = {};
        vm.pageSize = 10;
        vm.allTasks = [];
        vm.costList = [];
        vm.pageIndex = 0;
        vm.previousPageIndex = 0;
        vm.pageCount = 0;
        vm.employees = EmployeeService.query();
        vm.employeesNotIncluded = [];
        vm.newMembers = null;
        vm.showOwnedTasks = false;
        vm.showCreatedTasks = false;
        vm.isLoadingMembers = false;
        vm.isLoading = false;
        vm.currentUserId = null;
        vm.editingTask = {
            eta: moment().valueOf()
        };
        vm.editingCost = {};
        vm.allMembers = [];
        vm.searchTask = searchTask;
        vm.searchCost = searchCost;
        vm.loadAll = loadAll;
        vm.costIdOfUnknownCategory =  -1;

        var p = DictionaryService.setOptions(vm);
        console.log(p);
        p.then(function () {
            var unknownCategory = vm.costOptions.find(function (item) {
                return item.name == '其他';
            });
            vm.costIdOfUnknownCategory = unknownCategory != undefined ? unknownCategory.id : -1;
        }, function () {
        });

        loadAll();

        /*************************** query ***********************************/

        function loadAll() {
            vm.isLoading = true;

            var projectId = $stateParams.id;
            var projectQuery = ProjectManagementService.get({id: projectId}).$promise;
            var progressQuery = ProjectProgressTableService.query({projectId: projectId});
            var costQuery = ProjectCost.query(
                {
                    projectId: projectId,
                    sort: ['createTime,desc']
                }).$promise;
            var taskQuery = Task.query(
                {
                    projectId: projectId,
                    sort: ['status,asc', 'createTime,desc']
                }).$promise;

            $.when(projectQuery, costQuery, taskQuery, progressQuery).then(function (project, costs, tasks, pts) {
                vm.project = project;
                $state.$current.data.projectArchived = vm.project.status != undefined && vm.project.status.id != 0;

                vm.allMembers = [];
                if (project.members) {
                    vm.allMembers = [...vm.allMembers, ...project.members];
                }
                if (project.director) {
                    vm.allMembers = [...vm.allMembers, project.director];
                }
                if (project.aes) {
                    vm.allMembers = [...vm.allMembers, ...project.aes];
                }
                onCostQuerySuccess(costs);
                onTaskQuerySuccess(tasks);

                if (pts != undefined && pts.length > 0) {
                    vm.projectProgressTable = pts[0];

                    // workaround
                    var phases = ['文案初稿', '脚本终稿', '拍摄', '后期初稿', '后期终稿'];
                    for (let i = 0; i < 5; i++) {
                        vm.projectProgressTable.items[i].name = phases[i];
                    }
                }

                vm.isLoading = false;
            }, function () {
                PNotifyLoadFail();
                vm.isLoading = false;
            });
        }

        function searchCost() {
            ProjectCost.query(
                {
                    projectId: vm.project.id,
                    sort: ['createTime,desc']
                },
                onCostQuerySuccess,
                function () {
                    PNotifyError("加载支出列表失败！");
                }
            );
        }

        function onCostQuerySuccess(result) {
            vm.costList = result;
        }

        function searchTask() {
            if (vm.isLoading) {
                return
            }
            vm.isLoading = true;
            vm.previousPageIndex = vm.pageIndex > 0 ? vm.pageIndex : 0;
            vm.pageIndex = -1;

            var q = {
                projectId: vm.project.id,
                sort: ['status,asc', 'createTime,desc']
            };
            if (vm.showOwnedTasks === true) {
                q.ownerId = vm.currentUserId;
            }
            if (vm.showCreatedTasks == true) {
                q.creatorId = vm.currentUserId;
            }
            Task.query(
                q,
                onTaskQuerySuccess,
                function () {
                    PNotifyError("加载任务列表失败！");
                    vm.isLoading = false;
                }
            );
        }

        function onTaskQuerySuccess(result) {
            vm.allTasks = result;
            $.each(vm.allTasks, function (index, item) {
                formatETA(item);
            });
            sortTasks();
            vm.pageIndex = vm.previousPageIndex;
            updateProjectStatus();
            vm.isLoading = false;
        }

        /*************************** commons ***********************************/

        function updateProjectStatus() {
            vm.project.lastUpdateTime = moment().valueOf();
            // update progress
            if (!notEmptyArray(vm.allTasks)) {
                return
            }

            var finishedTasks = $.grep(vm.allTasks, function (item, index) {
                return item.status.id == 1;
            });
            var taskCount = vm.allTasks.length;
            var finishedCount = finishedTasks != undefined ? finishedTasks.length : 0;

            vm.project.progress = 1.0 * finishedCount / taskCount;
            vm.project.progressDesc = vm.project.progress * 100 + "%";


        }

        function sortTasks() {
            if (vm.allTasks != undefined) {
                vm.allTasks.sort(function (a, b) {
                    if (a.status.id == 1 || b.status.id == 1) {
                        if (a.status.id == 1 && b.status.id == 1) {
                            return b.lastUpdateTime - a.lastUpdateTime;
                        } else if (a.status.id == 1) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }

                    if (a.status.id != b.status.id) {
                        return a.status.id - b.status.id;
                    }

                    return b.createTime - a.createTime;
                });
                updateTasksToShow();
            }
        }

        function updateTasksToShow() {
            if (vm.allTasks.length > vm.pageSize * 10) {
                vm.pageSize = Math.ceil(vm.allTasks.length / 10)
            }
            vm.pageCount = Math.ceil(vm.allTasks.length / vm.pageSize);

            if (vm.pageCount < 1) {
                vm.tasks = []
            } else if (vm.pageIndex >= 0 && vm.pageIndex < vm.pageCount) {
                vm.tasks = vm.allTasks.slice(vm.pageIndex*vm.pageSize, vm.pageIndex*vm.pageSize + vm.pageSize);
            }
        }

        /*************************** ui ***********************************/

        vm.taskLabelClass = function (task) {
            if (task != undefined && task.status != undefined) {
                if (task.status.id == 1) {
                    return 'label-success';
                } else {
                    return labelClassForTimeLeftInHour(task.timeLeftInHour);
                }
            }
            return 'label-info'
        };

        vm.initJsComponents = function () {
            $('#modal-add-member').on('shown.bs.modal', function (e) {
                $('.select2').select2({
                    language: "zh-CN"
                });
            });

            $('#modal-add-task').on('shown.bs.modal', function (e) {
                $('#modal-add-task form').parsley();
                $('#field_new_task_eta').datetimepicker({
                    format: dateFormatDMY(),
                    locale: 'zh-CN',
                    defaultDate: datetimePickerDefaultTime(vm.editingTask.eta)
                }).on('dp.change', function(ev){
                    vm.editingTask.eta = moment(ev.date).valueOf();
                });

            });

            $('#modal-add-cost').on('shown.bs.modal', function (e) {
                $('#modal-add-cost form').parsley();
            });
        };

        function formatETA(task) {
            if (task != undefined && task.eta != undefined) {
                var now = moment();
                var timeLeftInHour = moment(task.eta).diff(now, 'hours');
                task.timeLeftInHour = timeLeftInHour;
                task.timeLeft = formatHour(timeLeftInHour);
            }
            return '';
        }

        /*************************** events ***********************************/

        $scope.$watchGroup(['vm.pageIndex', 'vm.allTasks'], function () {
            updateTasksToShow();
        });

        $scope.$watchGroup(['vm.showOwnedTasks', 'vm.showCreatedTasks'], function () {
            if ((vm.showOwnedTasks === true || vm.showCreatedTasks === true) && vm.currentUserId == undefined) {
                Principal.identity(true).then(function(account) {
                    User.get(
                        {login : account.login},
                        function (result) {
                            vm.currentUserId = result.employee != undefined ? result.employee.id : -1;
                            vm.searchTask();
                        },
                        function () {
                            PNotifySearchFail();
                        }
                    );
                });
            } else {
                vm.searchTask();
            }
        });

        vm.gotoPage = function (page) {
            vm.pageIndex = page;
        };

        vm.gotoNextPage = function () {
            if (vm.pageIndex < vm.pageCount - 1) {
                vm.pageIndex += 1;
            }
        };

        vm.gotoPreviousPage = function () {
            if (vm.pageIndex > 0) {
                vm.pageIndex -= 1;
            }
        };

        /*************************** task management ***********************************/

        vm.editTask = function (task) {
            if (task != null) {
                vm.editingTask = $.extend(true, {projectId: vm.project.id}, task)
            } else {
                vm.editingTask = {
                    eta: moment().valueOf()
                };
            }
            $('#modal-add-task').modal("show");
        };

        vm.saveEditedTask = function () {
            var valid = $('#modal-add-task form').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                return
            }
            var addNew = vm.editingTask.id == undefined;
            if (addNew) {
                Task.save(
                    $.extend(true, {projectId: vm.project.id}, vm.editingTask),
                    onSaveSuccess,
                    onSaveError
                );
            } else {
                Task.update(
                    $.extend(true, {projectId: vm.project.id},vm.editingTask),
                    onSaveSuccess,
                    onSaveError
                );
            }

            function onSaveSuccess(result) {
                vm.editingTask = {
                    eta: moment().valueOf()
                };
                PNotifySaveSuccess();
                $('#modal-add-task').modal("hide");

                vm.searchTask();
            }

            function onSaveError() {
                PNotifySaveFail();
            }
        };

        vm.deleteTask = function (task) {
            if (task != undefined && task.id != undefined) {
                bootboxConfirm("确认删除任务 <span class='text-aqua'>" + task.name + "</span> ?", function () {
                    Task.delete({id: task.id},
                        function () {
                            vm.allTasks = vm.allTasks.filter(function (item) {
                                return item.id != task.id;
                            });
                            PNotifyDeleteSuccess();
                            updateProjectStatus()
                        },
                        function () {
                            PNotifyDeleteFail();
                        }
                    );
                });
            }
        };

        vm.updateTaskStatus = function (task) {
            Task.update(task,
                function (result) {
                    task.lastUpdateTime = result.lastUpdateTime;
                    updateProjectStatus();
                    sortTasks();
                },
                function () {
                    PNotifySaveFail();
                }
            );
        };

        /*************************** cost management ***********************************/

        vm.deleteCost = function (cost) {
            if (cost != undefined && cost.id != undefined) {

                bootboxConfirm("确认删除支出 <span class='text-aqua'>" + cost.category.name + ", " + cost.amount + "</span> ?", function () {
                    ProjectCost.delete({id: cost.id},
                        function () {
                            vm.searchCost();
                            PNotifyDeleteSuccess();
                        },
                        function () {
                            PNotifyDeleteFail();
                        }
                    );
                });
            }
        };

        vm.editCost = function (cost) {
            if (cost != undefined) {
                vm.editingCost = $.extend(true, {projectId: vm.project.id}, cost);
            } else {
                vm.editingCost = {};
            }
            $('#modal-add-cost').modal("show");
        };

        vm.saveEditedCost = function () {
            var valid = $('#modal-add-cost form').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                return;
            }
            var addNew = vm.editingCost.id == undefined;

            if (addNew) {
                ProjectCost.save(
                    $.extend(true, {projectId: vm.project.id}, vm.editingCost),
                    onSaveCostSuccess,
                    onSaveCostError
                );
            } else {
                ProjectCost.update(
                    $.extend(true, {projectId: vm.project.id}, vm.editingCost),
                    onSaveCostSuccess,
                    onSaveCostError
                )
            }
            function onSaveCostSuccess() {
                $('#modal-add-cost').modal("hide");
                vm.searchCost();
                vm.editingCost = {};
            }
            function onSaveCostError() {
                PNotifySaveFail();
            }
        };

        /*************************** member management ***********************************/

        vm.addNewMember = function () {
            vm.employeesNotIncluded = $.grep(vm.employees, function (item, index) {
                return vm.project.members == undefined
                    || vm.project.members.find(function (eitem) {
                        return eitem.id == item.id
                    }) == undefined;
            });
            $('#modal-add-member').modal('show');
        };

        vm.saveNewMember = function () {
            $('#modal-add-member').modal('hide');
            if (notEmptyArray(vm.newMembers)) {
                if (vm.project.members == undefined) {
                    vm.project.members = [];
                }

                $.each(vm.newMembers, function (index, item) {
                    vm.project.members.push(
                        {
                            id: item
                        }
                    )
                });

                ProjectManagementService.update(
                    vm.project,
                    function (result) {
                        vm.project = result;
                        vm.newMembers = [];
                    },
                    function () {
                        PNotifySaveFail();
                    }
                );
            }
        };

        vm.deleteMember = function (member) {
            if (member != undefined && member.id != undefined) {

                ProjectCommonService.memberHasTask(vm.project.id, member.id).then(function (resp) {
                    if (resp.data) {
                        PNotifyError('该成员有未完成的任务，不可以删除！');
                    } else {
                        bootboxConfirm("确认从项目中移除成员 <span class='text-aqua'>" + member.name + "</span> ?", function () {
                            var newProject = $.extend(true, {}, vm.project);
                            newProject.members = $.grep(newProject.members, function (item) {
                                return item.id != member.id;
                            });

                            ProjectManagementService.update(
                                newProject,
                                function (savedProject) {
                                    vm.project = savedProject;
                                    PNotifySaveSuccess();
                                },
                                function () {
                                    PNotifySaveFail();
                                }
                            );
                        });
                    }
                }, function () {
                    PNotifyInternalError();
                })
            }
        };
    }
})();
