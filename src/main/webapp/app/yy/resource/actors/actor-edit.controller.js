(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ActorEditController', ActorEditController);

    ActorEditController.$inject = ['$timeout', 'AuthServerProvider', '$scope', '$rootScope', '$state', '$stateParams', '$q', 'disableEdit', 'ActorRepoService', 'DictionaryService'];

    function ActorEditController ($timeout, AuthServerProvider, $scope, $rootScope, $state, $stateParams, $q, disableEdit, ActorRepoService, DictionaryService) {
        var vm = this;
        vm.initJsComponents = initJsComponents;

        vm.pageTitle = $state.current.data.pageTitle;
        vm.disableEdit = !(!disableEdit);
        DictionaryService.setOptions(vm);
        vm.isLoading = false;
        vm.actor = {photos:[]};

        vm.save = save;
        vm.cancel = cancel;
        vm.deletePhoto = deletePhoto;
        vm.load = load;

        vm.load();

        function load() {
            if ($stateParams.id != undefined) {
                vm.isLoading = true;
                ActorRepoService.get(
                    {id: $stateParams.id},
                    function (entity) {
                        vm.actor = entity;
                        if (!vm.actor.photos) {
                            vm.actor.photos = [];
                        }
                        vm.isLoading = false;
                        initDateTimePicker();
                    },
                    function () {
                        PNotifyLoadFail();
                        vm.isLoading = false;
                        initDateTimePicker();
                    }
                );
            } else {
                initDateTimePicker();
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function deletePhoto(index) {
            vm.actor.photos.splice(index, 1);
        }

        function cancel() {
            $state.go("^");
        }

        function save () {
            var valid = $('.actor-edit-content form').parsley().validate();

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            if (!vm.actor.birthDate || !moment(vm.actor.birthDate).isValid()) {
                vm.actor.birthDate = moment($('#field_birth_date').val(), dateFormatDMY()).valueOf();
            }

            if (vm.actor.birthDate) {
                vm.actor.age = moment().diff(moment(vm.actor.birthDate), 'years');
            }
            vm.isSaving = true;
            if (vm.actor.id !== null) {
                ActorRepoService.update(vm.actor, onSaveSuccess, onSaveError);
            } else {
                ActorRepoService.save(vm.actor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yiyingOaApp:actorUpdate', result);
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        function initDateTimePicker() {
            $('#field_birth_date').datetimepicker({
                format: dateFormatDMY(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.actor.birthDate)
            }).on('dp.change', function(ev){
                vm.actor.birthDate = moment(ev.date).format('x');
            });
        }

        function initJsComponents() {
            $('.actor-edit-content form').parsley();

            $('.select2').select2({
                language: "zh-CN"
            });

            initImageUploader(
                {
                    headers: {
                        "Authorization": 'Bearer ' + AuthServerProvider.getToken()
                    },
                    auto: true,
                    accept: {
                        title: 'Images',
                        extensions: 'gif,jpg,jpeg,bmp,png',
                        mimeTypes: 'image/*'
                    }
                },
                function (file, response) {
                    vm.actor.photos.push(response);
                }
            );
        }
    }
})();
