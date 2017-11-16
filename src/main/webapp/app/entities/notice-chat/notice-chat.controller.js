(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('NoticeChatController', NoticeChatController);

    NoticeChatController.$inject = ['$scope', '$state', 'NoticeChat'];

    function NoticeChatController ($scope, $state, NoticeChat) {
        var vm = this;
        
        vm.noticeChats = [];

        loadAll();

        function loadAll() {
            NoticeChat.query(function(result) {
                vm.noticeChats = result;
            });
        }
    }
})();
