(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('NoticeDialogController', NoticeDialogController);

    NoticeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Notice', 'Project', 'Employee', 'Dept'];

    function NoticeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Notice, Project, Employee, Dept) {
        var vm = this;

        vm.notice = entity;
        vm.clear = clear;
        vm.save = save;
        vm.projects = Project.query();
        vm.employees = Employee.query();
        vm.depts = Dept.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.notice.id !== null) {
                Notice.update(vm.notice, onSaveSuccess, onSaveError);
            } else {
                Notice.save(vm.notice, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:noticeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
