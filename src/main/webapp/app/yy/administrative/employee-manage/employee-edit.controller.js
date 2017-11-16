(function() {
    'use strict';

    angular
        .module('yyOaApp')
        .controller('EmployeeEditController', EmployeeEditController);

    EmployeeEditController.$inject = ['$timeout', 'AuthServerProvider', '$scope', 'DictionaryService', '$state', '$stateParams', '$q', 'disableEdit', 'EmployeeService'];

    function EmployeeEditController ($timeout, AuthServerProvider, $scope, DictionaryService, $state, $stateParams, $q, disableEdit, EmployeeService) {
        var vm = this;
        vm.initJsComponents = initJsComponents;

        vm.pageTitle = $state.current.data.pageTitle;
        vm.disableEdit = !(!disableEdit);
        DictionaryService.setOptions(vm);
        vm.employee = {};
        vm.isLoading = false;
        vm.photoBlob = null;

        vm.save = save;
        vm.cancel = cancel;
        vm.load = load;

        vm.load();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function load() {
            if ($stateParams.id == undefined) {
                setDefaultsOFEmployee();
                initDateTimePicker();
            } else {
                vm.isLoading = true;
                EmployeeService.get(
                    {id: $stateParams.id},
                    function (employee) {
                        vm.employee = employee;
                        setDefaultsOFEmployee();
                        calBirthDateAndAge(vm.employee.idNumber);
                        initDateTimePicker();
                        vm.isLoading = false;
                    },
                    function () {
                        PNotifyLoadFail();
                        initDateTimePicker();
                        vm.isLoading = false;
                    }
                );
            }
        }

        function cancel() {
            $state.go("^");
        }

        function save () {
            var valid = true;
            $.each($('.emp-edit-content form').parsley(), function (i, p) {
                if (!valid || !p.validate()) {
                    valid = false;
                }
            });

            if (!valid) {
                PNotifyInvalidInput();
                return;
            }

            vm.isSaving = true;

            if (vm.employee.id !== null) {
                EmployeeService.update(vm.employee, onSaveSuccess, onSaveError);
            } else {
                EmployeeService.save(vm.employee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            PNotifySaveSuccess();
            $state.go("^");
        }

        function onSaveError () {
            vm.isSaving = false;
            PNotifySaveFail();
        }

        function setDefaultsOFEmployee() {
            if (!vm.employee.gender) {
                vm.employee.gender = {
                    id: 0
                };
            }
            if (!vm.employee.hasDriverLicense) {
                vm.employee.hasDriverLicense = {
                    id: 0
                };
            }
            if (!vm.employee.marriage) {
                vm.employee.marriage = {
                    id: 0
                };
            }
            if (!vm.employee.childbearing) {
                vm.employee.childbearing = {
                    id: 0
                };
            }
            if (!vm.employee.hasDriverLicense) {
                vm.employee.hasDriverLicense = {
                    id: 0
                };
            }
        }

        $scope.$watch('vm.employee.idNumber', function(newValue, oldValue) {
            calBirthDateAndAge(newValue);
        });

        function calBirthDateAndAge(idCardNumber) {
            var p = /\d{16}.*/;
            if (p.test(idCardNumber)) {
                var year = idCardNumber.substr(6,4);
                vm.employee.birthDate = moment(year +'-'+idCardNumber.substr(10,2)+'-'+idCardNumber.substr(12,2), "YYYY-MM-DD").valueOf();
                vm.employee.age = moment().subtract(parseInt(year), 'year').year();
            } else {
                vm.employee.birthDate = null;
                vm.employee.age = null;
            }
        }

        function initDateTimePicker() {
            $('#field_hire_date').datetimepicker({
                format: dateFormatDMY(),
                locale: 'zh-CN',
                defaultDate: datetimePickerDefaultTime(vm.employee.hireDate)
            }).on('dp.change', function(ev){
                vm.employee.hireDate = moment(ev.date).valueOf();
            });
        }

        function initJsComponents() {
            $('.emp-edit-content form').parsley();

            $('.select2').select2({
                language: "zh-CN"
            });

            // init cropper
            var Cropper = window.Cropper;
            var URL = window.URL || window.webkitURL;
            var container = document.querySelector('.img-container');
            var image = container.getElementsByTagName('img').item(0);
            var download = document.getElementById('download');
            var actions = document.getElementById('actions');
            var options = {
                aspectRatio: 1 / 1,
                preview: '.img-preview',
                ready: function (e) {
                    console.log(e.type);
                },
                cropstart: function (e) {
                    console.log(e.type, e.detail.action);
                },
                cropmove: function (e) {
                    console.log(e.type, e.detail.action);
                },
                cropend: function (e) {
                    console.log(e.type, e.detail.action);
                },
                crop: function (e) {
                    console.log(e.type, e.detail);
                },
                zoom: function (e) {
                    console.log(e.type, e.detail.ratio);
                }
            };
            var cropper = new Cropper(image, options);
            var originalImageURL = image.src;
            var uploadedImageURL;

            // Tooltip
            $('[data-toggle="tooltip"]').tooltip();


            // Buttons
            if (!document.createElement('canvas').getContext) {
                $('button[data-method="getCroppedCanvas"]').prop('disabled', true);
            }

            if (typeof document.createElement('cropper').style.transition === 'undefined') {
                $('button[data-method="rotate"]').prop('disabled', true);
                $('button[data-method="scale"]').prop('disabled', true);
            }

            // Methods
            actions.querySelector('.docs-buttons').onclick = function (event) {
                var e = event || window.event;
                var target = e.target || e.srcElement;
                var result;
                var input;
                var data;

                if (!cropper) {
                    return;
                }

                while (target !== this) {
                    if (target.getAttribute('data-method')) {
                        break;
                    }

                    target = target.parentNode;
                }

                if (target === this || target.disabled || target.className.indexOf('disabled') > -1) {
                    return;
                }

                data = {
                    method: target.getAttribute('data-method'),
                    target: target.getAttribute('data-target'),
                    option: target.getAttribute('data-option'),
                    secondOption: target.getAttribute('data-second-option')
                };

                if (data.method) {
                    if (typeof data.target !== 'undefined') {
                        input = document.querySelector(data.target);

                        if (!target.hasAttribute('data-option') && data.target && input) {
                            try {
                                data.option = JSON.parse(input.value);
                            } catch (e) {
                                console.log(e.message);
                            }
                        }
                    }

                    if (data.method === 'getCroppedCanvas') {
                        data.option = JSON.parse(data.option);
                    }

                    result = cropper[data.method](data.option, data.secondOption);

                    switch (data.method) {
                        case 'scaleX':
                        case 'scaleY':
                            target.setAttribute('data-option', -data.option);
                            break;

                        case 'getCroppedCanvas':
                            if (result) {
                                result.toBlob(function (blob) {
                                    var formData = new FormData();

                                    formData.append('file', blob);

                                    $.ajax('/resource/pictures', {
                                        method: "POST",
                                        data: formData,
                                        processData: false,
                                        contentType: false,
                                        headers: {"Authorization": 'Bearer ' + AuthServerProvider.getToken()},
                                        success: function (pictureInfo) {
                                            vm.employee.photo = pictureInfo;
                                            PNotifySaveSuccess();
                                        },
                                        error: function () {
                                            PNotifySaveFail();
                                        }
                                    });
                                });
                            }

                            break;

                        case 'destroy':
                            cropper = null;

                            if (uploadedImageURL) {
                                URL.revokeObjectURL(uploadedImageURL);
                                uploadedImageURL = '';
                                image.src = originalImageURL;
                            }

                            break;
                    }

                    if (typeof result === 'object' && result !== cropper && input) {
                        try {
                            input.value = JSON.stringify(result);
                        } catch (e) {
                            console.log(e.message);
                        }
                    }
                }
            };

            document.body.onkeydown = function (event) {
                var e = event || window.event;

                if (!cropper || this.scrollTop > 300) {
                    return;
                }

                switch (e.keyCode) {
                    case 37:
                        e.preventDefault();
                        cropper.move(-1, 0);
                        break;

                    case 38:
                        e.preventDefault();
                        cropper.move(0, -1);
                        break;

                    case 39:
                        e.preventDefault();
                        cropper.move(1, 0);
                        break;

                    case 40:
                        e.preventDefault();
                        cropper.move(0, 1);
                        break;
                }
            };


            // Import image
            var inputImage = document.getElementById('inputImage');

            if (URL) {
                inputImage.onchange = function () {
                    var files = this.files;
                    var file;

                    if (cropper && files && files.length) {
                        file = files[0];

                        if (/^image\/\w+/.test(file.type)) {
                            if (uploadedImageURL) {
                                URL.revokeObjectURL(uploadedImageURL);
                            }

                            image.src = uploadedImageURL = URL.createObjectURL(file);
                            cropper.destroy();
                            cropper = new Cropper(image, options);
                            inputImage.value = null;
                        } else {
                            window.alert('Please choose an image file.');
                        }
                    }
                };
            } else {
                inputImage.disabled = true;
                inputImage.parentNode.className += ' disabled';
            }
        }
    }
})();
