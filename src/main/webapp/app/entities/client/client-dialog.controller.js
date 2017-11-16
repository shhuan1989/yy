(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ClientDialogController', ClientDialogController);

    ClientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Client', 'Dictionary'];

    function ClientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Client, Dictionary) {
        var vm = this;

        vm.client = entity;
        vm.clear = clear;
        vm.save = save;
        vm.industries = Dictionary.query({filter: 'client-is-null'});
        $q.all([vm.client.$promise, vm.industries.$promise]).then(function() {
            if (!vm.client.industryId) {
                return $q.reject();
            }
            return Dictionary.get({id : vm.client.industryId}).$promise;
        }).then(function(industry) {
            vm.industries.push(industry);
        });
        vm.sources = Dictionary.query({filter: 'client-is-null'});
        $q.all([vm.client.$promise, vm.sources.$promise]).then(function() {
            if (!vm.client.sourceId) {
                return $q.reject();
            }
            return Dictionary.get({id : vm.client.sourceId}).$promise;
        }).then(function(source) {
            vm.sources.push(source);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.client.id !== null) {
                Client.update(vm.client, onSaveSuccess, onSaveError);
            } else {
                Client.save(vm.client, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:clientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
