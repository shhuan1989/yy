(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ProjectRateController', ProjectRateController);

    ProjectRateController.$inject = ['$scope', '$state', '$timeout', '$stateParams', 'ProjectRate', 'Project', 'ProjectRatePrivilegeService'];

    function ProjectRateController ($scope, $state, $timeout, $stateParams, ProjectRate, Project, ProjectRatePrivilegeService) {
        var vm = this;
        console.log("ProjectRateController Initialized");
        vm.rate = {};
        vm.currentRate = {};
        vm.project = {};
        vm.postprogressMethod = 0;
        vm.ratableItems = [];
        vm.isFinalDecision = false;
        vm.isLoading = false;
        vm.rateOptions = ['A', 'B', 'C', 'D', 'E'];

        load();

        function load() {
            vm.isLoading = true;
            var rq = ProjectRate.get({id : $stateParams.id}).$promise;
            var pq = ProjectRatePrivilegeService.get().$promise;
            $.when(rq, pq).then(
                function (rqrsp, pqrsp) {
                    vm.rate = rqrsp;
                    vm.project = Project.get({id: vm.rate.projectId});
                    $.each(pqrsp, function (k, v) {
                        if (v == true) {
                            vm.ratableItems.push(k);
                            if (k == '终审') {
                                vm.isFinalDecision = true;
                                ProjectRate.query(
                                    {projectId: vm.rate.projectId, avg: true},
                                    function (rates) {
                                        vm.currentRate  = vm.rate;
                                        vm.rate = rates[0];
                                    },
                                    function () {
                                        PNotifyLoadFail();
                                    }
                                )
                            }
                        }
                    });
                    console.log(vm.ratableItems);

                    vm.isLoading = false;
                },
                function () {
                    PNotifyLoadFail();
                    vm.isLoading = false;
                }
            );
        }

        $scope.$watchGroup(['vm.rate.cutting', 'vm.rate.effect', 'vm.rate.graphic', 'vm.rate.music', 'vm.rate.threeDimension'],
            function () {
                var postprocess = Math.round(
                    (vm.rate.cutting + vm.rate.effect + vm.rate.graphic + vm.rate.music + vm.rate.threeDimension)
                    / 5
                );
                if (!isNaN(postprocess)) {
                    vm.rate.postprocess = postprocess;
                }
            }
        );

        // workaround for parsley on ng-if
        $scope.$watch('vm.isLoading', function () {
            $timeout(
                function () {
                    $('.rate-edit-content form').parsley();
                },
                500
            );
        });

        vm.setPostprocessRateMethod = function (val) {
            if (val == 0 || val == 1) {
                vm.postprogressMethod = val;
            }
        };

        vm.save = function () {
            var valid = true;
            var forms = $('.rate-edit-content form');
            if (forms != null) {
                if (forms.length > 1) {
                    $.each(forms.parsley(), function (i, p) {
                        if (!valid || !p.validate()) {
                            valid = false;
                        }
                    });
                } else {
                    valid = forms.parsley().validate();
                }
            }

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            bootboxConfirm("评级一旦不提交就<span class='text-red'>不能修改</span>，确定要提交对项目 <span class='text-aqua'>" + vm.rate.projectName + "</span> 的评级?", function () {
                if (vm.isFinalDecision == true) {
                    var avgRate = $.extend(true, {}, vm.rate);
                    var rate = $.extend(true, {}, vm.currentRate);
                    rate.finished = true;

                    var desc = {
                        'managementRe': 'managementDesc',
                        'creationRe': 'creationDesc',
                        'productionRe': 'productionDesc',
                        'directRe': 'directDesc',
                        'photographyRe': 'photographyDesc',
                        'postprocessRe': 'postprocessDesc',
                        'cuttingRe': 'cuttingDesc',
                        'effecRe': 'effecDesc',
                        'graphicRe': 'graphicDesc',
                        'musicRe': 'musicDesc',
                        'threeDimensionRe': 'threeDimensionDesc'
                    };

                    var scores = {
                        'A': 100,
                        'B': 89,
                        'C': 74,
                        'D': 59,
                        'E': -1
                    };

                    var scoreItems = ['management', 'creation', 'production', 'direct', 'photography', 'postprocess', 'cutting', 'effec', 'graphic', 'music', 'threeDimension'];

                    $.each(desc, function (s, t) {
                        if ( avgRate[s] != null) {
                            rate[t] = avgRate[s];
                            avgRate[t] = avgRate[s];
                        }
                    });

                    $.each(scoreItems, function (i, si) {
                        var d = si+'Desc';
                        if (rate[d]) {
                            rate[si] = scores[rate[d]];
                        }
                        if (avgRate[d]) {
                            avgRate[si] = scores[avgRate[d]];
                        }
                    });

                    var aq = ProjectRate.update(avgRate).$promise;
                    var rq = ProjectRate.update(rate).$promise;
                    $.when(aq, rq).then(
                        function () {
                            PNotifySaveSuccess();
                            $state.go('^');
                        },
                        function () {
                            PNotifySaveFail();
                        }
                    )

                } else {
                    var saveRate = $.extend(true, {}, vm.rate);
                    saveRate.finished = true;
                    ProjectRate.update(
                        saveRate,
                        function () {
                            PNotifySaveSuccess();
                            $state.go('^');
                        },
                        function () {
                            PNotifySaveFail();
                        }
                    )
                }
            });
        };
    }
})();
