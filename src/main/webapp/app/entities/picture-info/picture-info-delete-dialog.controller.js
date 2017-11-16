(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('PictureInfoDeleteController',PictureInfoDeleteController);

    PictureInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'PictureInfo'];

    function PictureInfoDeleteController($uibModalInstance, entity, PictureInfo) {
        var vm = this;

        vm.pictureInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PictureInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
