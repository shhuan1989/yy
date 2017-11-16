(function() {
    'use strict';

    angular
        .module('yiyingOaApp')
        .controller('ActorRepoController', ActorRepoController);

    ActorRepoController.$inject = ['$scope', '$rootScope', '$state', 'ActorRepoService', 'PictureService', 'DictionaryService'];

    function ActorRepoController ($scope, $rootScope, $state, ActorRepoService, PictureService, DictionaryService) {
        var vm = this;

        vm.searchParams = {};
        DictionaryService.setOptions(vm);
        vm.actors = [];
        vm.totalPages = 0;
        vm.totalCount = 0;
        vm.currentPage = 0;
        vm.isLoading = false;
        vm.currentActor = {};
        vm.currentPhtoIndex = 0;
        vm.pageSize = 9;

        vm.search = search;
        vm.deleteActor = deleteActor;

        search(0);

        function deleteActor(actor) {

            bootboxConfirm("确认删除演员 <span class='text-aqua'>" + actor.name + "</span> ?", function () {
                ActorRepoService.delete({id: actor.id},
                    function () {
                        vm.search();
                        PNotifyDeleteSuccess();
                    },
                    function () {
                        PNotifyDeleteFail();
                    }
                );
            });
        }

        function search(page) {
            console.log("Search with params", vm.searchParams);
            vm.isLoading = true;
            ActorRepoService.query($.extend(true,
                {
                    page: page,
                    size: vm.pageSize,
                    sort: ['id']
                },
                vm.searchParams
            ), onSuccess, onError);

            function onSuccess(data, headers) {
                vm.actors = data.content;
                vm.totalCount = data.totalElements;
                vm.totalPages = data.totalPages;
                vm.isLoading = false;
            }
            function onError(error) {
                PNotifySearchFail();
                vm.isLoading = false;
            }
        }

        vm.goPage = function (page) {
            vm.currentPage = page;
        };

        vm.goPreviousPage = function () {
            if (vm.currentPage > 0) {
                vm.currentPage -= 1;
            }
        };

        vm.goNextPage = function () {
            if (vm.currentPage < vm.totalPages-1) {
                vm.currentPage += 1;
            }
        };

        $scope.$watch('vm.currentPage', function() {
            if (vm.currentPage >= 0 && vm.currentPage < vm.totalPages) {
                search(vm.currentPage);
            }
        });

        vm.showPhotos = function (actorId) {
            var links = [];
            var actor = vm.actors.find(function (item) {
                return item.id == actorId;
            });

            if (actor && actor.photos) {
                $.each(actor.photos, function (index, item) {
                    links.push({
                        href: 'content/images/placeholder_avatar.jpg'
                    })
                });
            }


            if (links.length <= 0) {
                PNotifyError('没有照片可以显示！');
                return;
            }
            vm.currentActor = actor;
            $('#photo-gallery').modal('show');
        };

        vm.previousPhoto = function () {
            if (vm.currentPhtoIndex > 0) {
                vm.currentPhtoIndex -= 1;
            }
        };

        vm.nextPhoto = function () {
            if (vm.currentActor && vm.currentActor.photos && vm.currentPhtoIndex < vm.currentActor.photos.length - 1) {
                vm.currentPhtoIndex += 1;
            }
        };

        vm.initSearchDateRangePicker = function () {
            $("#field_hire_date").daterangepicker(
                {
                    "locale": dateRangePickerLocale()
                },
                function (start, end) {
                    vm.searchParams.hireDateFrom = start.format('x');
                    vm.searchParams.hireDateTo = end.format('x');
                }
            );
        };

        vm.initJsComponents = function () {
            $('.select2').select2({
                language: "zh-CN"
            });

            $('#photo-gallery').on('shown.bs.modal', function () {
                var height = $(this).find('.modal-content').height() -120;
                $(this).find('.modal-body').outerHeight(height);
                console.log("set height", $(this), $(this).find('.modal-body'), $(this).height(), height);
            })
        };
    }
})();
