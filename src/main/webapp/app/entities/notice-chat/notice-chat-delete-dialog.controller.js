(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('NoticeChatDeleteController',NoticeChatDeleteController);

    NoticeChatDeleteController.$inject = ['$uibModalInstance', 'entity', 'NoticeChat'];

    function NoticeChatDeleteController($uibModalInstance, entity, NoticeChat) {
        var vm = this;

        vm.noticeChat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NoticeChat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
