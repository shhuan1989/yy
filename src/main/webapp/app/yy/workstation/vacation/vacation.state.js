(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_wks_vacation', {
                parent: 'yy_workstation',
                url: '/vacation',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/vacation/vacation.html',
                        controller: 'WksVacationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_wks_vacation.new', {
                parent: 'yy_wks_vacation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/vacation/vacation-edit.html',
                        controller: 'WksVacationEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_wks_vacation.view', {
                parent: 'yy_wks_vacation',
                url: '/view/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/vacation/vacation-view.html',
                        controller: 'WksVacationViewController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
