(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DictionaryDeleteController',DictionaryDeleteController);

    DictionaryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dictionary'];

    function DictionaryDeleteController($uibModalInstance, entity, Dictionary) {
        var vm = this;

        vm.dictionary = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dictionary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
