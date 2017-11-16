(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('overtime-work', {
            parent: 'entity',
            url: '/overtime-work',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.overtimeWork.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/overtime-work/overtime-works.html',
                    controller: 'OvertimeWorkController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('overtimeWork');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('overtime-work-detail', {
            parent: 'entity',
            url: '/overtime-work/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.overtimeWork.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/overtime-work/overtime-work-detail.html',
                    controller: 'OvertimeWorkDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('overtimeWork');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OvertimeWork', function($stateParams, OvertimeWork) {
                    return OvertimeWork.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'overtime-work',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('overtime-work-detail.edit', {
            parent: 'overtime-work-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/overtime-work/overtime-work-dialog.html',
                    controller: 'OvertimeWorkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OvertimeWork', function(OvertimeWork) {
                            return OvertimeWork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('overtime-work.new', {
            parent: 'overtime-work',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/overtime-work/overtime-work-dialog.html',
                    controller: 'OvertimeWorkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startTime: null,
                                endTime: null,
                                hours: null,
                                info: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('overtime-work', null, { reload: 'overtime-work' });
                }, function() {
                    $state.go('overtime-work');
                });
            }]
        })
        .state('overtime-work.edit', {
            parent: 'overtime-work',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/overtime-work/overtime-work-dialog.html',
                    controller: 'OvertimeWorkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OvertimeWork', function(OvertimeWork) {
                            return OvertimeWork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('overtime-work', null, { reload: 'overtime-work' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('overtime-work.delete', {
            parent: 'overtime-work',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/overtime-work/overtime-work-delete-dialog.html',
                    controller: 'OvertimeWorkDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OvertimeWork', function(OvertimeWork) {
                            return OvertimeWork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('overtime-work', null, { reload: 'overtime-work' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
