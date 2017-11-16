(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$rootScope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, $rootScope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.meetings = [];
        vm.employee = {};
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                $rootScope.account = account;
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;

                if (!vm.isAuthenticated()) {
                    vm.login();
                }
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
