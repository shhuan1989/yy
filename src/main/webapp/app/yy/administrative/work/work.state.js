(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_admin_work', {
                parent: 'yy_admin',
                url: '/work',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_OTWORK_RECORDS','EDIT_OTWORK_RECORDS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/work/work.html',
                        controller: 'AdminWorkController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_work.view', {
                parent: 'yy_admin_work',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_OTWORK_RECORDS','EDIT_OTWORK_RECORDS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/work/work-view.html',
                        controller: 'AdminWorkViewController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
