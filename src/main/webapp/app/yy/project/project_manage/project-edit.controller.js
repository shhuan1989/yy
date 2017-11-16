(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectManagementEditController', ProjectManagementEditController);

    ProjectManagementEditController.$inject = ['$timeout', '$scope', '$rootScope', '$state', '$stateParams', 'previousState', 'Principal', 'ProjectManagementService', 'ClientService', 'EmployeeService', 'ContractManagementService', 'disableEdit', 'DictionaryService'];

    function ProjectManagementEditController ($timeout, $scope, $rootScope, $state, $stateParams, previousState, Principal, ProjectManagementService, ClientService, EmployeeService, ContractManagementService, disableEdit, DictionaryService) {
        var vm = this;

        DictionaryService.setOptions(vm);
        vm.clients = [];
        vm.employees = [];
        vm.contracts = [];

        vm.disableEdit = !(!disableEdit);
        vm.pageTitle = $state.current.data.pageTitle;
        vm.project = {hasContract: {}};
        vm.isHuaWei = '0';

        vm.save = save;
        vm.cancel = cancel;

        loadAll();

        function loadAll() {
            vm.isLoading = true;
            if ($rootScope.account == undefined || $rootScope.account.employee == undefined) {
                Principal.identity(true).then(function(account) {
                    $rootScope.account = account;
                    vm.account = $rootScope.account;
                });
            } else {
                vm.account = $rootScope.account;
            }
            var pq = {hasContract: {}};
            if ($stateParams.id != null) {
                pq = ProjectManagementService.get({id: $stateParams.id}).$promise;
            }
            var cq = ClientService.query({showAll: true}).$promise;
            var eq = EmployeeService.query().$promise;
            var tq = ContractManagementService.query().$promise;

            $.when( pq, cq, eq, tq ).then(
                function (pqrsp, cqrsp, eqrsp, tqrsp) {
                    vm.project = pqrsp;
                    vm.project.aesIds = [];
                    if (vm.project.aes) {
                        vm.project.aesIds = vm.project.aes.map(function (ae) {
                            return ae.id;
                        });
                    }
                    vm.isHuaWei = vm.project.huaWei == true ? '1' : '0';

                    vm.clients = cqrsp;
                    vm.employees = eqrsp;
                    vm.contracts = tqrsp.filter(function (contract) {
                        return contract.project == undefined || contract.project.id == vm.project.id;
                    });
                    vm.isLoading = false;
                },
                function () {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                }
            )
        }

        $scope.$watch('vm.project.contract', function () {
            if (vm.project.contract != undefined && vm.project.contract.id != undefined) {
                vm.project.hasContract.id = 1;
            }
        }, true);

        $scope.$watch('vm.project.hasContract', function () {
            if (vm.project.hasContract != undefined && vm.project.hasContract.id != 1) {
                vm.project.contract = null;
            }
        }, true);

        function cancel() {
            $state.go(previousState.name, previousState.params);
        }

        function save () {
            var valid = true;
            $.each($('.project-edit-content form').parsley(), function (i, p) {
                if (!valid || !p.validate()) {
                    valid = false;
                }
            });

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (vm.project.aesIds) {
                vm.project.aes = vm.project.aesIds.map(function (item) {
                    return {
                        id: item
                    }
                });
            }

            vm.isSaving = true;

            if (vm.isHuaWei == '1') {
                vm.project.huaWei = true;
            } else {
                vm.project.huaWei = false;
            }

            if (vm.project.id !== null) {
                ProjectManagementService.update(vm.project, onSaveSuccess, onSaveError);
            } else {
                ProjectManagementService.save(vm.project, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:projectUpdate', result);
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        vm.initJsComponents = function initJsComponents() {
            $('.project-edit-content form').parsley();

            $('.select2').select2({
                language: "zh-CN"
            });
        }
    }
})();
