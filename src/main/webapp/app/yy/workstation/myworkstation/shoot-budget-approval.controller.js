(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ShootBudgetApprovalController', ShootBudgetApprovalController);

    ShootBudgetApprovalController.$inject = ['$scope', '$state', '$stateParams', 'WksShootBudgetService', 'ProjectManagementService', 'EmployeeService', 'ApprovalService', 'UserService', 'EquipmentManagementService'];

    function ShootBudgetApprovalController ($scope, $state, $stateParams, WksShootBudgetService, ProjectManagementService, EmployeeService, ApprovalService, UserService, EquipmentManagementService) {
        var vm = this;

        vm.budgetId = $stateParams.id;
        vm.isLoading = true;
        vm.budget = {};
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
            if (vm.budgetId != undefined) {
                vm.isLoading = true;
                let projectQuery = ProjectManagementService.query().$promise;
                let shootQuery = WksShootBudgetService.get({id: vm.budgetId});
                let empQuery = EmployeeService.query().$promise;
                let equipQuery = EquipmentManagementService.query({
                    page: 0,
                    size: 10000000,
                    sort: ['name', 'category', 'vendor', 'id']
                }).$promise;

                $.when(projectQuery, shootQuery, empQuery, equipQuery).then(function (projects, configResp, employees, equipments) {
                    vm.projects = projects;
                    vm.budget = configResp;
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

                    vm.configItems = groupShootBudget(vm.budget.configItems, vm.equipmentsByName);
                    let totalCost = 0;
                    for (let i = 0; i < vm.configItems.length; i++) {
                        let item = vm.configItems[i];
                        totalCost += item.cost;
                    }
                    vm.totalCost = totalCost;

                    // project
                    ProjectManagementService.get({id: vm.budget.projectId}, function (project) {
                        vm.project = project;
                        vm.projectId = vm.project.id;
                    });

                    // auditors
                    let employeesById = {};
                    $.each(employees, (index, employee) => {
                        employeesById[employee.id] = employee;
                    });
                    if (vm.budget && vm.budget.approvalRequest) {
                        vm.auditors = approvalChainToApprovers(vm.budget.approvalRequest.approvalRoot, employeesById);
                    } else {
                        vm.auditors = [];
                    }

                    // other
                    vm.approval = currentApproval(vm.budget, vm.currentUserId);
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
