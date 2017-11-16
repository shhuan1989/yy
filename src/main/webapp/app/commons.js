function filterOutAllInopions(options) {
    return options.filter(function (item) {
        return item.name !== "全部";
    });
}

function isMobileBrowser() {
    var a = navigator.userAgent||navigator.vendor||window.opera;
    return /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))
}

function rgb2hex(rgb) {
    if (rgb == undefined) {
        return '';
    }

    var r = /\s*\d+\s*,\s*\d+\s*,\s*\d+\s*/;
    var matches = r.exec(rgb);
    if (matches != undefined && matches.length > 0) {
        var m = matches[0];
        var digits = m.split(',');
        var result = '#';
        for (let d of digits) {
            let cd = Number(d.trim()).toString(16);
            if (cd.length == 1) {
                cd = '0'+cd;
            }
            result += cd;
        }
        return result;
    }

    return '';
}

function groupShootConfig(configItems) {
    // config items
    var items = {};
    $.each(configItems, function (index, item) {
        if (items[item.cat1] == undefined) {
            items[item.cat1] = {};
        }
        let cat2 = item.cat2 != undefined ? item.cat2 : '';
        if (items[item.cat1][cat2] == undefined) {
            items[item.cat1][cat2] = [];
        }
        items[item.cat1][cat2].push(item);
    });

    var result = [];
    $.each(items, function (cat1, c1items) {
        var citem = {};
        citem.cat1 = cat1;
        var count = 0;
        var sitems = [];
        $.each(c1items, function (cat2, c2items) {
            var c2count = 0;
            $.each(c2items, function (i, item) {
                count += 1;
                c2count += 1;
                if (count == 1) {
                    citem.cat2 = cat2;
                    citem.name = item.name;
                    citem.amount = item.amount;
                    citem.days = item.days;
                } else {
                    if (c2count == 1) {
                        sitems.push({
                            name: item.name,
                            amount: item.amount,
                            days: item.days,
                            cat2: cat2,
                            c2rowspan: c2items.length,
                        });
                    } else {
                        sitems.push({
                            name: item.name,
                            amount: item.amount,
                            days: item.days,
                        });
                    }
                }
            });
            if (citem.c2rowspan == undefined) {
                citem.c2rowspan = c2count;
            }
        });
        citem.c1rowspan = count;
        sitems.splice(0, 0, citem);
        result = [...result, ...sitems];
    });

    return result;
}

function calCatCostReturnTotalCost(configItems) {
    let totalCost = 0;
    for (let i = 0; i < configItems.length; i++) {
        let item = configItems[i];
        let costi = item.actualCost || item.cost || 0;
        totalCost += costi;
        if (item.c2rowspan != undefined) {
            let catCost = costi;
            for (let j = i+1; j < configItems.length; j++) {
                if (configItems[j].c2rowspan != undefined) {
                    break;
                }
                catCost += configItems[j].actualCost || configItems[j].cost || 0;
            }
            item.catCost = catCost;
        }
    }

    return totalCost;
}

