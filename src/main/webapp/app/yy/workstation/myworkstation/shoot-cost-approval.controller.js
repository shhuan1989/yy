(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ShootCostApprovalController', ShootCostApprovalController);

    ShootCostApprovalController.$inject = ['$scope', '$state', '$stateParams', 'WksShootCostService', 'ProjectManagementService', 'EmployeeService', 'ApprovalService', 'UserService', 'EquipmentManagementService'];

    function ShootCostApprovalController ($scope, $state, $stateParams, WksShootCostService, ProjectManagementService, EmployeeService, ApprovalService, UserService, EquipmentManagementService) {
        var vm = this;

        vm.shootCostId = $stateParams.id;
        vm.isLoading = true;
        vm.shootCost = {};
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
            if (vm.shootCostId != undefined) {
                vm.isLoading = true;
                let projectQuery = ProjectManagementService.query().$promise;
                let shootQuery = WksShootCostService.get({id: vm.shootCostId});
                let empQuery = EmployeeService.query().$promise;
                let equipQuery = EquipmentManagementService.query({
                    page: 0,
                    size: 10000000,
                    sort: ['name', 'category', 'vendor', 'id']
                }).$promise;

                $.when(projectQuery, shootQuery, empQuery, equipQuery).then(function (projects, configResp, employees, equipments) {
                    vm.projects = projects;
                    vm.shootCost = configResp;
                    vm.employees = employees;

                    let equipmentsByName = {};
                    $.each(equipments, function (index, equipment) {
                        let name = equipment.name;
                        if (equipmentsByName[name] == undefined) {
                            equipmentsByName[name] = [];
                        }
                        equipmentsByName[name].push(equipment);
                    });
                    vm.equipmentsByName = equipmentsByName;

                    vm.configItems = groupShootBudget(vm.shootCost.configItems, vm.equipmentsByName);
                    vm.totalCost = calCatCostReturnTotalCost(vm.configItems);

                    // project
                    ProjectManagementService.get({id: vm.shootCost.projectId}, function (project) {
                        vm.project = project;
                        vm.projectId = vm.project.id;
                    });

                    // auditors
                    let employeesById = {};
                    $.each(employees, (index, employee) => {
                        employeesById[employee.id] = employee;
                    });
                    if (vm.shootCost && vm.shootCost.approvalRequest) {
                        vm.auditors = approvalChainToApprovers(vm.shootCost.approvalRequest.approvalRoot, employeesById);
                    } else {
                        vm.auditors = [];
                    }

                    // other
                    vm.approval = currentApproval(vm.shootCost, vm.currentUserId);
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
            bootboxConfirmReject(function (reason) {
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
