(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('AdminNoticeViewController', AdminNoticeViewController);

    AdminNoticeViewController.$inject = ['$timeout', '$sce', '$scope', '$stateParams', '$state', 'AdminNoticeService', 'NoticeChat', 'FileService'];

    function AdminNoticeViewController ($timeout, $sce, $scope, $stateParams, $state, AdminNoticeService, NoticeChat, FileService) {
        let vm = this;
        vm.notice = {};
        vm.isLoading = false;
        vm.noticeId = $stateParams.id;
        vm.pageTitle = $state.current.data.pageTitle;
        vm.disableEdit = true;
        vm.newCommentText = null;
        vm.newFileInfoId = null;
        vm.newPictureInfoId = null;
        vm.comments = [];

        vm.cancel = cancel;
        vm.load = load;

        vm.load();

        function load() {
            vm.isLoading = true;

            if (vm.noticeId != undefined) {
                var noticeQuery = AdminNoticeService.get({id: vm.noticeId}, function (notice) {
                    vm.notice = notice;
                    vm.comments = notice.comments;
                    vm.isLoading = false;
                }, function () {
                    vm.isLoading = false;
                    PNotifyLoadFail();
                });
            } else {
                vm.isLoading = false;
                PNotifyLoadFail();
            }
        }

        function cancel() {
            $state.go("^");
        }

        vm.sendComment = function () {
            if ((vm.newCommentText != undefined && !/^\s*$/.test(vm.newCommentText))
                || vm.newFileInfoId != null || vm.newPictureInfoId != null) {

                NoticeChat.save(
                    {
                        text: vm.newCommentText,
                        fileInfo: {
                            id: vm.newFileInfoId
                        },
                        pictureInfo: {
                            id: vm.newPictureInfoId
                        },
                        noticeId: vm.noticeId
                    },
                    function (result) {
                        vm.newCommentText = "";
                        vm.newFileInfoId = null;
                        vm.newPictureInfoId = null;
                        vm.comments.push(result);
                    },
                    function () {
                        PNotifyError("发送失败，请重试！")
                    }
                )
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
                        vm.newFile = null;
                        if (result.type == 'IMAGE') {
                            vm.newPictureInfoId = result.id;
                        } else {
                            vm.newFileInfoId = result.id;
                        }
                        vm.sendComment();
                        waitingDialog.hide();
                    },
                    function () {
                        PNotifyUploadFail();
                        waitingDialog.hide();
                    }
                )
            }
        };

        $scope.$watch('vm.comments', function () {
                var attachments = [];
                $.each(vm.comments, function (index, item) {
                    if (item.fileInfo != null && item.fileInfo.id != null) {
                        attachments.push(item.fileInfo)
                    }
                    if (item.pictureInfo != null && item.pictureInfo.id != null) {
                        attachments.push(item.pictureInfo)
                    }

                    if (item.text != null && item.formattedText == undefined) {
                        item.formattedText = $sce.trustAsHtml(item.text.autoLink())
                    }

                });
                attachments.sort(function (a, b) {
                    return a.createTime - b.createTime;
                });
                vm.attachments = attachments;
            },
            true
        );
    }
})();
