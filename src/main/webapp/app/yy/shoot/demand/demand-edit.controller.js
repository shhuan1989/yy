(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('DirectorDemandEditController', DirectorDemandEditController);

    DirectorDemandEditController.$inject = ['$scope', '$state', '$stateParams', 'disableEdit', 'DirectorDemandService', 'ProjectManagementService'];

    function DirectorDemandEditController ($scope, $state, $stateParams, disableEdit, DirectorDemandService, ProjectManagementService) {
        var vm = this;

        vm.disableEdit = !(!disableEdit);
        vm.pageTitle = $state.current.data.pageTitle;
        vm.shootConfigId = $stateParams.id;
        vm.isLoading = true;
        vm.isSaving = false;
        vm.shootConfig = {};
        vm.projects = [];
        vm.project = {};
        vm.newConfig = {};
        vm.projectId = null;
        vm.isView = false;
        vm.newCommentText = "";

        vm.load = load;
        vm.save = save;
        vm.cancel = cancel;
        vm.load();


        function load() {
            vm.isView = $state.$current.toString() == "yy_director_needs.view";

            if (vm.shootConfigId != undefined) {
                vm.isLoading = true;
                let shootQuery = DirectorDemandService.get({id: vm.shootConfigId});
                let projectQuery = ProjectManagementService.query().$promise;

                $.when(projectQuery, shootQuery).then(function (projects, configResp) {
                    vm.projects = projects;
                    vm.shootConfig = configResp;

                    ProjectManagementService.get({id: vm.shootConfig.projectId}, function (project) {
                        vm.project = project;
                        vm.projectId = vm.project.id;
                    });
                    vm.isLoading = false;
                }, function () {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                });
            } else {
                vm.shootConfig = {
                    items: [],
                };

                let projectQuery = ProjectManagementService.query(
                    function (projects) {
                        vm.projects = projects;
                        vm.isLoading = false;
                    }, function () {
                        PNotifyLoadFail();
                        vm.isLoading = false;
                    }
                );
            }
        }

        function cancel() {
            $state.go('^');
        }

        vm.sendComment = function () {
            var shootConfig = $.extend(true, {}, vm.shootConfig);
            shootConfig.comments.push({
                content: vm.newCommentText
            });
            DirectorDemandService.update(shootConfig, function (sc) {
                vm.shootConfig = sc;
                vm.newCommentText = "";
            }, function () {
                PNotifySaveFail();
            });

        };

        function save() {
            if (vm.isSaving) {
                return;
            }

            if (vm.shootConfig.items == undefined || vm.shootConfig.items.length <= 0) {
                PNotifyError("请先添加配置后再保存！");
                return;
            }

            vm.isSaving = true;

            var valid = $('.shootconfig-edit-content form').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                vm.isSaving = false;
                return;
            }

            vm.shootConfig.projectId = vm.project.id;

            if (vm.shootConfig.id != undefined) {
                DirectorDemandService.update(vm.shootConfig, onSaveSuccess, onSaveError);
            } else {
                DirectorDemandService.save(vm.shootConfig, onSaveSuccess, onSaveError)
            }

            function onSaveSuccess() {
                vm.isSaving = false;
                PNotifySaveSuccess();
                $state.go('^');
            }

            function onSaveError() {
                vm.isSaving = false;
                PNotifySaveFail();
            }
        }

        $scope.$watch('vm.projectId', function () {
            if (vm.projectId != undefined && vm.projects != undefined) {
                vm.project = vm.projects.find((item) => item.id == vm.projectId);
            } else {
                vm.project = {};
            }
        });

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });

            $('.new-config-row').parsley();
        };

        /****************************************** add/remove config item ****************************/

        vm.addNewConfig = function () {
            let valid = $('.new-config-row').parsley().validate();
            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (vm.shootConfig.items == undefined) {
                vm.shootConfig.items = [];
            }

            vm.shootConfig.items.push($.extend(true, {}, vm.newConfig));
            vm.newConfig = {};
        };

        vm.removeConfig = function (index) {
            if (index >= 0 && index < vm.shootConfig.items.length) {
                vm.shootConfig.items.splice(index, 1);
            }
        };
    }
})();
