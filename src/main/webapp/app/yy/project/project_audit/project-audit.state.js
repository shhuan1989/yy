(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_project_audit', {
                parent: 'yy_project',
                url: '/audit_project',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_PROJECT_RATE', 'EDIT_PROJECT_RATE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_audit/project-audit.html',
                        controller: 'ProjectAuditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_project_audit.view', {
                parent: 'yy_project_audit',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_PROJECT_RATE', 'EDIT_PROJECT_RATE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/project_audit/project-audit-detail.html',
                        controller: 'ProjectAuditViewController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
