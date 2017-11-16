(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('WksVacationController', WksVacationController);

    WksVacationController.$inject = ['$scope', '$rootScope', '$state', 'WksVacationService', 'DictionaryService'];

    function WksVacationController ($scope, $rootScope, $state, WksVacationService, DictionaryService) {
        var vm = this;
        vm.searchParams = {};
        vm.vacationsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.vacationsTableParams.count();
        vm.vacations = [];
        vm.isLoading = false;
        vm.newVacationRequest = {};
        vm.approvalStatusOptions = {};
        vm.vacationTypeOptions = {};
        vm.search = search;

        DictionaryService.approvalStatusOptions().then((options) => vm.approvalStatusOptions = options);
        DictionaryService.vacationTypeOptions().then((options) => vm.vacationTypeOptions = options);

        vm.search();

        function search() {
            vm.isLoading = true;

            WksVacationService.query($.extend(true, {shownOwnedOnly: true}, vm.searchParams),
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


        vm.deleteVacationRequest = function (vacation) {

            bootboxConfirm("确认删除 <span class='text-aqua'>" + vacation.type.name + ' ' +
                formatTimestampDMY(vacation.startTime) + '-' +
                formatTimestampDMY(vacation.endTime) + "</span> ?",
                function () {
                    WksVacationService.delete({id: vacation.id},
                        function () {
                            vm.search();
                            PNotifyDeleteSuccess();
                        },
                        function () {
                            PNotifyDeleteFail();
                        }
                    );
                }
            );
        };

        $scope.$watch('vm.pageSize', function () {
            vm.vacationsTableParams.count(vm.pageSize);
        });
        
        vm.initSearchDateRangePicker = function () {
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
