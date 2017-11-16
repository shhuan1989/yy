(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_admin_vacation', {
                parent: 'yy_admin',
                url: '/vacation',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_VACATION_RECORDS','EDIT_VACATION_RECORDS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/vacation/vacation.html',
                        controller: 'AdmVacationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_vacation.view', {
                parent: 'yy_admin_vacation',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_VACATION_RECORDS','EDIT_VACATION_RECORDS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/vacation/vacation-view.html',
                        controller: 'AdmVacationViewController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
