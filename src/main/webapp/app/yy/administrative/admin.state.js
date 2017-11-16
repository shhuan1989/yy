(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('yy_admin', {
            abstract: true,
            parent: 'yy',
            url: '/administrative'
        });
    }
})();
