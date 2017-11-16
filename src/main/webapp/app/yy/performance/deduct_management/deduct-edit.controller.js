(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DeductEditController', DeductEditController);

    DeductEditController.$inject = ['$timeout', '$scope', '$rootScope', '$state', '$stateParams', '$q', 'entity', 'DeductManagementService', 'disableEdit', 'EmployeeService'];

    function DeductEditController ($timeout, $scope, $rootScope, $state, $stateParams, $q, entity, DeductManagementService, disableEdit, EmployeeService) {
        var vm = this;

        vm.disableEdit = !(!disableEdit);
        vm.pageTitle = $state.current.data.pageTitle;
        vm.employee = entity;

        vm.save = save;
        vm.cancel = cancel;

        vm.employees = EmployeeService.query();

        function formatPercentage(employee) {
            employee.employeeDeductionPercentage = employee.employeeDeduction * 100;
            employee.probationDeductionPercentage = employee.probationDeduction * 100;
        }

        $scope.$watch('vm.employee', function () {
           formatPercentage(vm.employee);
        });


        function cancel() {
            $state.go("^");
        }

        function save () {
            var valid = $('.employee-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            vm.isSaving = true;
            vm.employee.employeeDeduction = vm.employee.employeeDeductionPercentage / 100;
            vm.employee.probationDeduction = vm.employee.probationDeductionPercentage / 100;
            if (vm.employee.id !== null) {
                DeductManagementService.update(vm.employee, onSaveSuccess, onSaveError);
            } else {
                DeductManagementService.save(vm.employee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:employeeUpdate', result);
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        vm.initJsComponents = function initJsComponents() {
            $('.employee-edit-content form').parsley();

            $('.select2').select2({
                language: "zh-CN"
            });
        }
    }
})();
