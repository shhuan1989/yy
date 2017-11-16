(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('WksGoodsController', WksGoodsController);

    WksGoodsController.$inject = ['$scope', '$state', '$rootScope', 'DictionaryService', 'WksGoodsService', 'EmployeeService'];

    function WksGoodsController ($scope, $state, $rootScope, DictionaryService, WksGoodsService, EmployeeService) {
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
        vm.deleteRecord = deleteRecord;

        DictionaryService.approvalStatusOptions().then((options) => vm.approvalStatusOptions = options);
        vm.employees = EmployeeService.query();


        vm.search();

        function search() {
            vm.isLoading = true;

            WksGoodsService.query($.extend(true, {shownOwnedOnly: true}, vm.searchParams),
                (data) => {
                    vm.borrowRecords = data;
                    vm.isLoading = false;
                },
                () => {
                    PNotifySearchFail();
                    vm.isLoading = false;
                }
            );
        }

        function deleteRecord(record) {
            if (record == undefined || record.id == undefined) {
                return;
            }

            bootboxConfirm("确认删除申请 <span class='text-aqua'>" + record.asset.name + "</span> ?", function () {
                WksGoodsService.delete({id: record.id}, function () {
                    vm.borrowRecords = vm.borrowRecords.filter((item) => item.id != record.id);
                    PNotifySaveSuccess();
                }, function () {
                    PNotifySaveFail();
                });
            });
        }

        $scope.$watch('vm.pageSize', function () {
            vm.borrowRecordsTableParams.count(vm.pageSize);
        });
        
        $scope.$watch('vm.borrowRecords', function () {
            vm.borrowRecordsTableParams.settings({
                dataset: vm.borrowRecords
            });
        }, true);

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
