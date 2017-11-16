(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ShootCostEditController', ShootCostEditController);

    ShootCostEditController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'disableEdit', 'ShootCostService', 'ProjectManagementService', 'EmployeeService', 'EquipmentManagementService', 'DictionaryService'];

    function ShootCostEditController ($scope, $rootScope, $state, $stateParams, disableEdit, ShootCostService, ProjectManagementService, EmployeeService, EquipmentManagementService, DictionaryService) {
        var vm = this;

        vm.disableEdit = !(!disableEdit);
        vm.projectId = $stateParams.id;
        vm.pageTitle = $state.current.data.pageTitle;
        vm.project = {};
        vm.shootCosts = [];
        vm.shootCost = {};
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
        vm.save = save;

        load();

        function load() {
            let equipQuery = EquipmentManagementService.query({
                page: 0,
                size: 10000000,
                sort: ['name', 'category', 'vendor', 'id']
            }).$promise;
            let shootCostQuery = ShootCostService.query({projectId: vm.projectId}).$promise;
            let empQuery = EmployeeService.query().$promise;

            $.when(equipQuery, shootCostQuery, empQuery).then(function (equipments, shootCosts, employees) {
                vm.shootCosts = shootCosts;
                vm.shootCost = vm.shootCosts[vm.shootCosts.length - 1];
                vm.shootCosts.pop();

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

                // the current editing one
                vm.configItems = groupShootBudget(vm.shootCost.configItems, vm.equipmentsByName);
                $.each(vm.configItems, function (index, item) {
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

                if (vm.shootCost && vm.shootCost.approvalRequest) {
                    vm.auditors = approvalChainToApprovers(vm.shootCost.approvalRequest.approvalRoot, employeesById);
                } else {
                    vm.auditors = [];
                }

                ProjectManagementService.get({id: vm.shootCost.projectId}, function (project) {
                    vm.project = project;
                    vm.isLoading = false;
                }, onLoadError)
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
                            if (!isNaN(item.unitPrice)) {
                                let e1 = equipments.find((e) => e.price == item.unitPrice);
                                if (e1 != undefined) {
                                    item.unitPrice = equipment.price;
                                }
                            } else {
                                item.unitPrice = equipment.price;
                            }
                        }
                    } else {
                        item.vendor = equipments[0].vendor;
                        item.unitPrice = equipments[0].price;
                    }
                }

                item.actualCost = 1;
                if (!isNaN(item.actualCost)) {
                    item.actualCost *= item.unitPrice;
                }

                if (!isNaN(item.amount)) {
                    item.actualCost *= item.actualAmount;
                }

                if (!isNaN(item.days)) {
                    item.actualCost *= item.actualDays;
                }

                if (item.actualCost == 1) {
                    item.actualCost = '';
                }
            });
            vm.shootCost.actualCost = calCatCostReturnTotalCost(vm.configItems);
        }, true);

        function cancel() {
            $state.go('^');
        }

        function save() {
            if (vm.isSaving) {
                return;
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

            if (!vm.disableEdit) {
                // only edit last cost
                if (vm.auditors == undefined || vm.auditors.length <= 0) {
                    PNotifyErrorNeedApprover();
                    vm.isSaving = false;
                    return
                }

                let validate = $('#shoot-cost-edit-table').parsley().validate();
                if (!validate) {
                    PNotifyInvalidInput();
                    vm.isSaving = false;
                    return;
                }

                vm.isSaving = true;

                vm.shootCost.projectId = vm.projectId;
                if (vm.shootCost.startTime == undefined) {
                    vm.shootCost.startTime = moment().valueOf();
                }
                if (vm.shootCost.endTime == undefined) {
                    vm.shootCost.endTime = moment('9999-01-01', 'YYYY-MM-DD').valueOf();
                }

                vm.shootCost.configItems = groupedConfigItems2ConfigItems(vm.configItems);
                $.each(vm.shootCost.configItems, function (i, item) {
                    item.paymentStatus = item.paymentStatusVal == '1';
                });

                let approvalRequest = {
                    name: vm.project.name
                };
                approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
                vm.shootCost.approvalRequest = approvalRequest;

                if (vm.shootCost.id != undefined) {
                    ShootCostService.update(vm.shootCost, onSaveSuccess, onSaveError);
                } else {
                    ShootCostService.save(vm.shootCost, onSaveSuccess, onSaveError)
                }
            } else {
                vm.shootCost.configItems = groupedConfigItems2ConfigItems(vm.configItems);
                $.each(vm.shootCost.configItems, function (i, item) {
                    item.paymentStatus = item.paymentStatusVal == '1';
                });
                ShootCostService.update(vm.shootCost, onSaveSuccess, onSaveError);
            }

            // save payment status
            $.each(vm.shootCosts, function (index, shootCost) {
                // groupedConfigItems => configItems
                shootCost.configItems = groupedConfigItems2ConfigItems(shootCost.groupedConfigItems);
                $.each(shootCost.configItems, function (i, item) {
                    item.paymentStatus = item.paymentStatusVal == '1';
                });
                ShootCostService.update(shootCost);
            });

        }

        vm.initJsComponents = function () {
            $('.shootcost-edit-content table').parsley();
        };

        /****************************************** Auditor ****************************/

        const modalIdAddAduditor = '#modal-add-shootcost-auditor';

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
