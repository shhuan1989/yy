(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectAuditViewController', ProjectAuditViewController);

    ProjectAuditViewController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'ProjectManagementService', 'EmployeeService', 'ProjectRate'];

    // 项目评级详情
    function ProjectAuditViewController ($scope, $rootScope, $state, $stateParams, ProjectManagementService, EmployeeService, ProjectRate) {
        var vm = this;

        vm.projectId = $stateParams.id;
        vm.isLoading = true;
        vm.project = {};

        vm.cancel = cancel;
        vm.load = load;

        load();

        function load() {
            var projectQuery = ProjectManagementService.get({id: vm.projectId}).$promise;
            var rateQuery = ProjectRate.query({projectId: vm.projectId}).$promise;

            $.when(projectQuery, rateQuery).then(function (project, rates) {
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
                vm.project = project;
                vm.isLoading = false;
            }, function () {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

        function cancel() {
            $state.go('^');
        }
    }
})();
