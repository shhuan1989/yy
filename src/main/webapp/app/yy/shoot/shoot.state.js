(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('yy_shoot', {
            abstract: true,
            parent: 'yy',
            url: '/shoot'
        });
    }
})();
