(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('MeetingDialogController', MeetingDialogController);

    MeetingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Meeting', 'Employee', 'Room', 'Project'];

    function MeetingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Meeting, Employee, Room, Project) {
        var vm = this;

        vm.meeting = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.rooms = Room.query();
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.meeting.id !== null) {
                Meeting.update(vm.meeting, onSaveSuccess, onSaveError);
            } else {
                Meeting.save(vm.meeting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:meetingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
