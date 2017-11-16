(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ShootConfigEditController', ShootConfigEditController);

    ShootConfigEditController.$inject = ['$scope', '$state', '$stateParams', 'disableEdit', 'ShootConfigService', 'ProjectManagementService', 'EquipmentManagementService', 'StaffManageService', 'ActorRepoService', 'EmployeeService'];

    function ShootConfigEditController ($scope, $state, $stateParams, disableEdit, ShootConfigService, ProjectManagementService, EquipmentManagementService, StaffManageService, ActorRepoService, EmployeeService) {
        var vm = this;

        vm.disableEdit = !(!disableEdit);
        vm.pageTitle = $state.current.data.pageTitle;
        vm.shootConfigId = $stateParams.id;
        vm.isLoading = true;
        vm.isSaving = false;
        vm.shootConfig = {};
        vm.projects = [];
        vm.project = {};
        vm.cat2Options = [];
        vm.nameOptions = [];
        vm.cat1Options = ['器材类', '工作人员', '演员', '服化道', '吃住行', '其他'];
        vm.cat2OptionCaches = {};
        vm.newConfig = {};
        vm.equipments = [];
        vm.employeesNotIncluded = [];
        vm.employees = [];
        vm.staffs = [];
        vm.actors = [];
        vm.auditors = [];
        vm.newAuditors = [];
        vm.projectId = null;

        vm.load = load;
        vm.save = save;
        vm.cancel = cancel;
        vm.load();


        function load() {

            ActorRepoService.query({
                page: 0,
                size: 2000
            }, function (data) {
                vm.actors = data.content;
            });

            if (vm.shootConfigId != undefined) {
                vm.isLoading = true;
                let projectQuery = ProjectManagementService.query({hasShootConfig: false}).$promise;
                let shootQuery = ShootConfigService.get({id: vm.shootConfigId});
                let empQuery = EmployeeService.query().$promise;

                $.when(projectQuery, shootQuery, empQuery).then(function (projects, configResp, employees) {
                    vm.projects = projects;
                    vm.shootConfig = configResp;
                    vm.employees = employees;

                    ProjectManagementService.get({id: vm.shootConfig.projectId}, function (project) {
                        vm.project = project;
                        vm.projectId = vm.project.id;
                    });

                    let employeesById = {};
                    $.each(employees, (index, employee) => {
                        employeesById[employee.id] = employee;
                    });
                    if (vm.shootConfig && vm.shootConfig.approvalRequest) {
                        vm.auditors = approvalChainToApprovers(vm.shootConfig.approvalRequest.approvalRoot, employeesById);
                    } else {
                        vm.auditors = [];
                    }

                    vm.isLoading = false;
                }, function () {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                });
            } else {
                vm.shootConfig = {
                    configItems: [],
                };

                let projectQuery = ProjectManagementService.query().$promise;
                let empQuery = EmployeeService.query().$promise;

                $.when(projectQuery, empQuery).then(function (projects, employees) {
                    vm.projects = projects;
                    vm.employees = employees;
                    vm.isLoading = false;
                }, function () {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                });
            }
        }

        function cancel() {
            $state.go('^');
        }

        function save() {
            if (vm.isSaving) {
                return;
            }

            if (vm.shootConfig.configItems == undefined || vm.shootConfig.configItems.length <= 0) {
                PNotifyError("请先添加配置后再保存！");
                return;
            }

            vm.isSaving = true;

            var valid = $('.shootconfig-edit-content form').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                vm.isSaving = false;
                return;
            }

            if (vm.auditors == undefined || vm.auditors.length <= 0) {
                PNotifyErrorNeedApprover();
                vm.isSaving = false;
                return
            }

            vm.shootConfig.projectId = vm.project.id;
            vm.shootConfig.projectIdNumber = vm.project.idNumber;
            vm.shootConfig.projectName = vm.project.name;

            if (vm.shootConfig.startTime == undefined) {
                vm.shootConfig.startTime = moment().valueOf();
            }
            if (vm.shootConfig.endTime == undefined) {
                vm.shootConfig.endTime = moment('9999-01-01', 'YYYY-MM-DD').valueOf();
            }
            if (vm.project.contract != undefined) {
                vm.shootConfig.contractIdNumber = vm.project.contract.idNumber;
                vm.shootConfig.contractMoney = vm.project.contract == undefined ? 0 : vm.project.contract.moneyAmount;
            }
            if (vm.project.client != undefined) {
                vm.shootConfig.clientName = vm.project.client.name;
            }
            vm.shootConfig.type = 0;

            let approvalRequest = {
                name: vm.project.name
            };
            approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
            vm.shootConfig.approvalRequest = approvalRequest;

            if (vm.shootConfig.id != undefined) {
                ShootConfigService.update(vm.shootConfig, onSaveSuccess, onSaveError);
            } else {
                ShootConfigService.save(vm.shootConfig, onSaveSuccess, onSaveError)
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

        $scope.$watch('vm.projectId', function () {
            if (vm.projectId != undefined && vm.projects != undefined) {
                vm.project = vm.projects.find((item) => item.id == vm.projectId);
            } else {
                vm.project = {};
            }
        });

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });

            $('.new-config-row').parsley();
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
                    vm.equipments = {};

                    let equipmentsByname = {};
                    $.each(equipments, function (index, equipment) {
                        equipmentsByname[equipment.name] = equipment;
                    });

                    $.each(Object.values(equipmentsByname), function (index, equiment) {
                        if (vm.equipments[equiment.categoryName] == undefined) {
                            vm.equipments[equiment.categoryName] = [];
                        }
                        vm.equipments[equiment.categoryName].push(equiment);
                    });

                    let options = Object.keys(vm.equipments);
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
                    vm.nameOptions = vm.equipments[vm.newConfig.cat2];
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

        /****************************************** add/remove config item ****************************/

        vm.addNewConfig = function () {
            let valid = $('.new-config-row').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (vm.shootConfig.configItems == undefined) {
                vm.shootConfig.configItems = [];
            }

            vm.shootConfig.configItems.push($.extend(true, {}, vm.newConfig));
            vm.newConfig = {};
        };

        vm.removeConfig = function (index) {
            if (index >= 0 && index < vm.shootConfig.configItems.length) {
                vm.shootConfig.configItems.splice(index, 1);
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
