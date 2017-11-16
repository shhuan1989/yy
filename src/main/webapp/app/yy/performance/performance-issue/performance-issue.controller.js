(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('PerformanceIssueController', PerformanceIssueController);

    PerformanceIssueController.$inject = ['$scope', '$rootScope', '$state', 'ProjectManagementService', 'NgTableParams', 'DictionaryService', 'PerformanceApprovalService'];

    function PerformanceIssueController ($scope, $rootScope, $state, ProjectManagementService, NgTableParams, DictionaryService, PerformanceApprovalService) {
        var vm = this;

        DictionaryService.setOptions(vm);
        vm.searchParams = {};
        vm.projectsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.projectsTableParams.count();
        vm.projects = [];
        vm.isLoading = false;

        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.projectsTableParams.count(vm.pageSize);
        });

        function search() {
            vm.isLoading = true;
            ProjectManagementService.query($.extend(true, {bonusStatusIds: [1, 2]}, vm.searchParams), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.projects = data;
                $.each(vm.projects, function (index, project) {
                    project.bonusApproval = project.bonusApprovals ==undefined ? {} : project.bonusApprovals.find(function (item) {
                        return item.active == true;
                    });

                    project.issueStatus = project.bonusIssued ? '已发放' : '未发放';

                    if (!project.bonusIssued && (project.bonusApproval.status == undefined || project.bonusApproval.status.id != 2)) {
                        project.issueStatus += ', 未审批';
                    }
                });

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

        vm.issueBonus = function (project) {

            bootboxConfirm("请再次确认项目 <span class='text-aqua'>" + project.name +"</span> 的绩效已经发放，一旦确认，不可取消或者更改！", function () {
                PerformanceApprovalService.issue(project.bonusApproval.id)
                    .then(function () {
                        project.bonusIssued = true;
                        project.issueStatus = '已发放';
                        PNotifySuccess("发放成功！");
                    }, function () {
                        PNotifySaveFail();
                    });
            });
        }
    }
})();