function groupedConfigItems2ConfigItems(groupedConfigItems) {
    if (groupedConfigItems == undefined) {
        return [];
    }

    let cat1 = '';
    let cat2 = '';
    let configItems = [];
    for (let i = 0; i < groupedConfigItems.length; i++) {
        let item = groupedConfigItems[i];
        if (item.cat1 != undefined && item.cat1 != '') {
            cat1 = item.cat1;
        }
        if (item.cat2 != undefined && item.cat2 != '') {
            cat1 = item.cat2;
        }
        configItems.push({
            name: item.name,
            id: item.id,
            cat1: cat1,
            cat2: cat2,
            amount: item.amount,
            actualAmount: item.actualAmount,
            days: item.days,
            actualDays: item.days,
            unitPrice: item.unitPrice,
            cost: item.cost,
            actualCost: item.actualCost,
            defaultUnitPrice: item.defaultUnitPrice,
            vendor: item.vendor,
            paymentStatusVal: item.paymentStatusVal
        })
    }

    return configItems;
}
function groupShootBudget(configItems, equipmentsByName) {
    // config items
    var items = {};

    // group items by cat1 and cat2
    $.each(configItems, function (index, item) {
        if (items[item.cat1] == undefined) {
            items[item.cat1] = {};
        }
        let cat2 = item.cat2 != undefined ? item.cat2 : '';
        if (items[item.cat1][cat2] == undefined) {
            items[item.cat1][cat2] = [];
        }
        items[item.cat1][cat2].push(item);
    });

    var result = [];
    var catCost = 0;
    $.each(items, function (cat1, c1items) {
        var citem = {};
        citem.cat1 = cat1;
        var count = 0; // total item count
        var sitems = [];
        $.each(c1items, function (cat2, c2items) {
            var c2count = 0; // total item count in category 2
            catCost = 0;
            $.each(c2items, function (i, item) {
                count += 1;
                c2count += 1;
                if (item.actualCost == undefined) {
                    item.acutalCost = item.cost;
                }
                catCost += item.actualCost || 0;
                let equipment = null;
                if (equipmentsByName != undefined) {
                    equipment = equipmentsByName[item.name];
                }
                if (count == 1) {
                    citem.cat2 = cat2;
                    citem.name = item.name;
                    citem.id = item.id;
                    citem.equipments = equipment;
                    citem.amount = item.amount;
                    citem.actualAmount = item.actualAmount;
                    citem.days = item.days;
                    citem.actualDays = item.actualDays;
                    citem.unitPrice = item.unitPrice;
                    citem.cost = item.cost;
                    citem.actualCost = item.actualCost;
                    citem.catCost = catCost;
                    citem.vendor = item.vendor;
                    citem.paymentStatus = item.paymentStatus;
                } else {
                    if (c2count == 1) {
                        sitems.push({
                            name: item.name,
                            id: item.id,
                            equipments: equipment,
                            amount: item.amount,
                            actualAmount: item.actualAmount,
                            days: item.days,
                            actualDays: item.actualDays,
                            cat2: cat2,
                            c2rowspan: c2items.length,
                            unitPrice: item.unitPrice,
                            cost: item.cost,
                            actualCost: item.actualCost,
                            catCost: catCost,
                            vendor: item.vendor,
                            paymentStatus: item.paymentStatus
                        });
                    } else {
                        sitems.push({
                            name: item.name,
                            id: item.id,
                            equipments: equipment,
                            amount: item.amount,
                            actualAmount: item.actualAmount,
                            days: item.days,
                            actualDays: item.actualDays,
                            unitPrice: item.unitPrice,
                            cost: item.cost,
                            actualCost: item.actualCost,
                            catCost: catCost,
                            vendor: item.vendor,
                            paymentStatus: item.paymentStatus
                        });
                    }
                }
            });
            if (citem.c2rowspan == undefined) {
                citem.c2rowspan = c2count;
            }
        });
        citem.c1rowspan = count;
        sitems.splice(0, 0, citem);
        result = [...result, ...sitems];
    });

    return result;
}


function bootboxConfirmReject(callback) {
    bootbox.prompt({
        title: "请输入驳回的理由",
        buttons: {
            confirm: {
                label: '确定',
            },
            cancel: {
                label: '取消',
            }
        },
        inputType: 'textarea',
        callback: function (result) {
            if (result != undefined && result !== '') {
                callback(result);
            } else {
                PNotifyError("请填入驳回理由");
            }
        }
    });
}

function bootboxConfirm(message, callback) {
    bootbox.confirm({
        title: "确认信息",
        message: message,
        buttons: {
            confirm: {
                label: '确定',
                // className: 'btn-danger'
            },
            cancel: {
                label: '取消',
                // className: 'btn-success'
            }
        },
        callback: function (result) {
            if (result) {
                callback();
            }
        }
    });
}

function dateFormatDMY() {
    return 'YYYY-MM-DD';
}

function dateFormatDmyHms() {
    return 'YYYY-MM-DD HH:mm:ss';
}

function formatTimestampDMY(t) {
    return moment(t).format(dateFormatDMY());
}

function datetimePickerDefaultTime(timestamp) {
    if (timestamp == undefined || isNaN(timestamp)) {
        return false;
    }

    return moment(Number(timestamp));
}

function formatTimestampDmyHms(t) {
    return moment(t).format(dateFormatDmyHms());
}

