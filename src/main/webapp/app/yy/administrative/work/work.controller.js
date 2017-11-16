(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('AdminWorkController', AdminWorkController);

    AdminWorkController.$inject = ['$scope', '$rootScope', '$state', 'AdminWorkService', 'DictionaryService', 'EmployeeService'];

    function AdminWorkController ($scope, $rootScope, $state, AdminWorkService, DictionaryService, EmployeeService) {
        var vm = this;
        vm.searchParams = {};
        vm.worksTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.worksTableParams.count();
        vm.works = [];
        vm.isLoading = false;
        vm.approvalStatusOptions = {};
        vm.search = search;

        DictionaryService.approvalStatusOptions().then((options) => vm.approvalStatusOptions = options);
        DictionaryService.deptOptions().then((options) => vm.deptOptions = options);
        DictionaryService.jobTitleOptions().then((options) => vm.jobTitleOptions = options);
        vm.employees = EmployeeService.query();

        vm.search();

        function search() {
            vm.isLoading = true;

            AdminWorkService.query($.extend(true, {}, vm.searchParams),
                (data) => {
                    vm.works = data;
                    vm.worksTableParams.settings({
                        dataset: vm.works
                    });
                    vm.isLoading = false;
                },
                () => {
                    PNotifySearchFail();
                    vm.isLoading = false;
                }
            );
        }

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });

            $("#field_work_timerange").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.searchParams.timeFrom = start.valueOf();
                    vm.searchParams.timeTo = end.valueOf();
                }
            );
        };

    }
})();
