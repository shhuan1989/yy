(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('NoticeChatDialogController', NoticeChatDialogController);

    NoticeChatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NoticeChat', 'Employee', 'PictureInfo', 'FileInfo', 'Notice'];

    function NoticeChatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NoticeChat, Employee, PictureInfo, FileInfo, Notice) {
        var vm = this;

        vm.noticeChat = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.pictureinfos = PictureInfo.query();
        vm.fileinfos = FileInfo.query();
        vm.notices = Notice.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.noticeChat.id !== null) {
                NoticeChat.update(vm.noticeChat, onSaveSuccess, onSaveError);
            } else {
                NoticeChat.save(vm.noticeChat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:noticeChatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