function dateRangePickerLocale() {
    return {
        "format": "YYYY年MM月DD日",
        "separator": " - ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "从",
        "toLabel": "至",
        "weekLabel": "周",
        "daysOfWeek": [
            "日",
            "一",
            "二",
            "三",
            "四",
            "五",
            "六"
        ],
        "monthNames": [
            "一月",
            "二月",
            "三月",
            "四月",
            "五月",
            "六月",
            "七月",
            "八月",
            "九月",
            "十月",
            "十一月",
            "十二月"
        ],
        "firstDay": 1
    };
}

function approvalChainFromApprovers(approvers) {
    var approvalHead = null;
    var approvalTail = null;
    $.each(approvers, function (index, auditor) {
        var approval = {
            approverId: auditor.id
        };
        if (approvalHead == undefined) {
            approvalHead = approval;
            approvalTail = approval;
        } else  {
            approvalTail.nextApproval = approval;
            approvalTail = approval;
        }
    });

    return approvalHead;
}

function approvalChainToApprovers(approvalRoot, employeesById) {
    let approvers = [];
    let p = approvalRoot;
    while (p) {
        let approver = employeesById == undefined ? null : employeesById[p.approverId];
        if (approver != undefined) {
            approvers.push($.extend(true, {approval: p}, approver));
        } else {
            approvers.push($.extend(true, {approval: p}, {
                id: p.approverId,
            }));
        }
        p = p.nextApproval;
    }

    return approvers;
}


function currentApproval(approvalItem, currentUserId) {
    if (approvalItem && approvalItem.approvalRequest) {
        let p = approvalItem.approvalRequest.approvalRoot;
        while (p) {
            if (p.approverId == currentUserId) {
                return p;
            }
            p = p.nextApproval;
        }
    }

    return {};
}

function sum(array) {
    if (array == undefined) {
        return 0;
    }

    return array.reduce(function (a, b) {
        return Number(a) + Number(b);
    }, 0);
}

function notEmptyArray(arr) {
    return arr != undefined && arr.length > 0;
}

function isNaN(o) {
    if (o == undefined || /^\s*$/.test(o)) {
        return true;
    }

    return o !== o;
}

function formatHour(hour) {
    if (hour < 0) {
        return '-' + formatHour(0-hour);
    }
    if (hour < 24) {
        return hour + ' 小时';
    } else if (hour < 7 * 24) {
        return Math.round(hour / 24) + ' 天';
    } else if (hour < 4 * 7 *24) {
        return Math.round(hour / 24 / 7) + ' 周';
    } else if (hour < 365 *24) {
        return Math.round(hour / 24 / 30) + ' 月';
    } else {
        return Math.round(hour / 24 / 365) + ' 年';
    }
}

function labelClassForTimeLeftInHour(timeLeftInHour) {
    if (timeLeftInHour != null) {
        if (timeLeftInHour < 24) {
            return 'label-danger';
        } else if (timeLeftInHour < 24 * 2) {
            return 'label-warning';
        } else {
            return 'label-info';
        }
    }
    return '';
}

function labelClassForEtaTime(timeInUnixTimeStamp) {
    var t = moment(timeInUnixTimeStamp);
    if (!t.isValid()) {
        return '';
    }

    var now = moment();
    var timeLeftInHour = t.diff(now, 'hours');
    return labelClassForTimeLeftInHour(timeLeftInHour);
}

function labelClassForTimeElapsedInHour(timeLeftInHour) {
    if (timeLeftInHour != null) {
        if (timeLeftInHour < 24) {
            return 'label-info';
        } else if (timeLeftInHour < 24 * 2) {
            return 'label-warning';
        } else {
            return 'label-danger';
        }
    }
    return '';
}

function labelClassForTime(timeInUnixTimeStamp) {
    var t = moment(timeInUnixTimeStamp);
    if (!t.isValid()) {
        return '';
    }

    var now = moment();
    var timeLeftInHour = now.diff(t, 'hours');
    return labelClassForTimeElapsedInHour(timeLeftInHour);
}



function PNotifyInfo(message) {
    if (message) {
        return new PNotify({
            title: '提示信息',
            text: message,
            type: 'info',
            icon: 'fa fa-info-circle'
        });
    }
}

function PNotifySuccess(message) {
    if (message) {
        return new PNotify({
            title: '提示信息',
            text: message,
            type: 'success',
            icon: 'fa fa-info-circle'
        });
    }
}

