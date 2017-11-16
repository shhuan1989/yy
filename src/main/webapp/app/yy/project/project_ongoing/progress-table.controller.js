(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('ProjectProgressTableController', ProjectProgressTableController);

    ProjectProgressTableController.$inject = ['$scope', '$state', '$stateParams', 'ProjectManagementService', '$timeout', 'ProjectProgressTableService', 'EmployeeService'];

    function ProjectProgressTableController ($scope, $state, $stateParams, ProjectManagementService, $timeout, ProjectProgressTableService, EmployeeService) {
        var vm = this;

        vm.employees = EmployeeService.query();
        vm.projectId = $stateParams.id;
        vm.project = ProjectManagementService.get({id: vm.projectId});
        vm.projectProgress = {
            projectId: vm.projectId,
            items: [
                {
                    ord: 1
                },
                {
                    ord: 2
                },
                {
                    ord: 3
                },
                {
                    ord: 4
                },
                {
                    ord: 5
                }
            ]
        };

        vm.save = save;
        vm.cancel = cancel;

        load();

        function load() {
            if (vm.projectId != undefined) {
                ProjectProgressTableService.query({projectId: vm.projectId}, function (pps) {
                    if (pps != undefined && pps.length > 0) {
                        vm.projectProgress = pps[0];
                    }

                    for (let i =1; i <= 5; i++) {
                        $('#field_p'+i+'_eta').datetimepicker({
                            format: dateFormatDMY(),
                            locale: 'zh-CN',
                            defaultDate: datetimePickerDefaultTime(vm.projectProgress.items[i-1].eta)
                        }).on('dp.change', function(ev){
                            vm.projectProgress.items[i-1].eta= moment(ev.date).valueOf();
                        });

                        $('#field_p'+i+'_ft').datetimepicker({
                            format: dateFormatDMY(),
                            locale: 'zh-CN',
                            defaultDate: datetimePickerDefaultTime(vm.projectProgress.items[i-1].finishTime)
                        }).on('dp.change', function(ev){
                            vm.projectProgress.items[i-1].finishTime= moment(ev.date).valueOf();
                        });
                    }
                }, function () {
                    PNotifyLoadFail();
                });
            }
        }

        function save() {
            var valid = $('.progress-table-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return
            }

            let pitems = vm.projectProgress.items;
            
            for (let i = 1; i < pitems.length; i++) {
                if (pitems[i].eta < pitems[i-1].eta) {
                    PNotifyError('下一个进度的计划时间不可早于上一个进度的计划时间');
                    return;
                }
            }

            let notFinished = false;
            for (let i = 0; i < pitems.length; i++) {
                if (pitems[i] == undefined) {
                    notFinished = true;
                } else {
                    if (notFinished) {
                        PNotifyError('完成时间必须依次填写');
                        return;
                    }
                }
            }


            if (vm.projectProgress.id != undefined) {
                ProjectProgressTableService.update(vm.projectProgress, onSaveSuccess, onSaveError);
            } else {
                ProjectProgressTableService.save(vm.projectProgress, onSaveSuccess, onSaveError);
            }

            function onSaveSuccess (result) {
                PNotifySaveSuccess();
                $state.go("^");
            }

            function onSaveError () {
                PNotifySaveFail();
                $state.go("^");
            }

        }

        function cancel() {
            $state.go("^");
        }

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });
            $('.progress-table-edit-content form').parsley();
        };

    }
})();
