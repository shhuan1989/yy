(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_shoot_agenda', {
                parent: 'yy_shoot',
                url: '/agenda',
                data: {
                    authorities: ['ROLE_ADMIN', 'VIEW_SHOOT_AGENDA','EDIT_SHOOT_AGENDA','VIEW_OWNED_AGENDA','EDIT_OWNED_AGENDA']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/shoot/agenda/agenda.html',
                        controller: 'ShootAgendaController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
