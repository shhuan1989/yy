(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectTaskController', ProjectTaskController);

    ProjectTaskController.$inject = ['$scope', '$state', '$stateParams', 'previousState', '$timeout', 'Task'];

    function ProjectTaskController ($scope, $state, $stateParams, previousState, $timeout, Task) {
        var vm = this;
        console.log("ProjectTaskController initialized !");

        vm.projectId = previousState.params.id  || $stateParams.projectId;
        vm.taskId = $stateParams.id;

        console.log("task id", vm.taskId);
        console.log("project id", vm.projectId);

    }
})();
