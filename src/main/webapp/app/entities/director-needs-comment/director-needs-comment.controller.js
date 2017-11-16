(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorNeedsCommentController', DirectorNeedsCommentController);

    DirectorNeedsCommentController.$inject = ['$scope', '$state', 'DirectorNeedsComment'];

    function DirectorNeedsCommentController ($scope, $state, DirectorNeedsComment) {
        var vm = this;
        
        vm.directorNeedsComments = [];

        loadAll();

        function loadAll() {
            DirectorNeedsComment.query(function(result) {
                vm.directorNeedsComments = result;
            });
        }
    }
})();
