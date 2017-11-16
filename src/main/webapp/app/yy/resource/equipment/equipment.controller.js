(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('EquipmentManageControler', EquipmentManageControler);

    EquipmentManageControler.$inject = ['$scope', '$rootScope', '$state', 'EquipmentManagementService', 'NgTableParams', 'DictionaryService'];

    function EquipmentManageControler ($scope, $rootScope, $state, EquipmentManagementService, NgTableParams, DictionaryService) {
        var vm = this;

        vm.searchParams = {};
        vm.equipmentsTableParams = $rootScope.defaultNgTableParams();
        vm.pageSize = vm.equipmentsTableParams.count();
        vm.equipments = [];
        DictionaryService.setOptions(vm);
        vm.isLoading = false;
        vm.search = search;
        search();

        $scope.$watch('vm.pageSize', function () {
            vm.equipmentsTableParams.count(vm.pageSize);
        });

        vm.deleteEquipment = function deleteEquipment(equipment) {

            bootboxConfirm("确认删除器材 <span class='text-aqua'>" + equipment.name + "</span> ?", function () {
                EquipmentManagementService.delete({id: equipment.id},
                    function () {
                        vm.search();
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        };

        function search() {
            console.log("Search with params", vm.searchParams);
            vm.isLoading = true;
            EquipmentManagementService.query($.extend(true,
                {
                    page: 0,
                    size: 10000000,
                    sort: ['name', 'category', 'vendor', 'id']
                },
                vm.searchParams
            ), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.equipments = data;
                vm.equipmentsTableParams.settings({
                    dataset: vm.equipments
                });
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }
    }
})();
