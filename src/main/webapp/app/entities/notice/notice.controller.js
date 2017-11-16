(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('NoticeController', NoticeController);

    NoticeController.$inject = ['$scope', '$state', 'Notice'];

    function NoticeController ($scope, $state, Notice) {
        var vm = this;
        
        vm.notices = [];

        loadAll();

        function loadAll() {
            Notice.query(function(result) {
                vm.notices = result;
            });
        }
    }
})();
