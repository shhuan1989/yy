(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('MeetingEntityController', MeetingEntityController);

    MeetingEntityController.$inject = ['$scope', '$state', 'Meeting'];

    function MeetingEntityController ($scope, $state, Meeting) {
        var vm = this;

        vm.meetings = [];

        loadAll();

        function loadAll() {
            Meeting.query(function(result) {
                vm.meetings = result;
            });
        }
    }
})();
