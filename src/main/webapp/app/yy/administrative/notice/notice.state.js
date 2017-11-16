(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_admin_notice', {
                parent: 'yy_admin',
                url: '/notices',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_NOTICE','EDIT_NOTICE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/notice/notice.html',
                        controller: 'AdminNoticeController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_notice.new', {
                parent: 'yy_admin_notice',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_NOTICE'],
                    pageTitle: '新建公告'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/notice/notice-edit.html',
                        controller: 'AdminNoticeEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_notice.edit', {
                parent: 'yy_admin_notice',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_NOTICE'],
                    pageTitle: '编辑公告'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/notice/notice-edit.html',
                        controller: 'AdminNoticeEditController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('yy_admin_notice.view', {
                parent: 'yy_admin_notice',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_NOTICE','EDIT_NOTICE'],
                    pageTitle: '查看公告'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/administrative/notice/notice-view.html',
                        controller: 'AdminNoticeViewController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
