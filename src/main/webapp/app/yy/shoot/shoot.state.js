(function() {
    'use strict';

    angular
        .module('yyOaApp')
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
