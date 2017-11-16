(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('WksWorkController', WksWorkController);

    WksWorkController.$inject = ['$scope', '$rootScope', '$state', 'WksWorkService', 'DictionaryService'];

    function WksWorkController ($scope, $rootScope, $state, WksWorkService, DictionaryService) {
        var vm = this;
        vm.searchParams = {};
        vm.worksTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.worksTableParams.count();
        vm.works = [];
        vm.isLoading = false;
        vm.newWorkRequest = {};
        vm.approvalStatusOptions = {};
        vm.search = search;

        DictionaryService.approvalStatusOptions().then((options) => vm.approvalStatusOptions = options);

        vm.search();

        function search() {
            vm.isLoading = true;

            WksWorkService.query($.extend(true, {shownOwnedOnly: true}, vm.searchParams),
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


        vm.deleteWorkRequest = function (work) {

            bootboxConfirm('确认删除 <span class="text-aqua"> ' +
                formatTimestampDmyHms(work.startTime) + '-' +
                formatTimestampDmyHms(work.endTime) + '</span> 的加班?',
                function () {
                    WksWorkService.delete({id: work.id},
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
            vm.worksTableParams.count(vm.pageSize);
        });

        vm.initSearchDateRangePicker = function () {
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
