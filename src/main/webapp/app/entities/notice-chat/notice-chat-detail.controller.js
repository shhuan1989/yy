(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('NoticeChatDetailController', NoticeChatDetailController);

    NoticeChatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NoticeChat', 'Employee', 'PictureInfo', 'FileInfo', 'Notice'];

    function NoticeChatDetailController($scope, $rootScope, $stateParams, previousState, entity, NoticeChat, Employee, PictureInfo, FileInfo, Notice) {
        var vm = this;

        vm.noticeChat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:noticeChatUpdate', function(event, result) {
            vm.noticeChat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
