(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ExpenseViewController', ExpenseViewController);

    ExpenseViewController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'ShootCostService', 'ProjectManagementService', 'EmployeeService', 'EquipmentManagementService', 'DictionaryService'];

    function ExpenseViewController ($scope, $rootScope, $state, $stateParams, ShootCostService, ProjectManagementService, EmployeeService, EquipmentManagementService, DictionaryService) {
        var vm = this;
        vm.projectId = $stateParams.id;
        vm.type = $stateParams.type;
        vm.pageTitle = $state.current.data.pageTitle;
        vm.project = {};
        vm.shootCosts = [];
        vm.configItems = [];
        vm.isLoading = true;
        vm.newAuditors = [];
        vm.employeesNotIncluded = [];
        vm.equipmentsByName = {};
        vm.employees = [];
        vm.isSaving = false;
        vm.yesnoOptions = [];
        DictionaryService.yesnoOptions().then(function (options) {
            vm.yesnoOptions = options;
        });

        vm.load = load;
        vm.cancel = cancel;

        load();

        function load() {
            function onLoadError() {
                PNotifyLoadFail();
                vm.isLoading = false;
            }

            let equipQuery = EquipmentManagementService.query({
                page: 0,
                size: 10000000,
                sort: ['name', 'category', 'vendor', 'id']
            }).$promise;
            let shootCostQuery = ShootCostService.query({projectId: vm.projectId}).$promise;
            let empQuery = EmployeeService.query().$promise;

            $.when(equipQuery, shootCostQuery, empQuery).then(function (equipments, shootCosts, employees) {
                vm.shootCosts = shootCosts;

                let equipmentsByName = {};
                $.each(equipments, function (index, equipment) {
                    let name = equipment.name;
                    if (equipmentsByName[name] == undefined) {
                        equipmentsByName[name] = [];
                    }
                    equipmentsByName[name].push(equipment);
                });
                vm.equipmentsByName = equipmentsByName;

                vm.employees = employees;
                let employeesById = {};
                $.each(employees, (index, employee) => {
                    employeesById[employee.id] = employee;
                });

                // previous costs that are approved
                $.each(vm.shootCosts, function (index, shootCost) {
                    shootCost.groupedConfigItems = groupShootBudget(shootCost.configItems, vm.equipmentsByName);

                    $.each(shootCost.groupedConfigItems, function (index, item) {
                        if (item.actualDays == undefined) {
                            item.actualDays = item.days;
                        }
                        if (item.actualAmount == undefined) {
                            item.actualAmount = item.amount;
                        }
                        if (item.actualCost == undefined) {
                            item.actualCost = item.cost;
                        }

                        item.paymentStatusVal = item.paymentStatus ? '1' : '0';
                    });
                    shootCost.actualCost = calCatCostReturnTotalCost(shootCost.groupedConfigItems);
                });

                $.each(vm.shootCosts, function (index, shootCost) {
                    if (shootCost && shootCost.approvalRequest) {
                        shootCost.auditors = approvalChainToApprovers(shootCost.approvalRequest.approvalRoot, employeesById);
                    } else {
                        shootCost.auditors = [];
                    }
                });

                ProjectManagementService.get({id: vm.projectId}, function (project) {
                    vm.project = project;
                    vm.isLoading = false;
                }, onLoadError)
            }, onLoadError);
        }

        function cancel() {
            $state.go('^');
        }
    }
})();
