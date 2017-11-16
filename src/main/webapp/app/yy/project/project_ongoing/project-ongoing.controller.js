(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectOngoingController', ProjectOngoingController);

    ProjectOngoingController.$inject = ['$scope', '$rootScope', '$state', 'AlertService', 'Task', 'ProjectManagementService', 'EmployeeService', 'DictionaryService'];

    // 进行中的项目
    function ProjectOngoingController ($scope, $rootScope, $state, AlertService, Task, ProjectManagementService, EmployeeService, DictionaryService) {
        var vm = this;
        console.log("ProjectOngoingController Initialized");
        vm.projects = [];
        vm.searchParams = {};

        DictionaryService.setOptions(vm);
        vm.employees = EmployeeService.query();
        vm.memberOptions = vm.employees;
        vm.search = search;

        vm.search();

        function search() {
            vm.isLoading = true;
            ProjectManagementService.query($.extend(true,
                {
                    statusIds: [0]
                },
                vm.searchParams
            ), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.projects = data;
                $.each(vm.projects, function (index, item) {
                   if (item.progress != null) {
                       item.progressDesc = item.progress * 100 + "%";
                   }

                   if (item.progressStatus != undefined) {
                       var phases = ['文案初稿', '脚本终稿', '拍摄', '后期初稿', '后期终稿'];
                       item.progressStatusName = phases[item.progressStatus];
                   } else {
                       item.progressStatusName = '文案初稿';
                   }
                });
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }

        vm.viewDetail = function (project) {
            $state.go("yy_project_ongoing.view",{id: project.id});
        };

        vm.initJsComponents = function initJsComponents() {
            $('.select2').select2({
                language: "zh-CN"
            });
        }
    }
})();
