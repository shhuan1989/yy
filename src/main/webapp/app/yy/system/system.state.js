(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('yy_system', {
            abstract: true,
            parent: 'yy',
            url: '/system'
        });
    }
})();
