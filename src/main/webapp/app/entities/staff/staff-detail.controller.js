(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('StaffDetailController', StaffDetailController);

    StaffDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Staff', 'Dictionary'];

    function StaffDetailController($scope, $rootScope, $stateParams, previousState, entity, Staff, Dictionary) {
        var vm = this;

        vm.staff = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:staffUpdate', function(event, result) {
            vm.staff = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
