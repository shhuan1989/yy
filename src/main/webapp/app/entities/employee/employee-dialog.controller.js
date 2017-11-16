(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('EmployeeDialogController', EmployeeDialogController);

    EmployeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Employee', 'JobTitle', 'City', 'Dept', 'Project'];

    function EmployeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Employee, JobTitle, City, Dept, Project) {
        var vm = this;

        vm.employee = entity;
        vm.clear = clear;
        vm.save = save;
        vm.jobtitles = JobTitle.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.jobtitles.$promise]).then(function() {
            if (!vm.employee.jobTitleId) {
                return $q.reject();
            }
            return JobTitle.get({id : vm.employee.jobTitleId}).$promise;
        }).then(function(jobTitle) {
            vm.jobtitles.push(jobTitle);
        });
        vm.nativeplaces = City.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.nativeplaces.$promise]).then(function() {
            if (!vm.employee.nativePlaceId) {
                return $q.reject();
            }
            return City.get({id : vm.employee.nativePlaceId}).$promise;
        }).then(function(nativePlace) {
            vm.nativeplaces.push(nativePlace);
        });
        vm.depts = Dept.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.depts.$promise]).then(function() {
            if (!vm.employee.deptId) {
                return $q.reject();
            }
            return Dept.get({id : vm.employee.deptId}).$promise;
        }).then(function(dept) {
            vm.depts.push(dept);
        });
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.employee.id !== null) {
                Employee.update(vm.employee, onSaveSuccess, onSaveError);
            } else {
                Employee.save(vm.employee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:employeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
