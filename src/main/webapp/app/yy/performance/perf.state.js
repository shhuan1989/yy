(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('yy_perf', {
            abstract: true,
            parent: 'yy',
            url: '/performance'
        });
    }
})();