function PNotifyWarn(message) {
    if (message) {
        return new PNotify({
            title: '警告信息',
            text: message,
            type: 'warn',
            icon: 'fa fa-exclamation-triangle'
        });
    }
}

function PNotifyError(message) {
    if (message) {
        return new PNotify({
            title: '错误信息',
            text: message,
            type: 'error',
            icon: 'fa fa-times-circle-o'
        });
    }
}

function PNotifyInternalError() {
    return PNotifyError("内部错误！");
}

function PNotifyDownloadFail() {
    return PNotifyError("下载失败！");
}

function PNotifyUploadFail() {
    return PNotifyError("上传失败！");
}

function PNotifyUploadSuccess() {
    return PNotifyError("上传成功！");
}

function PNotifyLoadFail() {
    return PNotifyError("加载失败！");
}

function PNotifySearchFail() {
    return PNotifyError("查询失败！");
}

function PNotifyInvalidInput() {
    return PNotifyError('请检查录入项是否正确！');
}

function PNotifySaveSuccess() {
    return PNotifySuccess('保存成功！');
}

function PNotifySaveFail() {
    return PNotifyError('保存失败！');
}

function PNotifyDeleteSuccess() {
    return PNotifySuccess('删除成功！');
}

function PNotifyDeleteFail() {
    return PNotifyError('删除失败！');
}

function PNotifyApproveSuccess() {
    return PNotifySuccess("审核通过！");
}

function PNotifyRejectSuccess() {
    return PNotifySuccess("驳回申请成功！");
}


function PNotifyApproveFail() {
    return PNotifySuccess("审核失败！");
}

function PNotifyErrorNeedApprover() {
    return PNotifyError('请指定审批领导');
}

function PNotifyTimeRangeError() {
    return PNotifyError('开始时间应当在结束时间之前！');
}


