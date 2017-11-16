(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ContractIncomeController', ContractIncomeController);

    ContractIncomeController.$inject = ['$scope', '$state', '$rootScope', 'ContractManagementService', 'NgTableParams', 'DictionaryService', 'ContractIncomeService', 'OtherIncomeService'];

    function ContractIncomeController ($scope, $state, $rootScope, ContractManagementService, NgTableParams, DictionaryService, ContractIncomeService, OtherIncomeService) {
        var vm = this;

        vm.searchParams = {};
        vm.contractsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.contractsTableParams.count();
        vm.contracts = [];
        vm.income = {};
        vm.savingContract = {};
        vm.isLoading = false;
        vm.isFinalInstallment = false;
        vm.incomeStatusOptions = [];
        vm.search = search;

        init();

        $scope.$watch('vm.pageSize', function () {
            vm.contractsTableParams.count(vm.pageSize);
        });

        function init() {
            vm.isLoading = true;
            let payMethodQuery = DictionaryService.payMethodOptions();
            let approvalStatusQuery = DictionaryService.approvalStatusOptions();
            let incomeStatusQuery = DictionaryService.contractInstallmentStatusOptions();

            $.when(payMethodQuery, approvalStatusQuery, incomeStatusQuery).then(function (payMethodOptions, approvalStatusOptions, incomeStatusOptions) {
                vm.payMethodOptions = payMethodOptions;
                vm.approvalStatusOptions = approvalStatusOptions;
                vm.incomeStatusOptions = incomeStatusOptions;
                vm.allIncomeStatusOptions = [...incomeStatusOptions, ...approvalStatusOptions];
                search();
            });
        }

        function search() {
            vm.isLoading = true;

            let contractSearchParams = $.extend(true, {}, vm.searchParams);
            let incomeSearchParams = $.extend(true, {}, vm.searchParams);

            let ignoreContract = vm.incomeCategory == 2;
            let ignoreIncome = vm.incomeCategory == 1;

            if (vm.searchParams.paymentStatusName != undefined) {
                let option = vm.incomeStatusOptions.find((op) => op.name == contractSearchParams.paymentStatusName);
                if (option == undefined) {
                    ignoreContract = true;
                } else {
                    contractSearchParams.paymentStatusId = option.id;
                }

                incomeSearchParams.incomeTimeFrom = incomeSearchParams.payTimeFrom;
                incomeSearchParams.incomeTimeTo = incomeSearchParams.incomeTimeTo;
                option = vm.approvalStatusOptions.find((op) => op.name == incomeSearchParams.paymentStatusName);
                if (option == undefined) {
                    ignoreIncome = true;
                } else {
                    incomeSearchParams.incomeStatusId = option.id;
                }
            }

            let contractQuery = ContractManagementService.query(contractSearchParams).$promise;
            let incomeQuery = OtherIncomeService.query(incomeSearchParams).$promise;

            $.when(contractQuery, incomeQuery).then(function (contracts, incomes) {
                vm.contracts = contracts;
                vm.incomes = incomes;

                var totalIncome = 0;
                if (!ignoreContract) {
                    $.each(vm.contracts, function (index, contract) {
                        contract.category = '合同收入';

                        var payMethod = '';
                        var payMethodId = null;
                        $.each(contract.installments, function (i, ist) {
                            if (payMethod == '' && ist.payMethod != undefined && ist.payMethod.name != undefined) {
                                payMethod = ist.payMethod.name;
                                payMethodId = ist.payMethod.id;
                            }
                        });
                        contract.payMethod = payMethod;
                        contract.payMethodId = payMethodId;
                        contract.incomeInfo = '项目';
                        totalIncome += contract.moneyAmount || 0;
                    });

                    if (contractSearchParams.incomeMethodId != undefined && contractSearchParams.incomeMethodId != '') {
                        vm.contracts = vm.contracts.filter((c) => c.payMethodId == contractSearchParams.incomeMethodId)
                    }
                    if (contractSearchParams.paymentStatusId != undefined) {
                        vm.contracts = vm.contracts.filter((c) => c.paymentStatus.id == contractSearchParams.paymentStatusId)
                    }
                }

                if (!ignoreIncome) {
                    $.each(vm.incomes, function (index, income) {
                        income.category = '其他收入';
                        income.incomeInfo = income.incomeDesc;
                        income.moneyAmount = income.amount;
                        totalIncome += income.amount || 0;
                        income.payMethod = income.incomeMethodName;
                        income.nextInstallmentETA = income.incomeTime;
                        if (income.approvalRequest != undefined) {
                            income.paymentStatus = income.approvalRequest.status;
                            income.paymentRejectReason = income.approvalRequest.result;
                        }
                        income.isOtherIncome = true;
                    });

                    if (incomeSearchParams.incomeMethodId != undefined) {
                        vm.incomes = vm.incomes.filter((i) => i.incomeMethodId == incomeSearchParams.incomeMethodId);
                    }
                }

                vm.totalIncome = totalIncome;

                let ds = [];
                if (!ignoreContract) {
                    ds = vm.contracts;
                }
                if (!ignoreIncome) {
                    ds = [...ds, ...vm.incomes]
                }

                ds.sort((i1, i2) => {
                    if (i1.nextInstallmentETA == undefined) {
                        return 1;
                    }
                    if (i2.nextInstallmentETA == undefined) {
                        return -1;
                    }
                    return i2.nextInstallmentETA - i1.nextInstallmentETA;
                })

                vm.showingIncomes = ds;
                vm.contractsTableParams.settings({
                    dataset: ds
                });
                vm.isLoading = false;
            }, function () {
                PNotifySearchFail();
                vm.isLoading = false;
            });
        }

        vm.initSearchDateRangePicker = function () {
            $("#field_pay_time").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.searchParams.payTimeFrom = start.valueOf();
                    vm.searchParams.payTimeTo = end.valueOf();
                }
            );
        };

        /******************************** confirm income *****************************************/

        $('#field_income_time').datetimepicker({
            sideBySide: true,
            locale: 'zh-CN',
        }).on('dp.change', function (ev) {
            vm.payTime = moment(ev.date).valueOf();
        });

        const IncomeModalId = '#modal-income';
        vm.payNextInstallment = function (contract) {
            vm.income = {
                contractId: contract.id,
                payTimeText: moment().format(dateFormatDMY()),
            };
            vm.savingContract = contract;


            // If only one installment left, must pay all
            vm.isFinalInstallment = false;
            let installments = contract.installments;
            if (installments.filter((i) => i.actualAmount == null).length == 1) {
                vm.isFinalInstallment = true;
                let paidAmount = installments.filter((i) => i.actualAmount != undefined).map((i) => Number(i.actualAmount)).sum();
                vm.income.amount = contract.moneyAmount - paidAmount;
            }

            vm.payTime = moment().valueOf();

            $(IncomeModalId).modal("show");
        };

        $(IncomeModalId).on('shown.bs.modal', function (e) {
            // can't popup datetime picker on modal?
            $(IncomeModalId+' #field_income_time_container').datetimepicker({
                locale: 'zh-CN',
                format: dateFormatDMY()
            }).on('dp.change', function(ev){
                vm.income.actualPayTime = moment(ev.date).valueOf();
            });

            $(IncomeModalId+' form').parsley();
        });

        vm.saveNextInstallment = function () {
            let valid = $(IncomeModalId+' form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            ContractIncomeService.payNextInstallment(vm.income.contractId, {
                amount: vm.income.amount,
                payTime: vm.payTime
            }).then((contract) => {
                PNotifySaveSuccess();
                vm.search();
                vm.savingContract = null;
                $(IncomeModalId).modal("hide");
            }, () => {
                PNotifySaveFail();
            })
        }
    }
})();
