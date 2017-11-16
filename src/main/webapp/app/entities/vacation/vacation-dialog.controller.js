(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('VacationDialogController', VacationDialogController);

    VacationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vacation', 'Employee'];

    function VacationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vacation, Employee) {
        var vm = this;

        vm.vacation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vacation.id !== null) {
                Vacation.update(vm.vacation, onSaveSuccess, onSaveError);
            } else {
                Vacation.save(vm.vacation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:vacationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
