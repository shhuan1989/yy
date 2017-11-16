(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('IncomeDialogController', IncomeDialogController);

    IncomeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Income', 'Dictionary'];

    function IncomeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Income, Dictionary) {
        var vm = this;

        vm.income = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dictionaries = Dictionary.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.income.id !== null) {
                Income.update(vm.income, onSaveSuccess, onSaveError);
            } else {
                Income.save(vm.income, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:incomeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
