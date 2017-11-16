(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('MeetingDetailController', MeetingDetailController);

    MeetingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Meeting', 'Employee', 'Room', 'Project'];

    function MeetingDetailController($scope, $rootScope, $stateParams, previousState, entity, Meeting, Employee, Room, Project) {
        var vm = this;

        vm.meeting = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:meetingUpdate', function(event, result) {
            vm.meeting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
