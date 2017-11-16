(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('PictureInfoDialogController', PictureInfoDialogController);

    PictureInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PictureInfo'];

    function PictureInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PictureInfo) {
        var vm = this;

        vm.pictureInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.thumbnails = PictureInfo.query({filter: 'originpicture-is-null'});
        $q.all([vm.pictureInfo.$promise, vm.thumbnails.$promise]).then(function() {
            if (!vm.pictureInfo.thumbnail || !vm.pictureInfo.thumbnail.id) {
                return $q.reject();
            }
            return PictureInfo.get({id : vm.pictureInfo.thumbnail.id}).$promise;
        }).then(function(thumbnail) {
            vm.thumbnails.push(thumbnail);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pictureInfo.id !== null) {
                PictureInfo.update(vm.pictureInfo, onSaveSuccess, onSaveError);
            } else {
                PictureInfo.save(vm.pictureInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:pictureInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
