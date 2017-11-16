(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('OvertimeWorkController', OvertimeWorkController);

    OvertimeWorkController.$inject = ['$scope', '$state', 'OvertimeWork'];

    function OvertimeWorkController ($scope, $state, OvertimeWork) {
        var vm = this;
        
        vm.overtimeWorks = [];

        loadAll();

        function loadAll() {
            OvertimeWork.query(function(result) {
                vm.overtimeWorks = result;
            });
        }
    }
})();
