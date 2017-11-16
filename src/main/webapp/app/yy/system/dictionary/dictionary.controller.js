(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('SystemDictionaryController', SystemDictionaryController);

    SystemDictionaryController.$inject = ['$scope', '$rootScope', '$state', 'SystemDictionaryService', 'DictionaryService', 'Dept', 'JobTitle'];

    function SystemDictionaryController ($scope, $rootScope, $state, SystemDictionaryService, DictionaryService, Dept, JobTitle) {
        var vm = this;
        vm.isLoading = true;

        load();

        function load() {
            vm.isLoading = true;
            $.when(
                SystemDictionaryService.query({category: '假期类型'}).$promise,
                SystemDictionaryService.query({category: '会议室'}).$promise,
                SystemDictionaryService.query({category: '客户来源'}).$promise,
                SystemDictionaryService.query({category: '行业类别'}).$promise,
                SystemDictionaryService.query({category: '器材类别'}).$promise,
                SystemDictionaryService.query({category: '工作人员类型'}).$promise,
                // SystemDictionaryService.query({category: '演员所属'}).$promise,
                SystemDictionaryService.query({category: '支付方式'}).$promise,
                SystemDictionaryService.query({category: '客户状态'}).$promise,
                // DictionaryService.deptOptions(),
                JobTitle.query({size: 2000}).$promise

            ).then(function (vacationTypes, meetingRooms, clientSource, industry,
                             equipCategory, staffTypes, payMethods, clientStatus, jobTitles) {
                vm.originDics = [
                    vacationTypes[0], meetingRooms[0], clientSource[0], industry[0], equipCategory[0], staffTypes[0],
                    payMethods[0], clientStatus[0],jobTitles
                ];

                vm.dics = [{
                    name: '假期类型',
                    val: firstChildren(vacationTypes) || []
                }, {
                    name: '会议室',
                    val: firstChildren(meetingRooms) || []
                }, {
                    name: '客户来源',
                    val: firstChildren(clientSource) || []
                }, {
                    name: '行业类别',
                    val: firstChildren(industry) || []
                }, {
                    name: '器材类别',
                    val: firstChildren(equipCategory) || []
                }, {
                    name: '工作人员类型',
                    val: firstChildren(staffTypes) || []
                }, {
                    name: '支付方式',
                    val: firstChildren(payMethods) || []
                }, {
                    name: '客户状态',
                    val: firstChildren(clientStatus) || []
                }, {
                    name: '职位',
                    val: jobTitles || []
                }];

                vm.isLoading = false;
            }, function () {
                vm.isLoading = false;
                PNotifyLoadFail();
            });
        }

        function firstChildren(options) {
            if (options == undefined) {
                return [];
            }

            return options[0].children;
        }
        vm.save = function () {
            vm.isLoading = true;
            var ps = [];
            for (let i = 0; i < vm.dics.length - 1; i++) {
                let dic = vm.originDics[i];
                dic.children = vm.dics[i].val;
                ps.push(SystemDictionaryService.update(dic).$promise);
            }


            // // update depts
            // var depts = vm.dics[vm.dics.length-2].val;
            // $.each(depts, function (index, dept) {
            //     if (dept.id != undefined) {
            //         ps.push(Dept.update(dept).$promise);
            //     } else {
            //         ps.push(Dept.save(dept).$promise);
            //     }
            // });
            //
            // update positions
            var positions = vm.dics[vm.dics.length-1].val;
            $.each(positions, function (index, position) {
                if (! position.immutable) {
                    if (position.id != undefined) {
                        ps.push(JobTitle.update(position).$promise);
                    } else {
                        position.immutable = false;
                        ps.push(JobTitle.save(position).$promise);
                    }
                }
            });

            $.when.apply($, ps).then(function () {
                PNotifySaveSuccess();
                load();
            }, function () {
                vm.isLoading = false;
                PNotifySaveFail();
            });
        };


        vm.addNewDicItem = function (categoryIndex) {
            vm.dics[categoryIndex].val.push({name: ''})
        };

        vm.initJsComponents = function initJsComponents() {
            $('.dictionary-content table').parsley();
        };

    }
})();
