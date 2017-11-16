(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('asset-borrow-record', {
            parent: 'entity',
            url: '/asset-borrow-record',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.assetBorrowRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asset-borrow-record/asset-borrow-records.html',
                    controller: 'AssetBorrowRecordController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('assetBorrowRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('asset-borrow-record-detail', {
            parent: 'entity',
            url: '/asset-borrow-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'yyOaApp.assetBorrowRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asset-borrow-record/asset-borrow-record-detail.html',
                    controller: 'AssetBorrowRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('assetBorrowRecord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AssetBorrowRecord', function($stateParams, AssetBorrowRecord) {
                    return AssetBorrowRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'asset-borrow-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('asset-borrow-record-detail.edit', {
            parent: 'asset-borrow-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asset-borrow-record/asset-borrow-record-dialog.html',
                    controller: 'AssetBorrowRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AssetBorrowRecord', function(AssetBorrowRecord) {
                            return AssetBorrowRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('asset-borrow-record.new', {
            parent: 'asset-borrow-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asset-borrow-record/asset-borrow-record-dialog.html',
                    controller: 'AssetBorrowRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                borrowTime: null,
                                estimateReturnTime: null,
                                actualReturnTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('asset-borrow-record', null, { reload: 'asset-borrow-record' });
                }, function() {
                    $state.go('asset-borrow-record');
                });
            }]
        })
        .state('asset-borrow-record.edit', {
            parent: 'asset-borrow-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asset-borrow-record/asset-borrow-record-dialog.html',
                    controller: 'AssetBorrowRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AssetBorrowRecord', function(AssetBorrowRecord) {
                            return AssetBorrowRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('asset-borrow-record', null, { reload: 'asset-borrow-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('asset-borrow-record.delete', {
            parent: 'asset-borrow-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asset-borrow-record/asset-borrow-record-delete-dialog.html',
                    controller: 'AssetBorrowRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AssetBorrowRecord', function(AssetBorrowRecord) {
                            return AssetBorrowRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('asset-borrow-record', null, { reload: 'asset-borrow-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
