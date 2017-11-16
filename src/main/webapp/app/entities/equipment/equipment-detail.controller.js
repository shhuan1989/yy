(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('EquipmentDetailController', EquipmentDetailController);

    EquipmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Equipment', 'Dictionary'];

    function EquipmentDetailController($scope, $rootScope, $stateParams, previousState, entity, Equipment, Dictionary) {
        var vm = this;

        vm.equipment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yiyingOaApp:equipmentUpdate', function(event, result) {
            vm.equipment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
