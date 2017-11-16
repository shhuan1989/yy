(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('PerformanceEditController', PerformanceEditController);

    PerformanceEditController.$inject = ['$timeout', '$scope', '$rootScope', '$state', '$stateParams', '$q', 'ProjectManagementService', 'ProjectRate', 'EmployeeService', 'PerformanceApprovalRequest'];

    function PerformanceEditController ($timeout, $scope, $rootScope, $state, $stateParams, $q, ProjectManagementService, ProjectRate, EmployeeService, PerformanceApprovalRequest) {
        var vm = this;

        vm.projectId = $stateParams.id;
        vm.pageTitle = $state.current.data.pageTitle;
        vm.isLoading = false;
        vm.project = {};
        vm.employeesNotIncluded = {};
        vm.employees = [];
        vm.employeesById = {};
        vm.rate = {};
        vm.newAuditor = null;
        vm.auditors = [];
        vm.isSaving = false;

        vm.membersByDept = {};
        vm.totalBonusByDept = {};
        vm.autoBonus = {};
        vm.bonus = {}; // id: moneyAmount
        vm.members = {}; //id: member
        vm.bonusRatio = 0.02;
        vm.depts = ['AE', '创意部', '制片部', '摄像部', '设计部'];
        vm.canEdit = $state.$current.toString() !== 'yy_perf_mgt.view';
        vm.workType = '0.03';
        vm.productionDifficulty = '0.005';
        vm.workTypeDesc = 'A';
        vm.productionDifficultyDesc = '一类';
        vm.addPostBonus = {};

        vm.load = load;
        vm.load();


        function load() {
            vm.isLoading = true;
            var rateQuyery = ProjectRate.query({projectId: vm.projectId, avg: true}).$promise;
            var projectQuery = ProjectManagementService.get({id: vm.projectId}).$promise;
            var empQuery = EmployeeService.query().$promise;

            $.when(rateQuyery, projectQuery, empQuery)
                .then(
                    function (rateResp, projectRsp, empResp) {
                        if (notEmptyArray(rateResp)) {
                            vm.rate = rateResp[0]
                        }
                        vm.project = projectRsp;
                        vm.employees = empResp;

                        $.each(vm.employees, function (index, employee) {
                            vm.employeesById[employee.id] = employee;
                        });

                        $.each(vm.project.members, function (i, member) {
                            var deptName = member.dept == undefined ? '' : member.dept.name;

                            if (deptName.startsWith('创意')) {
                                deptName = '创意部';
                            }
                            if (deptName != undefined) {
                                if (vm.membersByDept[deptName] == undefined) {
                                    vm.membersByDept[deptName] = [];
                                }
                                vm.membersByDept[deptName].push(member);
                            }
                            vm.members[member.id] = member;
                        });
                        $.each(vm.project.aes, function (i, member) {
                            vm.members[member.id] = member;
                            member.isAe = true;
                        });
                        vm.membersByDept['项目经理'] = vm.project.aes;

                        // existing approval
                        var existsApproval = null;
                        var bonusApprovals = vm.project.bonusApprovals;
                        if (bonusApprovals != undefined) {
                            existsApproval = bonusApprovals.find(function (approval) {
                                return approval.status.id == 0 || approval.status.id == 1;
                            });

                            if (existsApproval == undefined) {
                                bonusApprovals.sort((a, b) => {
                                    if (a.createTime == undefined) {
                                        return -1;
                                    } else if (b.createTime == undefined){
                                        return 1;
                                    }
                                    return a.createTime - b.createTime;
                                });
                                existsApproval = bonusApprovals[bonusApprovals.length - 1];
                            }
                        }

                        if (existsApproval != undefined) {
                            vm.canEdit = vm.canEdit && existsApproval.status.id == 0;
                            vm.workType = existsApproval.workType + "";
                            if (vm.workType == '0.03') {
                                vm.workTypeDesc = 'A'
                            } else if (vm.workType == '0.02') {
                                vm.workTypeDesc = 'B'
                            } else if (vm.workType == '0.01') {
                                vm.workTypeDesc = 'C'
                            }

                            vm.productionDifficulty = existsApproval.productionDifficulty + "";
                            if (vm.productionDifficulty == '0.005') {
                                vm.productionDifficultyDesc = '一类'
                            } else if (vm.productionDifficulty == '0.003') {
                                vm.productionDifficultyDesc = '二类'
                            } else if (vm.productionDifficulty == '0.002') {
                                vm.productionDifficultyDesc = '三类'
                            }

                            var bonuses = existsApproval.bonuses;
                            $.each(bonuses, function (i, bonus) {
                                vm.members[bonus.ownerId].workProportion = bonus.workProportion;
                                vm.bonus[bonus.ownerId] = bonus.amount;
                            });

                            var p = existsApproval.approvalRoot;
                            var approvers = [];
                            while (p != undefined) {
                                approvers.push($.extend(true, {approval: p}, vm.employeesById[p.approverId]));
                                p = p.nextApproval;
                            }
                            vm.auditors = approvers;
                        }
                        vm.isLoading = false;
                    },
                    function () {
                        PNotifyLoadFail();
                        vm.isLoading = false;
                    }
                );

        }

        vm.cancel = function () {
            $state.go("^");
        };

        vm.save = function () {
            var valid = true;

            $.each(vm.depts, function (index, dept) {
               var members = vm.membersByDept[dept];
                if (notEmptyArray(members)) {
                    var t = members.map(function (a) {
                        return Number(a.workProportion);
                    }).reduce(function (a, b) {
                        return a + b;
                    });
                    if (isNaN(t) || Math.abs(t - 100) > 0.0001) {
                        PNotifyError(dept + ' 的工作占比设置不正确，请确保工作占比之和是100');
                        valid = false;
                    }
                }
            });

            if (!notEmptyArray(vm.auditors)) {
                PNotifyErrorNeedApprover();
                valid = false;
            }

            if (!valid) {
                return;
            }


            vm.isSaving = true;
            var approvalRequest = {
                projectId: vm.projectId,
                bonuses: [],
                name: vm.project.name,
                approvalRoot: {}
            };

            var bonuses = $.extend(true, {}, vm.autoBonus);
            bonuses = $.extend(true, bonuses, vm.bonus);

            var workProportions = {};
            $.each(vm.membersByDept, function (dept, members) {
               $.each(members, function (idnex, member) {
                   workProportions[member.id] = member.workProportion;
               })
            });
            $.each(bonuses, function (k, v) {
                if (!isNaN(v)) {
                    var bonus = {
                        amount: v,
                        issued: false,
                        projectId: vm.projectId,
                        ownerId: k,
                        workProportion: workProportions[k]
                    };
                    approvalRequest.bonuses.push(bonus);
                }
            });

            approvalRequest.approvalRoot = approvalChainFromApprovers(vm.auditors);
            approvalRequest.workType = vm.workType;
            approvalRequest.productionDifficulty = vm.productionDifficulty;

            PerformanceApprovalRequest.save(approvalRequest,
                function () {
                    PNotifySaveSuccess();
                    vm.isSaving = false;
                    $state.go("^");
                },
                function () {
                    PNotifySaveFail();
                    vm.isSaving = false;
                }
            );
        };

        // workaround for parsley on ng-if
        $scope.$watch('vm.isLoading', function () {
            $timeout(
                function () {
                    $('.performance-edit-content form').parsley();
                },
                1000
            );
        });

        /****************************************** Bonus ******************************/

        function calBonus(member) {
            var p = member.workProportion;
            if (!isNaN(p) && p >= 0 && p <= 100) {
                if (member.dept) {
                    var quality = {
                        'A': 1.2,
                        'B': 1,
                        'C': 0.75,
                        'D': 0.3
                    };
                    member.employeeDeduction = member.employeeDeduction || 0;
                    if (member.isAe) {
                        if (vm.project.huaWei) {
                            if (vm.rate.managementDesc == 'E') {
                                vm.autoBonus[member.id] = (vm.project.bonusPool * member.employeeDeduction * p) / 100 - 500;
                            } else {
                                vm.autoBonus[member.id] = (vm.project.bonusPool * member.employeeDeduction * p * quality[vm.rate.managementDesc]) / 100;
                            }
                        } else {
                            if (vm.rate.managementDesc == 'E') {
                                vm.autoBonus[member.id] = (vm.project.bonusPool * vm.workType * p) / 100 - 500;
                            } else {
                                vm.autoBonus[member.id] = (vm.project.bonusPool * vm.workType * p * quality[vm.rate.managementDesc]) / 100;
                            }
                        }
                    } else if (member.dept.name.startsWith('创意')) {
                        if (vm.rate.creationDesc == 'E') {
                            vm.autoBonus[member.id] = (vm.project.bonusPool * member.employeeDeduction * p) / 100 - 500;
                        } else {
                            vm.autoBonus[member.id] = (vm.project.bonusPool * member.employeeDeduction * p * quality[vm.rate.creationDesc]) / 100;
                        }
                    } else if (member.dept.name.startsWith('制片')) {
                        if (vm.rate.productionDesc == 'E') {
                            vm.autoBonus[member.id] = (vm.project.bonusPool * vm.productionDifficulty * p) / 100 - 1000;
                        } else {
                            vm.autoBonus[member.id] = (vm.project.bonusPool * vm.productionDifficulty * p * quality[vm.rate.productionDesc]) / 100;
                        }
                    } else if (member.dept.name.startsWith('摄像')) {
                        var quality = {
                            'A': 0.012,
                            'B': 0.01,
                            'C': 0.005,
                            'D': 0.003
                        };
                        if (vm.rate.photographyDesc == 'E') {
                            vm.autoBonus[member.id] = (vm.project.bonusPool * p) / 100 - 1000;
                        } else {
                            vm.autoBonus[member.id] = (vm.project.bonusPool * p * quality[vm.rate.photographyDesc]) / 100;
                        }
                    } else if (member.dept.name.startsWith('设计')) {
                        var quality = {
                            'A': 0.05,
                            'B': 0.04,
                            'C': 0.02,
                            'D': 0.01,
                            'E': 0
                        };
                        var addBonus = vm.addPostBonus[member.id];
                        vm.autoBonus[member.id] = (vm.project.bonusPool * quality[vm.rate.postprocessDesc] + (addBonus || 0)) * p / 100;
                    }
                }
            } else {
                vm.autoBonus[member.id] = "";
            }
        }

        function bonusOfMember(member) {
            if (!isNaN(vm.bonus[member.id])) {
                return parseFloat(vm.bonus[member.id]);
            } else if (!isNaN(vm.autoBonus[member.id])){
                return parseFloat(vm.autoBonus[member.id]);
            }
            return 0;
        }

        function totalBonusOfMembers(members) {
            if (members == undefined) {
                return 0;
            }
            var total = 0.0;

            var totalPortion = 0;
            $.each(members, function (index, member) {
               totalPortion += Number(member.workProportion);
            });

            $.each(members, function (index, member) {
                total += bonusOfMember(member);
            });
            return total;
        }

        function calTotalBonus() {
            $.each(vm.membersByDept, function (dept, members) {
                $.each(members, function (index, member) {
                    calBonus(member);
                })
            });

            var totalBonusByDept = {};
            $.each(vm.membersByDept, function (dept, members) {
                totalBonusByDept[dept] = totalBonusOfMembers(members);
            });

            vm.totalBonusByDept = totalBonusByDept;
        }

        function autoSetLastWorkProportion() {
            $.each(vm.membersByDept, function (dept, members) {
                var count = members.filter(function (member) {
                    return isNaN(member.workProportion)
                }).length;

                if (count === 1) {
                    var totalWp = members.map((member) => {
                        if (isNaN(member.workProportion)) {
                            return 0 ;
                        } else {
                            return Number(member.workProportion);
                        }
                    }).sum();
                    $.each(members, function (index, member) {
                        if (isNaN(member.workProportion)) {
                            member.workProportion = 100 - totalWp;
                        }
                    })
                }
            });
        }

        $scope.$watch('vm.membersByDept', function () {
            calTotalBonus();
        }, true);

        $scope.$watch('vm.bonus', function () {
            calTotalBonus();
        }, true);

        $scope.$watch('vm.autoBonus', function () {
            calTotalBonus();
        }, true);

        $scope.$watch('vm.addPostBonus', function () {
            calTotalBonus();
        }, true);

        $scope.$watchGroup(['vm.productionDifficulty', 'vm.workType'], function () {
            calTotalBonus();
        },true);
        /****************************************** Auditor ****************************/

        $('#modal-add-auditor').on('shown.bs.modal', function (e) {

            vm.employeesNotIncluded = $.grep(vm.employees, function (item) {
                var exists = vm.auditors.find(function (exitem) {
                    return exitem.id == item.id;
                });
                return !exists;
            });

            $('.select2').select2({
                language: "zh-CN"
            });
        });

        vm.addNewAuditor = function () {
            $('#modal-add-auditor').modal('show');
        };

        vm.saveNewAuditor = function () {
            if (vm.newAuditor != undefined) {
                vm.auditors.push(vm.newAuditor);
                vm.newAuditor = null;
            }
            $('#modal-add-auditor').modal('hide');
        };

        vm.deleteAuditor = function (auditor) {
            vm.auditors = vm.auditors.filter(function (item) {
                return item.id != auditor.id;
            });
        };

    }
})();
