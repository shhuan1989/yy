(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_resource_equipment', {
                parent: 'yy_resource',
                url: '/equipment',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_INSTRUMENTS','EDIT_INSTRUMENTS']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/equipment/equipment.html',
                        controller: 'EquipmentManageControler',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_equipment',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_resource_equipment.edit', {
                parent: 'yy_resource_equipment',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_INSTRUMENTS'],
                    pageTitle: '编辑器材信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/equipment/equipment-edit.html',
                        controller: 'EquipmentEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_equipment',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_resource_equipment.view', {
                parent: 'yy_resource_equipment',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_INSTRUMENTS','EDIT_INSTRUMENTS'],
                    pageTitle: '查看器材信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/equipment/equipment-edit.html',
                        controller: 'EquipmentEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return true;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_equipment',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_resource_equipment.new', {
                parent: 'yy_resource_equipment',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_INSTRUMENTS'],
                    pageTitle: '增加新的器材'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/resource/equipment/equipment-edit.html',
                        controller: 'EquipmentEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_resource_equipment',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
