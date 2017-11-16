(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$scope', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function SidebarController ($scope, $state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;
    }
})();
