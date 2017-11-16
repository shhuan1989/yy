(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorNeedsCommentDeleteController',DirectorNeedsCommentDeleteController);

    DirectorNeedsCommentDeleteController.$inject = ['$uibModalInstance', 'entity', 'DirectorNeedsComment'];

    function DirectorNeedsCommentDeleteController($uibModalInstance, entity, DirectorNeedsComment) {
        var vm = this;

        vm.directorNeedsComment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DirectorNeedsComment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
