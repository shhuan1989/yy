(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('FileInfoController', FileInfoController);

    FileInfoController.$inject = ['$scope', '$state', 'FileInfo'];

    function FileInfoController ($scope, $state, FileInfo) {
        var vm = this;
        
        vm.fileInfos = [];

        loadAll();

        function loadAll() {
            FileInfo.query(function(result) {
                vm.fileInfos = result;
            });
        }
    }
})();
