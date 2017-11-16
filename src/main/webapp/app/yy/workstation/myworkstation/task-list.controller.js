(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('WksTaskController', WksTaskController);

    WksTaskController.$inject = ['$scope', '$state', '$rootScope', 'Task'];

    function WksTaskController ($scope, $state, $rootScope, Task) {
        var vm = this;
        vm.tasksTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.tasksTableParams.count();
        vm.tasks = [];
        vm.isLoading = false;
        vm.search = search;

        vm.search();

        function search() {
            vm.isLoading = true;

            Task.query({shownOwnedOnly: true, statusIds: [-2, -1, 0]},
                (data) => {
                    vm.tasks = data;
                    vm.tasksTableParams.settings({
                        dataset: vm.tasks
                    });
                    vm.isLoading = false;
                },
                () => {
                    PNotifySearchFail();
                    vm.isLoading = false;
                }
            );
        }
    }
})();
