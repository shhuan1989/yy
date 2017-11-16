(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ShootCostAuditEditController', ShootCostAuditEditController);

    ShootCostAuditEditController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'ShootCostAuditService', 'ProjectManagementService', 'EmployeeService', 'EquipmentManagementService'];

    function ShootCostAuditEditController ($scope, $rootScope, $state, $stateParams, ShootCostAuditService, ProjectManagementService, EmployeeService, EquipmentManagementService) {
        var vm = this;

        vm.projectId = $stateParams.id;
        vm.project = {};
        vm.shootCosts = {};
        vm.configItems = [];
        vm.isLoading = true;

        vm.load = load;
        vm.cancel = cancel;

        load();

        function load() {
            let equipQuery = EquipmentManagementService.query({
                page: 0,
                size: 10000000,
                sort: ['name', 'category', 'vendor', 'id']
            }).$promise;
            let shootCostQuery = ShootCostAuditService.query({projectId: vm.projectId}).$promise;
            let projectQuery = ProjectManagementService.get({id: vm.projectId}).$promise;

            $.when(equipQuery, shootCostQuery, projectQuery).then(function (equipments, shootCosts, project) {
                vm.shootCosts = shootCosts;

                let equipmentsByName = {};
                $.each(equipments, function (index, equipment) {
                    let name = equipment.name;
                    if (equipmentsByName[name] == undefined) {
                        equipmentsByName[name] = [];
                    }
                    equipmentsByName[name].push(equipment);
                });

                $.each(vm.shootCosts, function (index, shootCost) {
                    shootCost.groupedConfigItems = groupShootBudget(shootCost.configItems, vm.equipmentsByName);

                    if (!isNaN(shootCost.budget) && !isNaN(shootCost.actualCost)) {
                        shootCost.overBudgetPercentage = (shootCost.actualCost - shootCost.budget) / shootCost.budget;
                        shootCost.diff = shootCost.actualCost - shootCost.budget;
                    }

                    let catBudget = 0;
                    let catCost = 0;
                    let catItem = null;
                    for (let i = 0; i < shootCost.groupedConfigItems.length; i++) {
                        let item = shootCost.groupedConfigItems[i];
                        if (item.cat2 != undefined) {
                            if (catItem != undefined) {
                                catItem.catBudget = catBudget;
                                catItem.catCost = catCost;
                                catItem.catOverBudgetPercentage =  (catCost - catBudget) / catBudget;
                                catItem.catDiff = catCost - catBudget;
                            }
                            catBudget = item.cost;
                            catCost = item.actualCost;
                            catItem = item;
                        } else {
                            catBudget += item.cost;
                            catCost += item.actualCost;
                        }
                    }
                    catItem.catBudget = catBudget;
                    catItem.catCost = catCost;
                    catItem.catOverBudgetPercentage =  (catCost - catBudget) / catBudget;
                    catItem.catDiff = catCost - catBudget;

                    $.each(shootCost.groupedConfigItems, function (index, item) {
                        item.diff = 0.0;
                        if (!isNaN(item.cost) && !isNaN(item.actualCost)) {
                            item.overBudgetPercentage = (item.actualCost - item.cost) / item.cost;
                            item.diff = item.actualCost - item.cost;
                        }
                    });
                });

                vm.project = project;
                vm.isLoading = false;
            }, onLoadError);

            function onLoadError() {
                PNotifyLoadFail();
                vm.isLoading = false;
            }
        }

        function cancel() {
            $state.go('^');
        }
    }
})();
