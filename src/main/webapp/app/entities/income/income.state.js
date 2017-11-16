(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('income', {
            parent: 'entity',
            url: '/income',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.income.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/income/incomes.html',
                    controller: 'IncomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('income');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('income-detail', {
            parent: 'entity',
            url: '/income/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.income.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/income/income-detail.html',
                    controller: 'IncomeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('income');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Income', function($stateParams, Income) {
                    return Income.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'income',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('income-detail.edit', {
            parent: 'income-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/income/income-dialog.html',
                    controller: 'IncomeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Income', function(Income) {
                            return Income.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('income.new', {
            parent: 'income',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/income/income-dialog.html',
                    controller: 'IncomeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                incomeTime: null,
                                amount: null,
                                incomeDesc: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('income', null, { reload: 'income' });
                }, function() {
                    $state.go('income');
                });
            }]
        })
        .state('income.edit', {
            parent: 'income',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/income/income-dialog.html',
                    controller: 'IncomeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Income', function(Income) {
                            return Income.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('income', null, { reload: 'income' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('income.delete', {
            parent: 'income',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/income/income-delete-dialog.html',
                    controller: 'IncomeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Income', function(Income) {
                            return Income.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('income', null, { reload: 'income' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
