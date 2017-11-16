(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_contract_mgt', {
                parent: 'yy_contract',
                url: '/contract_management',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_CONTRACT_MANAGE','EDIT_CONTRACT_MANAGE']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/contract_management/contract-manage.html',
                        controller: 'ContractManagementController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    searchParams: function() {
                        return {

                        };
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_contract_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_contract_mgt.new', {
                parent: 'yy_contract_mgt',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_CONTRACT_MANAGE'],
                    pageTitle: '增加新的合同'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/contract_management/contract-edit.html',
                        controller: 'ContractEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_contract_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_contract_mgt.view', {
                parent: 'yy_contract_mgt',
                url: '/view_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','VIEW_CONTRACT_MANAGE','EDIT_CONTRACT_MANAGE'],
                    pageTitle: '查看合同信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/contract_management/contract-edit.html',
                        controller: 'ContractEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return true;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_contract_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('yy_contract_mgt.edit', {
                parent: 'yy_contract_mgt',
                url: '/edit_{id}',
                data: {
                    authorities: ['ROLE_ADMIN','EDIT_CONTRACT_MANAGE'],
                    pageTitle: '编辑合同信息'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/contract/contract_management/contract-edit.html',
                        controller: 'ContractEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    disableEdit: function(){
                        return false;
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'yy_contract_mgt',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
