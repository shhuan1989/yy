(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('PerformanceBonusDetailController', PerformanceBonusDetailController);

    PerformanceBonusDetailController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'PerformanceApprovalRequest', 'ProjectManagementService', 'PerformanceApprovalService', 'UserService'];

    function PerformanceBonusDetailController ($scope, $rootScope, $state, $stateParams, PerformanceApprovalRequest, ProjectManagementService, PerformanceApprovalService, UserService) {
        var vm = this;
        vm.projectId = $stateParams.id;
        vm.project = {};
        vm.isLoading = true;
        vm.currentUserId = -1;
        vm.approval = {};
        vm.totalBonus = 0;

        vm.costs = [];
        vm.costTableParams = $rootScope.defaultNgTableParams();

        /*
         * [{
         *   dept: 'AE',
         *   total: 200,
         *   rowspan: 3
         *   name: 'messi',
         *   amount: 100.0
         *  }]
         *
         */
        vm.bonuses = [];



        UserService.currentUser().then(
            function (user) {
                vm.currentUserId = user != undefined ? user.id : -1;
                load();
            }
        );


        function load() {
            vm.isLoading = true;
            ProjectManagementService.get({id: vm.projectId},
                function (project) {
                    vm.project = project;

                    vm.project.bonusApproval = project.bonusApprovals == undefined ? {}
                        : project.bonusApprovals.find(function (item) {
                        return item.active == true;
                    });

                    var employees = [];
                    if (project.director) {
                        employees.push(project.director);
                    }
                    if (project.aes) {
                        employees = employees.concat(project.aes);
                    }
                    if (project.members) {
                        employees = employees.concat(project.members);
                    }

                    var empById = {};
                    $.each(employees, function (i, m) {
                        empById[m.id] = m;
                    });

                    // bonus detail
                    var bonusBydept = {};
                    $.each(vm.project.bonusApproval.bonuses, function (index, bonus) {
                        if (bonus && bonus.ownerId) {
                            var owner = empById[bonus.ownerId];
                            bonus.owner = owner;
                            if (owner) {
                                var dept = owner.dept == undefined ? '' : owner.dept.name;
                                if (bonusBydept[dept] == undefined) {
                                    bonusBydept[dept] = [];
                                }
                                bonusBydept[dept].push(bonus);
                            }
                        }
                    });

                    var bonuses = [];
                    $.each(bonusBydept, function (dept, bs) {
                        var total = 0;
                        if (notEmptyArray(bs)) {
                            var deptTotal = sum(bs.map(function (b) {
                                return Number(b.amount)
                            }));
                            total += deptTotal;

                            bonuses.push({
                                dept: dept,
                                rowspan: bs.length,
                                total: deptTotal,
                                name: bs[0].owner.name,
                                amount: bs[0].amount
                            });

                            $.each(bs.slice(1), function (i, b) {
                                bonuses.push({
                                    name: b.owner.name,
                                    amount: b.amount
                                })
                            });
                        }
                        vm.totalBonus += total;
                    });
                    vm.bonuses = bonuses;

                    // approval item
                    var p = vm.project.bonusApproval.approvalRoot;
                    while (p != undefined) {
                        if (p.status.id == 1 && p.approverId == vm.currentUserId) {
                            vm.approval = p;
                            break;
                        }
                        p = p.nextApproval;
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
        }
    }
})();
