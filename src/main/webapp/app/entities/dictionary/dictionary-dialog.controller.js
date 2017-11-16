(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DictionaryDialogController', DictionaryDialogController);

    DictionaryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dictionary'];

    function DictionaryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dictionary) {
        var vm = this;

        vm.dictionary = entity;
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
            if (vm.dictionary.id !== null) {
                Dictionary.update(vm.dictionary, onSaveSuccess, onSaveError);
            } else {
                Dictionary.save(vm.dictionary, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:dictionaryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
