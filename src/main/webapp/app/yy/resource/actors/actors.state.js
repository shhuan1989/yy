(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_resource_actor', {
                parent: 'yy_resource',
                url: '/actor_management',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_ACTORS','EDIT_ACTORS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/actors/actors.html',
                        controller: 'ActorRepoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_actor',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_resource_actor.edit', {
                parent: 'yy_resource_actor',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_ACTORS'],
                    pageTitle: '编辑演员信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/actors/actor-edit.html',
                        controller: 'ActorEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_actor',
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
            .state('yy_resource_actor.view', {
                parent: 'yy_resource_actor',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_ACTORS','EDIT_ACTORS'],
                    pageTitle: '查看演员信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/actors/actor-edit.html',
                        controller: 'ActorEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return true;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_actor',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_resource_actor.new', {
                parent: 'yy_resource_actor',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_ACTORS'],
                    pageTitle: '增加新的演员'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/actors/actor-edit.html',
                        controller: 'ActorEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_actor',
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
