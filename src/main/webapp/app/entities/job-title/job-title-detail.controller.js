(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('JobTitleDetailController', JobTitleDetailController);

    JobTitleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobTitle'];

    function JobTitleDetailController($scope, $rootScope, $stateParams, previousState, entity, JobTitle) {
        var vm = this;

        vm.jobTitle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:jobTitleUpdate', function(event, result) {
            vm.jobTitle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
