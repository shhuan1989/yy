(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-payment', {
            parent: 'entity',
            url: '/project-payment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.projectPayment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-payment/project-payments.html',
                    controller: 'ProjectPaymentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectPayment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-payment-detail', {
            parent: 'entity',
            url: '/project-payment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yiyingOaApp.projectPayment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-payment/project-payment-detail.html',
                    controller: 'ProjectPaymentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectPayment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectPayment', function($stateParams, ProjectPayment) {
                    return ProjectPayment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-payment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-payment-detail.edit', {
            parent: 'project-payment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-payment/project-payment-dialog.html',
                    controller: 'ProjectPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectPayment', function(ProjectPayment) {
                            return ProjectPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-payment.new', {
            parent: 'project-payment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-payment/project-payment-dialog.html',
                    controller: 'ProjectPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                appointedTime: null,
                                amount: null,
                                payTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-payment', null, { reload: 'project-payment' });
                }, function() {
                    $state.go('project-payment');
                });
            }]
        })
        .state('project-payment.edit', {
            parent: 'project-payment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-payment/project-payment-dialog.html',
                    controller: 'ProjectPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectPayment', function(ProjectPayment) {
                            return ProjectPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-payment', null, { reload: 'project-payment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-payment.delete', {
            parent: 'project-payment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-payment/project-payment-delete-dialog.html',
                    controller: 'ProjectPaymentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectPayment', function(ProjectPayment) {
                            return ProjectPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-payment', null, { reload: 'project-payment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
