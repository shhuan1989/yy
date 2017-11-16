(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DeptDetailController', DeptDetailController);

    DeptDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dept'];

    function DeptDetailController($scope, $rootScope, $stateParams, previousState, entity, Dept) {
        var vm = this;

        vm.dept = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yyOaApp:deptUpdate', function(event, result) {
            vm.dept = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
