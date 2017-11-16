(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProjectDialogController', ProjectDialogController);

    ProjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Project', 'Client', 'Contract', 'Employee'];

    function ProjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Project, Client, Contract, Employee) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.save = save;
        vm.clients = Client.query();
        vm.contracts = Contract.query({filter: 'project-is-null'});
        $q.all([vm.project.$promise, vm.contracts.$promise]).then(function() {
            if (!vm.project.contractId) {
                return $q.reject();
            }
            return Contract.get({id : vm.project.contractId}).$promise;
        }).then(function(contract) {
            vm.contracts.push(contract);
        });
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.project.id !== null) {
                Project.update(vm.project, onSaveSuccess, onSaveError);
            } else {
                Project.save(vm.project, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yyOaApp:projectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
