(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_wks_bonus', {
                parent: 'yy_workstation',
                url: '/bonus',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/workstation/bonus/bonus.html',
                        controller: 'WksBonusController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
