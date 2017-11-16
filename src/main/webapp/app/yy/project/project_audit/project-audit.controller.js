(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectAuditController', ProjectAuditController);

    ProjectAuditController.$inject = ['$scope', '$rootScope', '$state', 'ProjectManagementService', 'EmployeeService', 'ProjectRate', 'DictionaryService', 'ProjectAuditService'];

    // 项目评级
    function ProjectAuditController ($scope, $rootScope, $state, ProjectManagementService, EmployeeService, ProjectRate, DictionaryService, ProjectAuditService) {
        var vm = this;

        DictionaryService.setOptions(vm);

        vm.employees = EmployeeService.query();
        vm.memberOptions = vm.employees;

        vm.projectsTableParams = $rootScope.defaultNgTableParams();
        vm.isLoading = false;
        vm.pageSize = vm.projectsTableParams.count();
        vm.searchParams = {};
        vm.shownProject = {};


        $scope.$watch('vm.pageSize', function () {
            vm.projectsTableParams.count(vm.pageSize);
        });

        vm.search = search;
        search();

        function search() {
            vm.isLoading = true;

            ProjectManagementService.query($.extend(true,
                {
                    statusId: 2
                },
                vm.searchParams
            ), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.projects = data;

                if (notEmptyArray(vm.projects)) {
                    $.each(vm.projects, function (index, project) {
                        ProjectRate.query(
                            {projectId: project.id},
                            function (rates) {
                                if (notEmptyArray(rates)) {
                                    project.avgRate = rates.find(function (rate) {
                                        return rate.avg == true;
                                    });

                                    var userRates = {};
                                    $.each(rates, function (index, rate) {
                                        userRates[rate.ownerId] = rate;
                                    });

                                    $.each(project.firstLevelRateMembers, function (index, member) {
                                        member.rate = userRates[member.id];
                                    });

                                    $.each(project.secondLevelRateMembers, function (index, member) {
                                        member.rate = userRates[member.id];
                                    });
                                }
                                vm.isLoading = false;
                            },
                            function () {
                                PNotifySearchFail();
                                vm.isLoading = false;
                            }
                        )
                    });
                } else {
                    vm.isLoading = false;
                }
                vm.projectsTableParams.settings({
                    dataset: vm.projects
                });
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }

        vm.viewRateDetail = function (project) {
            vm.shownProject = project;
            $("#modal-rate-status").modal("show");


        };

        vm.closeProjectRate = function (project) {
            if (project != undefined && project.id != undefined) {
                bootboxConfirm("确定关闭项目评级 <span class='text-aqua'>" + project.name + "</span> ?", function () {
                    ProjectAuditService.closeProjectRates(project.id).then(function () {
                        PNotifySaveSuccess();
                        project.rateClosed = true;
                    }, function () {
                        PNotifySaveFail();
                    });
                });
            }
        };

        vm.initJsComponents = function initJsComponents() {
            $('.select2').select2({
                language: "zh-CN"
            });
        };

    }
})();
