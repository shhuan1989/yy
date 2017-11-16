(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('StaffDetailController', StaffDetailController);

    StaffDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Staff', 'Dictionary'];

    function StaffDetailController($scope, $rootScope, $stateParams, previousState, entity, Staff, Dictionary) {
        var vm = this;

        vm.staff = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:staffUpdate', function(event, result) {
            vm.staff = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
