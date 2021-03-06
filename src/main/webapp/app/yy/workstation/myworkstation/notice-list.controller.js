(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('WksNoticeController', WksNoticeController);

    WksNoticeController.$inject = ['$scope', '$state', '$rootScope', 'AdminNoticeService', 'ProjectManagementService'];

    function WksNoticeController ($scope, $state, $rootScope, AdminNoticeService, ProjectManagementService) {
        var vm = this;
        vm.searchParams = {};
        vm.noticesTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.noticesTableParams.count();
        vm.notices = [];
        vm.isLoading = false;
        vm.projects = ProjectManagementService.query();

        vm.search = search;

        vm.search();

        function search() {
            vm.isLoading = true;

            AdminNoticeService.query($.extend(true, {ownedOnly: true, validate: true}, vm.searchParams),
                (data) => {
                    vm.notices = data;

                    $.each(vm.notices, function (index, notice) {
                        if (notice.projectName == undefined) {
                            notice.projectName = '非项目类公告';
                        }
                    });

                    vm.noticesTableParams.settings({
                        dataset: vm.notices
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
        };
    }
})();
