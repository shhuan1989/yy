(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('DeptDetailController', DeptDetailController);

    DeptDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dept'];

    function DeptDetailController($scope, $rootScope, $stateParams, previousState, entity, Dept) {
        var vm = this;

        vm.dept = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:deptUpdate', function(event, result) {
            vm.dept = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
