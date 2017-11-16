(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('yy_sys_dictionary', {
                parent: 'yy_system',
                url: '/config',
                data: {
                    authorities: ['ROLE_ADMIN', 'EDIT_SYS_DIC']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/yy/system/dictionary/dictionary.html',
                        controller: 'SystemDictionaryController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
