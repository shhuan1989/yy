(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('CommentDialogController', CommentDialogController);

    CommentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Comment', 'PictureInfo', 'FileInfo'];

    function CommentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Comment, PictureInfo, FileInfo) {
        var vm = this;

        vm.comment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pictureinfos = PictureInfo.query({filter: 'comment-is-null'});
        $q.all([vm.comment.$promise, vm.pictureinfos.$promise]).then(function() {
            if (!vm.comment.pictureInfoId) {
                return $q.reject();
            }
            return PictureInfo.get({id : vm.comment.pictureInfoId}).$promise;
        }).then(function(pictureInfo) {
            vm.pictureinfos.push(pictureInfo);
        });
        vm.fileinfos = FileInfo.query({filter: 'comment-is-null'});
        $q.all([vm.comment.$promise, vm.fileinfos.$promise]).then(function() {
            if (!vm.comment.fileInfoId) {
                return $q.reject();
            }
            return FileInfo.get({id : vm.comment.fileInfoId}).$promise;
        }).then(function(fileInfo) {
            vm.fileinfos.push(fileInfo);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.comment.id !== null) {
                Comment.update(vm.comment, onSaveSuccess, onSaveError);
            } else {
                Comment.save(vm.comment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:commentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