function initImageUploader(options, successHandler, errorHandler) {
    var $wrap = $('#uploader'),

        // 图片容器
        $queue = $( '<ul class="filelist"></ul>' )
            .appendTo( $wrap.find( '.queueList' ) ),

        // 状态栏，包括进度和控制按钮
        $statusBar = $wrap.find( '.statusBar' ),

        // 文件总体选择信息。
        $info = $statusBar.find( '.info' ),

        // 上传按钮
        $upload = $wrap.find( '.uploadBtn' ),

        // 没选择文件之前的内容。
        $placeHolder = $wrap.find( '.placeholder' ),

        $progress = $statusBar.find( '.progress' ).hide(),

        // 添加的文件数量
        fileCount = 0,

        // 添加的文件总大小
        fileSize = 0,

        // 优化retina, 在retina下这个值是2
        ratio = window.devicePixelRatio || 1,

        // 缩略图大小
        thumbnailWidth = 110 * ratio,
        thumbnailHeight = 110 * ratio,

        // 可能有pedding, ready, uploading, confirm, done.
        state = 'pedding',

        // 所有文件的进度信息，key为file id
        percentages = {},
        // 判断浏览器是否支持图片的base64
        isSupportBase64 = ( function() {
            var data = new Image();
            var support = true;
            data.onload = data.onerror = function() {
                if( this.width != 1 || this.height != 1 ) {
                    support = false;
                }
            }
            data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
            return support;
        } )(),

        // 检测是否已经安装flash，检测flash的版本
        flashVersion = ( function() {
            var version;

            try {
                version = navigator.plugins[ 'Shockwave Flash' ];
                version = version.description;
            } catch ( ex ) {
                try {
                    version = new ActiveXObject('ShockwaveFlash.ShockwaveFlash')
                        .GetVariable('$version');
                } catch ( ex2 ) {
                    version = '0.0';
                }
            }
            version = version.match( /\d+/g );
            return parseFloat( version[ 0 ] + '.' + version[ 1 ], 10 );
        } )(),

        supportTransition = (function(){
            var s = document.createElement('p').style,
                r = 'transition' in s ||
                    'WebkitTransition' in s ||
                    'MozTransition' in s ||
                    'msTransition' in s ||
                    'OTransition' in s;
            s = null;
            return r;
        })(),

        // WebUploader实例
        uploader;

    if (options.auto) {
        $upload.addClass( 'element-invisible' );
    }

    if ( !WebUploader.Uploader.support('flash') && WebUploader.browser.ie ) {

        // flash 安装了但是版本过低。
        if (flashVersion) {
            (function(container) {
                window['expressinstallcallback'] = function( state ) {
                    switch(state) {
                        case 'Download.Cancelled':
                            alert('您取消了更新！')
                            break;

                        case 'Download.Failed':
                            alert('安装失败')
                            break;

                        default:
                            alert('安装已成功，请刷新！');
                            break;
                    }
                    delete window['expressinstallcallback'];
                };

                var swf = './expressInstall.swf';
                // insert flash object
                var html = '<object type="application/' +
                    'x-shockwave-flash" data="' +  swf + '" ';

                if (WebUploader.browser.ie) {
                    html += 'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ';
                }

                html += 'width="100%" height="100%" style="outline:0">'  +
                    '<param name="movie" value="' + swf + '" />' +
                    '<param name="wmode" value="transparent" />' +
                    '<param name="allowscriptaccess" value="always" />' +
                    '</object>';

                container.html(html);

            })($wrap);

            // 压根就没有安转。
        } else {
            $wrap.html('<a href="http://www.adobe.com/go/getflashplayer" target="_blank" border="0"><img alt="get flash player" src="http://www.adobe.com/macromedia/style_guide/images/160x41_Get_Flash_Player.jpg" /></a>');
        }

        return;
    } else if (!WebUploader.Uploader.support()) {
        alert( 'Web Uploader 不支持您的浏览器！');
        return;
    }

    // 实例化
    uploader = WebUploader.create(
        $.extend(true, options,
            {
                pick: {
                    id: '#filePicker',
                    label: '点击选择图片'
                },
                formData: {
                    uid: 123
                },
                dnd: '#dndArea',
                paste: '#uploader',
                swf: '../../dist/Uploader.swf',
                chunked: false,
                chunkSize: 512 * 1024,
                server: 'resource/pictures',
                // runtimeOrder: 'flash',

                // accept: {
                //     title: 'Images',
                //     extensions: 'gif,jpg,jpeg,bmp,png',
                //     mimeTypes: 'image/*'
                // },

                // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
                disableGlobalDnd: true,
                fileNumLimit: 100,
                fileSizeLimit: 200 * 1024 * 1024,    // 200 M
                fileSingleSizeLimit: 50 * 1024 * 1024    // 50 M
            }
        ));

    // 拖拽时不接受 js, txt 文件。
    uploader.on( 'dndAccept', function( items ) {
        var denied = false,
            len = items.length,
            i = 0,
            // 修改js类型
            unAllowed = 'text/plain;application/javascript ';

        for ( ; i < len; i++ ) {
            // 如果在列表里面
            if ( ~unAllowed.indexOf( items[ i ].type ) ) {
                denied = true;
                break;
            }
        }

        return !denied;
    });

    uploader.on('dialogOpen', function() {
        console.log('here');
    });

    // uploader.on('filesQueued', function() {
    //     uploader.sort(function( a, b ) {
    //         if ( a.name < b.name )
    //           return -1;
    //         if ( a.name > b.name )
    //           return 1;
    //         return 0;
    //     });
    // });

    // 添加“添加文件”的按钮，
    uploader.addButton({
        id: '#filePicker2',
        label: '继续添加'
    });

    uploader.on('ready', function() {
        window.uploader = uploader;
    });

    // 当有文件添加进来时执行，负责view的创建
    function addFile( file ) {
        var $li = $( '<li id="' + file.id + '">' +
                '<p class="title">' + file.name + '</p>' +
                '<p class="imgWrap"></p>'+
                '<p class="progress"><span></span></p>' +
                '</li>' ),

            $btns = $('<div class="file-panel">' +
                '<span class="cancel">删除</span>' +
                '<span class="rotateRight">向右旋转</span>' +
                '<span class="rotateLeft">向左旋转</span></div>').appendTo( $li ),
            $prgress = $li.find('p.progress span'),
            $wrap = $li.find( 'p.imgWrap' ),
            $info = $('<p class="error"></p>'),

            showError = function( code ) {
                switch( code ) {
                    case 'exceed_size':
                        text = '文件大小超出';
                        break;

                    case 'interrupt':
                        text = '上传暂停';
                        break;

                    default:
                        text = '上传失败，请重试';
                        break;
                }

                $info.text( text ).appendTo( $li );
            };

        if ( file.getStatus() === 'invalid' ) {
            showError( file.statusText );
        } else {
            // @todo lazyload
            $wrap.text( '预览中' );
            uploader.makeThumb( file, function( error, src ) {
                var img;

                if ( error ) {
                    $wrap.text( '不能预览' );
                    return;
                }

                if( isSupportBase64 ) {
                    img = $('<img src="'+src+'">');
                    $wrap.empty().append( img );
                } else {
                    $.ajax('../../server/preview.php', {
                        method: 'POST',
                        data: src,
                        dataType:'json'
                    }).done(function( response ) {
                        if (response.result) {
                            img = $('<img src="'+response.result+'">');
                            $wrap.empty().append( img );
                        } else {
                            $wrap.text("预览出错");
                        }
                    });
                }
            }, thumbnailWidth, thumbnailHeight );

            percentages[ file.id ] = [ file.size, 0 ];
            file.rotation = 0;
        }

        file.on('statuschange', function( cur, prev ) {
            if ( prev === 'progress' ) {
                $prgress.hide().width(0);
            }

            // 成功
            if ( cur === 'error' || cur === 'invalid' ) {
                console.log( file.statusText );
                showError( file.statusText );
                percentages[ file.id ][ 1 ] = 1;
            } else if ( cur === 'interrupt' ) {
                showError( 'interrupt' );
            } else if ( cur === 'queued' ) {
                $info.remove();
                $prgress.css('display', 'block');
                percentages[ file.id ][ 1 ] = 0;
            } else if ( cur === 'progress' ) {
                $info.remove();
                $prgress.css('display', 'block');
            } else if ( cur === 'complete' ) {
                $prgress.hide().width(0);
                $li.append( '<span class="success"></span>' );
            }

            $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
        });

        $li.on( 'mouseenter', function() {
            $btns.stop().animate({height: 30});
        });

        $li.on( 'mouseleave', function() {
            $btns.stop().animate({height: 0});
        });

        $btns.on( 'click', 'span', function() {
            var index = $(this).index(),
                deg;

            switch ( index ) {
                case 0:
                    uploader.removeFile( file );
                    return;

                case 1:
                    file.rotation += 90;
                    break;

                case 2:
                    file.rotation -= 90;
                    break;
            }

            if ( supportTransition ) {
                deg = 'rotate(' + file.rotation + 'deg)';
                $wrap.css({
                    '-webkit-transform': deg,
                    '-mos-transform': deg,
                    '-o-transform': deg,
                    'transform': deg
                });
            } else {
                $wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
                // use jquery animate to rotation
                // $({
                //     rotation: rotation
                // }).animate({
                //     rotation: file.rotation
                // }, {
                //     easing: 'linear',
                //     step: function( now ) {
                //         now = now * Math.PI / 180;

                //         var cos = Math.cos( now ),
                //             sin = Math.sin( now );

                //         $wrap.css( 'filter', "progid:DXImageTransform.Microsoft.Matrix(M11=" + cos + ",M12=" + (-sin) + ",M21=" + sin + ",M22=" + cos + ",SizingMethod='auto expand')");
                //     }
                // });
            }


        });

        $li.appendTo( $queue );
    }

    // 负责view的销毁
    function removeFile( file ) {
        var $li = $('#'+file.id);

        delete percentages[ file.id ];
        updateTotalProgress();
        $li.off().find('.file-panel').off().end().remove();
    }

    function updateTotalProgress() {
        var loaded = 0,
            total = 0,
            spans = $progress.children(),
            percent;

        $.each( percentages, function( k, v ) {
            total += v[ 0 ];
            loaded += v[ 0 ] * v[ 1 ];
        } );

        percent = total ? loaded / total : 0;


        spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
        spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
        updateStatus();
    }

    function updateStatus() {
        var text = '', stats;

        if ( state === 'ready' ) {
            text = '选中' + fileCount + '张图片，共' +
                WebUploader.formatSize( fileSize ) + '。';
        } else if ( state === 'confirm' ) {
            stats = uploader.getStats();
            if ( stats.uploadFailNum ) {
                text = '已成功上传' + stats.successNum+ '张照片，'+
                    stats.uploadFailNum + '张照片上传失败，<button type="button" class="retry btn btn-link">重新上传</button>失败图片或<button class="ignore btn btn-link" href="#">忽略</button>'
            }

        } else {
            stats = uploader.getStats();
            text = '共' + fileCount + '张（' +
                WebUploader.formatSize( fileSize )  +
                '），已上传' + stats.successNum + '张';

            if ( stats.uploadFailNum ) {
                text += '，失败' + stats.uploadFailNum + '张';
            }
        }

        $info.html( text );
    }

    function setState( val ) {
        var file, stats;

        if ( val === state ) {
            return;
        }

        $upload.removeClass( 'state-' + state );
        $upload.addClass( 'state-' + val );
        state = val;

        switch ( state ) {
            case 'pedding':
                $placeHolder.removeClass( 'element-invisible' );
                $queue.hide();
                $statusBar.addClass( 'element-invisible' );
                uploader.refresh();
                break;

            case 'ready':
                $placeHolder.addClass( 'element-invisible' );
                $( '#filePicker2' ).removeClass( 'element-invisible');
                $queue.show();
                $statusBar.removeClass('element-invisible');
                uploader.refresh();
                break;

            case 'uploading':
                $( '#filePicker2' ).addClass( 'element-invisible' );
                $progress.show();
                $upload.text( '暂停上传' );
                break;

            case 'paused':
                $progress.show();
                $upload.text( '继续上传' );
                break;

            case 'confirm':
                $progress.hide();
                $( '#filePicker2' ).removeClass( 'element-invisible' );
                $upload.text( '开始上传' );

                stats = uploader.getStats();
                if ( stats.successNum && !stats.uploadFailNum ) {
                    setState( 'finish' );
                    return;
                }
                break;
            case 'finish':
                stats = uploader.getStats();
                if ( stats.successNum ) {
                    PNotifyInfo('上传成功！')
                } else {
                    // 没有成功的图片，重设
                    state = 'done';
                    location.reload();
                }
                break;
        }

        updateStatus();
    }

    uploader.onUploadProgress = function( file, percentage ) {
        var $li = $('#'+file.id),
            $percent = $li.find('.progress span');

        $percent.css( 'width', percentage * 100 + '%' );
        percentages[ file.id ][ 1 ] = percentage;
        updateTotalProgress();
    };

    uploader.onFileQueued = function( file ) {
        fileCount++;
        fileSize += file.size;

        if ( fileCount === 1 ) {
            $placeHolder.addClass( 'element-invisible' );
            $statusBar.show();
        }

        addFile( file );
        setState( 'ready' );
        updateTotalProgress();
    };

    uploader.onFileDequeued = function( file ) {
        fileCount--;
        fileSize -= file.size;

        if ( !fileCount ) {
            setState( 'pedding' );
        }

        removeFile( file );
        updateTotalProgress();

    };

    uploader.on( 'all', function( type, file, response ) {
        var stats;
        switch( type ) {
            case 'uploadFinished':
                setState( 'confirm' );
                console.log( uploader.getFiles() );    // => all files
                break;

            case 'startUpload':
                setState( 'uploading' );
                break;

            case 'stopUpload':
                setState( 'paused' );
                break;
            case 'uploadSuccess':
                if (successHandler) {
                    successHandler(file, response);
                }
                break;
            case 'uploadError':
                if (errorHandler) {
                    errorHandler(file, response);
                }
                break;

        }

    });

    uploader.onError = function( code ) {
        var errorMessage = '出错了，请重试！';
        if (code === 'F_DUPLICATE') {
            errorMessage = '请不要上传重复的文件!';
        }
        new PNotify({
            title: '错误',
            text: errorMessage,
            type: 'error',
            icon: 'glyphicon glyphicon-info-sign',

        });
    };

    $upload.on('click', function() {
        if ( $(this).hasClass( 'disabled' ) ) {
            return false;
        }

        if ( state === 'ready' ) {
            uploader.upload();
        } else if ( state === 'paused' ) {
            uploader.upload();
        } else if ( state === 'uploading' ) {
            uploader.stop();
        }
    });

    $info.on( 'click', '.retry', function() {
        uploader.retry();
    } );

    $info.on( 'click', '.ignore', function() {
        $.each(uploader.getFiles('error'), function (index, item) {
            uploader.removeFile( item );
        });
    } );

    $upload.addClass( 'state-' + state );
    updateTotalProgress();
}
