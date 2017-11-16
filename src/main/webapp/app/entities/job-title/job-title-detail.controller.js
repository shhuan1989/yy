(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('JobTitleDetailController', JobTitleDetailController);

    JobTitleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobTitle'];

    function JobTitleDetailController($scope, $rootScope, $stateParams, previousState, entity, JobTitle) {
        var vm = this;

        vm.jobTitle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:jobTitleUpdate', function(event, result) {
            vm.jobTitle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
