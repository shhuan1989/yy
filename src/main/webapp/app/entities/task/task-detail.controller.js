(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Task', 'Employee', 'FileInfo', 'PictureInfo', 'Dictionary', 'Comment', 'Project'];

    function TaskDetailController($scope, $rootScope, $stateParams, previousState, entity, Task, Employee, FileInfo, PictureInfo, Dictionary, Comment, Project) {
        var vm = this;

        vm.task = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
