(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('NoticeDetailController', NoticeDetailController);

    NoticeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Notice', 'Project', 'Employee', 'Dept'];

    function NoticeDetailController($scope, $rootScope, $stateParams, previousState, entity, Notice, Project, Employee, Dept) {
        var vm = this;

        vm.notice = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:noticeUpdate', function(event, result) {
            vm.notice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
