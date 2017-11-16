(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('IncomeEditController', IncomeEditController);

    IncomeEditController.$inject = ['$timeout', '$scope', '$rootScope', '$state', '$stateParams', '$q', 'ContractManagementService', 'FileService', 'DictionaryService'];

    function IncomeEditController ($timeout, $scope, $rootScope, $state, $stateParams, $q, ContractManagementService, FileService, DictionaryService) {
        const vm = this;
        vm.initJsComponents = initJsComponents;

        vm.isLoading = false;
        vm.contract = {
            taxRate: 3,
            installments: [
                {
                    amount: null,
                    eta: null
                }
            ],
            level: {
                id: 0
            }
        };
        DictionaryService.contractLevelOptions().then(function (options) {
            vm.contractLevelOptions = options;
        });
        DictionaryService.payMethodOptions().then(function (options) {
            vm.payMethodOptions = options;
        });

        vm.save = save;
        vm.cancel = cancel;
        vm.load = load;

        vm.load();

        function load() {
            if ($stateParams.id != null) {
                vm.isLoading = true;
                ContractManagementService.get(
                    {id: $stateParams.id},
                    function (entity) {
                        vm.contract = entity;
                        let installments = vm.contract.installments;
                        if (!notEmptyArray(installments)) {
                            installments = [{amount: vm.contract.moneyAmount}];
                            vm.contract.installments = installments;
                        }

                        installments.sort((a, b) => Number(a.eta)-Number(b.eta));

                        for (let i = 0; i < installments.length; i++) {
                            if (installments[i].actuanPayTime == undefined) {
                                initInstallmentTimePicker(i);
                            }
                        }
                        initTimePicker();

                        vm.isLoading = false;
                    },
                    function () {
                        PNotifyLoadFail();
                        vm.isLoading = false;
                    }
                )
            } else {
                initTimePicker();
                initInstallmentTimePicker(0);
            }
        }


        /************************** datetime picker *********************************/

        function initTimePicker() {
            $('#field_sign_date').datetimepicker({
                format: dateFormatDMY(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.contract.signTime)
            }).on('dp.change', function(ev){
                vm.contract.signTime = moment(ev.date).valueOf();
            });
        }

        function initInstallmentTimePicker(index) {
            $scope.$$postDigest(function() {
                $('#field_actual_time_'+index).datetimepicker({
                    format: dateFormatDMY(),
                    locale: 'zh-CN',
                    defaultDate: datetimePickerDefaultTime(vm.contract.installments[index].actualPayTime)
                }).on('dp.change', function(ev){
                    vm.contract.installments[index].actualPayTime = moment(ev.date).valueOf();
                });
            });
        }

        /************************** save/cancel *********************************/

        function cancel() {
            $state.go("^");
        }

        function save () {
            if (vm.isSaving) {
                return;
            }

            let valid = true;
            $.each($('.contract-edit-content form').parsley(), function (i, p) {
                if (!p.validate()) {
                    valid = false;
                }
            });

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            let installments = vm.contract.installments;
            if (Math.abs(installments.map((i) => Number(i.amount)).sum() - Number(vm.contract.moneyAmount)) > 0.000001) {
                PNotifyError('分期付款金额之和应该等于合同总额');
                return;
            }


            for (let i = 1; i < installments.length; i++) {
                if (moment(installments[i].eta).isBefore(moment(installments[i-1].eta))) {
                    PNotifyError('第'+(i+1)+'期付款时间应该在第'+i+'期之后');
                    return;
                }
            }


            vm.isSaving = true;
            if (vm.contract.id !== null) {
                ContractManagementService.update(vm.contract, onSaveSuccess, onSaveError);
            } else {
                ContractManagementService.save(vm.contract, onSaveSuccess, onSaveError);
            }

            function onSaveSuccess (result) {
                $scope.$emit('yiyingOaApp:contractUpdate', result);
                vm.isSaving = false;
                PNotifySaveSuccess();
                $state.go("^");
            }

            function onSaveError (resp) {
                vm.isSaving = false;

                let headers = resp.headers();

                if(headers['x-yy-error'] == 'error.idNumberExists') {
                    PNotifyError('保存失败，合同编号' + vm.contract.idNumber + ' 已经存在！');
                }
            }
        }


        /************************** events *********************************/

        $scope.$watchGroup(['vm.contract.taxRate', 'vm.contract.moneyAmount'], function () {
            if (!isNaN(vm.contract.moneyAmount) && !isNaN(vm.contract.taxRate)) {
                vm.contract.tax = (vm.contract.moneyAmount * vm.contract.taxRate / 100).toFixed(2);
            }
        });

        $scope.$watch('vm.contract.installments', () => {
            if (vm.contract.installments != undefined && vm.contract.installments.length > 1) {
                let installments = vm.contract.installments.slice(0, vm.contract.installments.length - 1);
                if (installments.filter((i) => i.amount == undefined).length <= 0) {
                    let installedSum = installments.map((i) => Number(i.amount)).sum();
                    const total = Number(vm.contract.moneyAmount);
                    if (installedSum < total) {
                        vm.contract.installments[vm.contract.installments.length-1].amount = total - installedSum;
                    }
                }
            }
        }, true);

        /************************** installments *********************************/

        vm.addNewInstallment = function () {
            let index = vm.contract.installments.length;
            vm.contract.installments.push({amount:null, eta:null});
            initInstallmentTimePicker(index);
        };

        vm.deleteInstallment = function (index) {
            vm.contract.installments.splice(index, 1);
        };

        /************************** attachments *********************************/

        vm.newFileSelected = function (event) {
            var files = event.target.files;
            if (files != null) {
                if (files[0].size > 20 * 1024 * 2014) {
                    PNotifyError("不能上传超过20MB的文件");
                    return;
                }

                waitingDialog.show('上传中...');
                var fd = new FormData();
                fd.append("file", files[0]);
                fd.append("name", files[0].name);
                FileService.save(
                    fd,
                    function (result) {
                        if (vm.contract.attachments == undefined) {
                            vm.contract.attachments = [];
                        }
                        vm.contract.attachments.push(result);
                        waitingDialog.hide();
                    },
                    function () {
                        PNotifyUploadFail();
                        waitingDialog.hide();
                    }
                )
            }
        };

        vm.deleteAttachment = function (index) {
            if (vm.contract.attachments != undefined) {
                vm.contract.attachments.splice(index, 1);
            }
        };

        vm.download = function (fileInfo) {
            FileService.get(
                {id: fileInfo.id},
                function (result) {
                    var file = new File([result.data], fileInfo.originName, {type: "application/octet-stream"});
                    saveAs(file);
                },
                function () {
                    PNotifyDownloadFail();
                }
            );
        };

        /************************** others*********************************/
        function initJsComponents() {
            $('.contract-edit-content form').parsley();
        }
    }
})();
