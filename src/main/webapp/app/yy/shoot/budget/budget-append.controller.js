(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ShootBudgetAppendController', ShootBudgetAppendController);

    ShootBudgetAppendController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'ShootBudgetService', 'ProjectManagementService', 'EquipmentManagementService', 'StaffManageService', 'ActorRepoService', 'EmployeeService'];

    function ShootBudgetAppendController ($scope, $rootScope, $state, $stateParams, ShootBudgetService, ProjectManagementService, EquipmentManagementService, StaffManageService, ActorRepoService, EmployeeService) {
        var vm = this;

        vm.projectId = $stateParams.id;
        vm.pageTitle = $state.current.data.pageTitle;
        vm.project = {};
        vm.budget = {};
        vm.budgets = [];
        vm.configItems = [];
        vm.isLoading = true;
        vm.auditors = [];
        vm.actors = [];
        vm.newAuditors = [];
        vm.employeesNotIncluded = [];
        vm.equipmentsByName = {};
        vm.equipments = {};
        vm.equipmentCategory = {};
        vm.vendorOptions = [];
        vm.employees = [];
        vm.cat2Options = [];
        vm.nameOptions = [];
        vm.cat1Options = ['器材类', '工作人员', '演员', '服化道', '吃住行', '其他'];
        vm.cat2OptionCaches = {};
        vm.newConfig = {};
        vm.isSaving = false;
        vm.status = "view";

        vm.load = load;
        vm.cancel = cancel;
        vm.save = save;

        load();


        function load() {
            if ($state.$current.toString() == "yy_shoot_budget.view") {
                vm.status = "view";
            } else if ($state.$current.toString() == "yy_shoot_budget.edit") {
                vm.status = "edit";
            } else if ($state.$current.toString() == "yy_shoot_budget.append") {
                vm.status = "append";
            }

            ActorRepoService.query({
                page: 0,
                size: 2000
            }, function (data) {
                vm.actors = data.content;
            });

            let equipQuery = EquipmentManagementService.query({
                page: 0,
                size: 10000000,
                sort: ['name', 'category', 'vendor', 'id']
            }).$promise;
            let budgetQuery = ShootBudgetService.query({projectId: vm.projectId}).$promise;
            let empQuery = EmployeeService.query().$promise;
            let projectQuery = ProjectManagementService.get({id: vm.projectId}).$promise;

            $.when(equipQuery, budgetQuery, empQuery, projectQuery).then(function (equipments, budgets, employees, project) {
                vm.budgets = budgets;
                vm.project = project;

                if (vm.status != "append") {
                    vm.budget = vm.budgets[vm.budgets.length-1];
                    vm.budgets.pop();
                }

                vm.employees = employees;
                let employeesById = {};
                $.each(employees, (index, employee) => {
                    employeesById[employee.id] = employee;
                });
                if (vm.budget && vm.budget.approvalRequest) {
                    vm.auditors = approvalChainToApprovers(vm.budget.approvalRequest.approvalRoot, employeesById);
                } else {
                    vm.auditors = [];
                }
                $.each(vm.budgets, function (index, budget) {
                   if (budget && budget.approvalRequest) {
                       budget.auditors = approvalChainToApprovers(budget.approvalRequest.approvalRoot, employeesById);
                   } else {
                       budget.auditors = [];
                   }
                });

                let equipmentsByName = {};
                $.each(equipments, function (index, equipment) {
                    let name = equipment.name;
                    if (equipmentsByName[name] == undefined) {
                        equipmentsByName[name] = [];
                    }
                    equipmentsByName[name].push(equipment);
                });
                vm.equipmentsByName = equipmentsByName;

                $.each(vm.budgets, function (index, budget) {
                    budget.groupedConfigItems =  groupShootBudget(budget.configItems, vm.equipmentsByName);

                    if (budget.budget == undefined) {
                        budget.budget = sum(budget.groupedConfigItems.map((c) => c.cost || 0));
                    }

                    if (vm.project != undefined
                        && vm.project.contract != undefined
                        && vm.project.contract.moneyAmount != undefined) {
                        budget.percent = budget.budget / vm.project.contract.moneyAmount;
                    }
                });

                vm.configItems = groupShootBudget(vm.budget.configItems, vm.equipmentsByName);
                vm.isLoading = false;
            }, onLoadError);

            function onLoadError() {
                PNotifyLoadFail();
                vm.isLoading = false;
            }
        }

        $scope.$watch('vm.configItems', function () {
            if (vm.configItems == undefined) {
                return;
            }

            $.each(vm.configItems, function (index, item) {
                let equipments = vm.equipmentsByName[item.name];
                if (equipments && equipments.length > 0) {
                    if (item.vendor) {
                        let equipment = equipments.find((e) => e.vendor == item.vendor);
                        if (equipment) {
                            item.unitPrice = equipment.price;
                        }
                    } else {
                        item.vendor = equipments[0].vendor;
                        item.unitPrice = equipments[0].price;
                    }
                }

                item.cost = 1;
                if (!isNaN(item.cost)) {
                    item.cost *= item.unitPrice;
                }

                if (!isNaN(item.amount)) {
                    item.cost *= item.amount;
                }

                if (!isNaN(item.days)) {
                    item.cost *= item.days;
                }

                if (item.cost == 1) {
                    item.cost = '';
                }
            });

            let totalCost = 0;
            for (let i = 0; i < vm.configItems.length; i++) {
                let item = vm.configItems[i];
                totalCost += item.cost;
                if (item.c2rowspan != undefined) {
                    let catCost = item.cost;
                    for (let j = i+1; j < vm.configItems.length; j++) {
                        if (vm.configItems[j].c2rowspan != undefined) {
                            break;
                        }
                        catCost += vm.configItems[j].cost;
                    }
                    item.catCost = catCost;
                }
            }
            vm.budget.budget = totalCost;
            if (vm.project != undefined && vm.project.contract != undefined && !isNaN(vm.project.contract.moneyAmount)) {
                vm.budget.percent = totalCost / vm.project.contract.moneyAmount;
            }
        }, true);

        function cancel() {
            $state.go('^');
        }

        function save() {
            if (vm.isSaving) {
                return;
            }
            vm.isSaving = true;
            let validate = $('#new-config-table').parsley().validate();
            if (!validate) {
                PNotifyInvalidInput();
                vm.isSaving = false;
                return;
            }

            if (vm.auditors == undefined || vm.auditors.length <= 0) {
                PNotifyErrorNeedApprover();
                vm.isSaving = false;
                return
            }

            vm.budget.projectId = vm.project.id;
            if (vm.budget.startTime == undefined) {
                vm.budget.startTime = moment().valueOf();
            }
            if (vm.budget.endTime == undefined) {
                vm.budget.endTime = moment('9999-01-01', 'YYYY-MM-DD').valueOf();
            }
            vm.budget.type = 1;

            // let cat1 = '';
            // let cat2 = '';
            // let configItems = [];
            // for (let i = 0; i < vm.configItems.length; i++) {
            //     let item = vm.configItems[i];
            //     if (item.cat1 != undefined && item.cat1 != '') {
            //         cat1 = item.cat1;
            //     }
            //     if (item.cat2 != undefined && item.cat2 != '') {
            //         cat2 = item.cat2;
            //     }
            //     configItems.push({
            //         name: item.name,
            //         id: item.id,
            //         cat1: cat1,
            //         cat2: cat2,
            //         amount: item.amount,
            //         days: item.days,
            //         unitPrice: item.unitPrice,
            //         cost: item.cost,
            //         defaultUnitPrice: item.defaultUnitPrice,
            //         vendor: item.vendor,
            //     })
            // }
            // vm.budget.configItems = configItems;

            let approvalRequest = {
                name: vm.project.name
            };
            approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
            vm.budget.approvalRequest = approvalRequest;

            if (vm.budget.id != undefined) {
                ShootBudgetService.update(vm.budget, onSaveSuccess, onSaveError);
            } else {
                ShootBudgetService.save(vm.budget, onSaveSuccess, onSaveError)
            }

            function onSaveSuccess() {
                vm.isSaving = false;
                PNotifySaveSuccess();
                $state.go('^');
            }

            function onSaveError() {
                vm.isSaving = false;
                PNotifySaveFail();
            }
        }

        vm.initJsComponents = function () {
            $('.budget-edit-content table').parsley();
        };

        /****************************************** category dropdowns ****************************/

        $scope.$watch('vm.newConfig.cat1', function () {
            let index = vm.cat1Options.findIndex((item) => item == vm.newConfig.cat1);
            getCat2Options(index);
            vm.newConfig.cat2 = '';
        });

        function getCat2Options(index) {
            vm.nameOptions = [];
            if (index < 0 || index > 3) {
                vm.cat2Options = [];
                return;
            }

            let cachedOptions = vm.cat2OptionCaches[index];
            if (cachedOptions != undefined) {
                vm.cat2Options = cachedOptions;
                return;
            }

            if (index == 0) {
                EquipmentManagementService.query(
                    {
                        page: 0,
                        size: 2000,
                        sort: ['id']
                    }, function (equipments) {
                    vm.equipments = equipments;

                    vm.equipmentCategory = {};
                    let equipmentsByname = {};
                    $.each(equipments, function (index, equipment) {
                        equipmentsByname[equipment.name] = equipment;
                    });

                    $.each(Object.values(equipmentsByname), function (index, equiment) {
                        if (vm.equipmentCategory[equiment.categoryName] == undefined) {
                            vm.equipmentCategory[equiment.categoryName] = [];
                        }
                        vm.equipmentCategory[equiment.categoryName].push(equiment);
                    });

                    let options = Object.keys(vm.equipmentCategory);
                    vm.cat2OptionCaches[index] = options;
                    vm.cat2Options = options;
                })
            } else if (index == 1) {
                StaffManageService.query(
                    {
                        page: 0,
                        size: 2000,
                        sort: ['id']
                    }, function (staffs) {
                    vm.staffs = [];
                    $.each(staffs, function (index, staff) {
                        if (vm.staffs[staff.typeName] == undefined) {
                            vm.staffs[staff.typeName] = [];
                        }
                        vm.staffs[staff.typeName].push(staff);
                    });

                    let options = Object.keys(vm.staffs);
                    vm.cat2OptionCaches[index] = options;
                    vm.cat2Options = options;
                })
            } else if (index == 2) {
                vm.cat2Options = [];
                vm.nameOptions = vm.actors;
            } else {
                vm.cat2Options = [];
            }
        }

        $scope.$watch('vm.newConfig.cat2', function () {
            let c1index = vm.cat1Options.findIndex((item) => item == vm.newConfig.cat1);
            if (c1index >= 0 && c1index <= 3) {
                if (c1index == 0) {
                    vm.nameOptions = vm.equipmentCategory[vm.newConfig.cat2];
                } else if (c1index == 1) {
                    vm.nameOptions = vm.staffs[vm.newConfig.cat2];
                } else if (c1index == 2) {
                    vm.nameOptions = vm.actors;
                } else {
                    vm.nameOptions = [];
                }
            } else {
                vm.nameOptions = [];
            }
            vm.newConfig.name = '';
        });

        $scope.$watch('vm.newConfig.name', function () {
            var vendorOptions = [];
            $.each(vm.equipments, function (index, equipment) {
                if (equipment.categoryName == vm.newConfig.cat2 && equipment.name == vm.newConfig.name) {
                    vendorOptions.push({
                        name: equipment.vendor,
                        price: equipment.price
                    })
                }
            });

            vm.vendorOptions = vendorOptions;
        });

        $scope.$watch('vm.newConfig.vendorPrice', function () {
            if (vm.newConfig.vendorPrice == undefined) {
                return;
            }
            vm.newConfig.vendor = vm.newConfig.vendorPrice.name;
            if (vm.newConfig.unitPrice == undefined) {
                vm.newConfig.unitPrice = vm.newConfig.vendorPrice.price;
            }
        });

        /****************************************** add/remove config item ****************************/

        vm.addNewConfig = function () {
            let valid = $('.new-config-row').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (vm.budget.configItems == undefined) {
                vm.budget.configItems = [];
            }

            vm.budget.configItems.push($.extend(true, {}, vm.newConfig));
            vm.newConfig = {};
        };

        vm.removeConfig = function (index) {
            if (index >= 0 && index < vm.budget.configItems.length) {
                vm.budget.configItems.splice(index, 1);
            }
        };
        /****************************************** Auditor ****************************/

        const modalIdAddAduditor = '#modal-add-shootconfig-auditor';

        $(modalIdAddAduditor).on('shown.bs.modal', function (e) {
            vm.employeesNotIncluded = $.grep(vm.employees, function (item) {
                let exists = vm.auditors.find(function (exitem) {
                    return exitem.id == item.id;
                });
                return !exists;
            });

            $('.select2').select2({
                language: "zh-CN"
            });
        });

        vm.addNewAuditor = function () {
            $(modalIdAddAduditor).modal('show');
        };

        vm.saveNewAuditor = function () {
            if (vm.auditors == undefined) {
                vm.auditors = [];
            }
            if (vm.newAuditors != undefined) {
                vm.auditors.push(vm.newAuditors);
                vm.newAuditors = null;
            }
            $(modalIdAddAduditor).modal('hide');
        };

        vm.deleteAuditor = function (auditor) {
            vm.auditors = vm.auditors.filter(function (item) {
                return item.id != auditor.id;
            });
        };
    }
})();
