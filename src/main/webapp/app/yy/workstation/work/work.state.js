(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_wks_work', {
                parent: 'yy_workstation',
                url: '/work',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/work/work.html',
                        controller: 'WksWorkController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_wks_work.new', {
                parent: 'yy_wks_work',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/work/work-edit.html',
                        controller: 'WksWorkEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_wks_work.view', {
                parent: 'yy_wks_work',
                url: '/view/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/work/work-view.html',
                        controller: 'WksWorkViewController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
