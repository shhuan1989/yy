(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('AdminGoodsController', AdminGoodsController);

    AdminGoodsController.$inject = ['$scope', '$state', '$rootScope', 'DictionaryService', 'AdminGoodsService', 'EmployeeService'];

    function AdminGoodsController ($scope, $state, $rootScope, DictionaryService, AdminGoodsService, EmployeeService) {
        var vm = this;
        vm.searchParams = {};
        vm.borrowRecordsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.borrowRecordsTableParams.count();
        vm.borrowRecords = [];
        vm.isLoading = false;
        vm.approvalStatusOptions = [];
        vm.borrowRecordTypeOptions = [];
        vm.jobTitleOptions = [];
        vm.deptOptions = [];
        vm.search = search;
        vm.returnAsset = returnAsset;

        DictionaryService.approvalStatusOptions().then((options) => vm.approvalStatusOptions = options);
        DictionaryService.borrowRecordTypeOptions().then((options) => vm.borrowRecordTypeOptions = options);
        DictionaryService.deptOptions().then((options) => vm.deptOptions = options);
        DictionaryService.jobTitleOptions().then((options) => vm.jobTitleOptions = options);
        vm.employees = EmployeeService.query();


        vm.search();

        function search() {
            vm.isLoading = true;

            AdminGoodsService.query($.extend(true, {}, vm.searchParams),
                (data) => {
                    vm.borrowRecords = data;
                    vm.borrowRecordsTableParams.settings({
                        dataset: vm.borrowRecords
                    });
                    vm.isLoading = false;
                },
                () => {
                    PNotifySearchFail();
                    vm.isLoading = false;
                }
            );
        }

        function returnAsset(record) {
            if (record == undefined) {
                return;
            }

            let savingRecord = $.extend(true, {}, record);
            savingRecord.returnStatus = {id: 1};
            savingRecord.actualEndTime = moment().valueOf();

            AdminGoodsService.update(savingRecord, function (savedRecord) {
                vm.search();
                PNotifySaveSuccess();
            }, function () {
                PNotifySaveFail();
            })
        }

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });

            $("#field_borrowrecord_timerange").daterangepicker(
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
