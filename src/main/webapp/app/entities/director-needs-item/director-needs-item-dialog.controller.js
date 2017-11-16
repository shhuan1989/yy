(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorNeedsItemDialogController', DirectorNeedsItemDialogController);

    DirectorNeedsItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DirectorNeedsItem', 'DirectorNeeds'];

    function DirectorNeedsItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DirectorNeedsItem, DirectorNeeds) {
        var vm = this;

        vm.directorNeedsItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.directorneeds = DirectorNeeds.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.directorNeedsItem.id !== null) {
                DirectorNeedsItem.update(vm.directorNeedsItem, onSaveSuccess, onSaveError);
            } else {
                DirectorNeedsItem.save(vm.directorNeedsItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:directorNeedsItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
