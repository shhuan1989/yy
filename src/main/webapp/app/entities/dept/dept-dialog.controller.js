(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DeptDialogController', DeptDialogController);

    DeptDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dept'];

    function DeptDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dept) {
        var vm = this;

        vm.dept = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dept.id !== null) {
                Dept.update(vm.dept, onSaveSuccess, onSaveError);
            } else {
                Dept.save(vm.dept, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:deptUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
