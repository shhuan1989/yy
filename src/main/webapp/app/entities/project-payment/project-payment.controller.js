(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectPaymentController', ProjectPaymentController);

    ProjectPaymentController.$inject = ['$scope', '$state', 'ProjectPayment'];

    function ProjectPaymentController ($scope, $state, ProjectPayment) {
        var vm = this;
        
        vm.projectPayments = [];

        loadAll();

        function loadAll() {
            ProjectPayment.query(function(result) {
                vm.projectPayments = result;
            });
        }
    }
})();
