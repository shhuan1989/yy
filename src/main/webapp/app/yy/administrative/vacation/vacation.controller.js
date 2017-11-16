(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('AdmVacationController', AdmVacationController);

    AdmVacationController.$inject = ['$scope', '$rootScope', '$state', 'AdmVacationService', 'DictionaryService', 'EmployeeService'];

    function AdmVacationController ($scope, $rootScope, $state, AdmVacationService, DictionaryService, EmployeeService) {
        var vm = this;
        vm.searchParams = {};
        vm.vacationsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.vacationsTableParams.count();
        vm.vacations = [];
        vm.isLoading = false;
        vm.newVacationRequest = {};
        vm.approvalStatusOptions = [];
        vm.vacationTypeOptions = [];
        vm.jobTitleOptions = [];
        vm.deptOptions = [];
        vm.search = search;

        DictionaryService.approvalStatusOptions().then((options) => vm.approvalStatusOptions = options);
        DictionaryService.vacationTypeOptions().then((options) => vm.vacationTypeOptions = options);
        DictionaryService.deptOptions().then((options) => vm.deptOptions = options);
        DictionaryService.jobTitleOptions().then((options) => vm.jobTitleOptions = options);
        vm.employees = EmployeeService.query();


        vm.search();

        function search() {
            vm.isLoading = true;

            AdmVacationService.query($.extend(true, {}, vm.searchParams),
                (data) => {
                    vm.vacations = data;
                    vm.vacationsTableParams.settings({
                        dataset: vm.vacations
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

            $("#field_vacation_timerange").daterangepicker(
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
