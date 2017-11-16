(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ShootConfigApprovalController', ShootConfigApprovalController);

    ShootConfigApprovalController.$inject = ['$scope', '$state', '$stateParams', 'WksShootConfigService', 'ProjectManagementService', 'EmployeeService', 'ApprovalService', 'UserService'];

    function ShootConfigApprovalController ($scope, $state, $stateParams, WksShootConfigService, ProjectManagementService, EmployeeService, ApprovalService, UserService) {
        var vm = this;

        vm.shootConfigId = $stateParams.id;
        vm.isLoading = true;
        vm.shootConfig = {};
        vm.project = {};
        vm.approval = {};
        vm.configItems = [];
        vm.currentUserId = null;

        vm.load = load;
        vm.reject = reject;
        vm.approve = approve;

        UserService.currentUser().then(
            function (user) {
                vm.currentUserId = user != undefined ? user.id : -1;
                load();
            }
        );

        function load() {
            if (vm.shootConfigId != undefined) {
                vm.isLoading = true;
                let projectQuery = ProjectManagementService.query().$promise;
                let shootQuery = WksShootConfigService.get({id: vm.shootConfigId});
                let empQuery = EmployeeService.query().$promise;

                $.when(projectQuery, shootQuery, empQuery).then(function (projects, configResp, employees) {
                    vm.projects = projects;
                    vm.shootConfig = configResp;
                    vm.employees = employees;

                    vm.configItems = groupShootConfig(vm.shootConfig.configItems);

                    // project
                    ProjectManagementService.get({id: vm.shootConfig.projectId}, function (project) {
                        vm.project = project;
                        vm.projectId = vm.project.id;
                    });

                    // auditors
                    let employeesById = {};
                    $.each(employees, (index, employee) => {
                        employeesById[employee.id] = employee;
                    });
                    if (vm.shootConfig && vm.shootConfig.approvalRequest) {
                        vm.auditors = approvalChainToApprovers(vm.shootConfig.approvalRequest.approvalRoot, employeesById);
                    } else {
                        vm.auditors = [];
                    }

                    // other
                    vm.approval = currentApproval(vm.shootConfig, vm.currentUserId);
                    vm.isLoading = false;
                });
            }
        }

        function approve() {
            ApprovalService.approve(vm.approval.id)
                .then(function () {
                    PNotifyApproveSuccess();
                    $state.go("^");
                }, function () {
                    PNotifyApproveFail();
                });
        }

        function reject() {
            bootboxConfirmReject(function(reason){
                ApprovalService.reject(vm.approval.id, reason)
                    .then(function () {
                        PNotifyRejectSuccess();
                        $state.go("^");
                    }, function () {
                        PNotifyApproveFail();
                    });
            });
        }
    }
})();
