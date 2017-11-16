(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProjectArchivedController', ProjectArchivedController);

    ProjectArchivedController.$inject = ['$scope', '$rootScope', '$state', 'ProjectManagementService', 'EmployeeService', 'DictionaryService'];

    // 已经归档的项目
    function ProjectArchivedController ($scope, $rootScope, $state, ProjectManagementService, EmployeeService, DictionaryService) {
        var vm = this;
        DictionaryService.setOptions(vm);

        vm.employees = EmployeeService.query();
        vm.memberOptions = vm.employees;

        vm.projectsTableParams = $rootScope.defaultNgTableParams();
        vm.isLoading = false;
        vm.pageSize = vm.projectsTableParams.count();
        vm.searchParams = {};


        $scope.$watch('vm.pageSize', function () {
            vm.projectsTableParams.count(vm.pageSize);
        });

        vm.search = search;
        search();

        function search() {
            vm.isLoading = true;
            ProjectManagementService.query($.extend(true,
                {
                    statusIds: [2, 3]
                },
                vm.searchParams
            ), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.projects = data;
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
    }
})();
