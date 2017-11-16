(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('WksBonusController', WksBonusController);

    WksBonusController.$inject = ['$scope', '$state', '$rootScope', 'ProjectManagementService', 'UserService'];

    function WksBonusController ($scope, $state, $rootScope, ProjectManagementService, UserService) {
        var vm = this;

        vm.searchParams = {};
        vm.projectsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.projectsTableParams.count();
        vm.projects = [];
        vm.isLoading = true;

        vm.search = search;

        $scope.$watch('vm.pageSize', function () {
            vm.projectsTableParams.count(vm.pageSize);
        });

        UserService.currentUser().then(
            function (user) {
                vm.currentUserId = user != undefined ? user.id : -1;
                search();
            }
        );

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
                });

                vm.projects = vm.projects.filter((project) => {
                    let bonuses = project.bonusApproval.bonuses;
                    if (bonuses != undefined) {
                        let bonus = bonuses.find((item) => item.ownerId == vm.currentUserId);
                        if (bonus != undefined) {
                            project.myBonus = bonus;
                            return true;
                        }
                    }

                    return false;
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
    }
})();
