(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('PerformanceManagementController', PerformanceManagementController);

    PerformanceManagementController.$inject = ['$scope', '$rootScope', '$state', 'ProjectManagementService', 'NgTableParams', 'DictionaryService', 'PerformanceApprovalService'];

    function PerformanceManagementController ($scope, $rootScope, $state, ProjectManagementService, NgTableParams, DictionaryService, PerformanceApprovalService) {
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
                    if (project.bonusApprovals != undefined) {
                        project.bonusApproval = project.bonusApprovals.find(function (item) {
                            return item.active == true;
                        });
                    } else {
                        project.bonusApproval = {};
                    }
                    
                    project.bonusPercentage = project.contract == undefined ? 0 : project.totalBonus / project.contract.moneyAmount;
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

        vm.submitApprovalRequest = function (project) {
            PerformanceApprovalService.start(project.bonusApproval.id)
                .then(function (result) {
                    project.bonusApproval = result.data;
                    PNotifySuccess("提交成功！");
                }, function () {
                    PNotifyError("提交失败，请稍后重试！");
                });
        }
    }
})();
