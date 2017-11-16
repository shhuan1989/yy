(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_meeting', {
                parent: 'yy_project',
                url: '/meeting',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_MEETING', 'EDIT_MEETING'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/meeting/meeting.html',
                        controller: 'MeetingController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_meeting',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('yy_meeting.new', {
                parent: 'yy_meeting',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_MEETING'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/project/meeting/meeting-edit.html',
                        controller: 'MeetingEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_meeting',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    }
})();
