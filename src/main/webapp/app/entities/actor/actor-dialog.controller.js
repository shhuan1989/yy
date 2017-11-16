(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ActorDialogController', ActorDialogController);

    ActorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Actor', 'PictureInfo', 'Dictionary'];

    function ActorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Actor, PictureInfo, Dictionary) {
        var vm = this;

        vm.actor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pictureinfos = PictureInfo.query();
        vm.dictionaries = Dictionary.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.actor.id !== null) {
                Actor.update(vm.actor, onSaveSuccess, onSaveError);
            } else {
                Actor.save(vm.actor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:actorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
