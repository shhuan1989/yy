(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectPaymentDetailController', ProjectPaymentDetailController);

    ProjectPaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectPayment', 'Project'];

    function ProjectPaymentDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectPayment, Project) {
        var vm = this;

        vm.projectPayment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:projectPaymentUpdate', function(event, result) {
            vm.projectPayment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
