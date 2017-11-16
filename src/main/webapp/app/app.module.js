(function() {
    'use strict';

    var yiyingOaApp =
    angular
        .module('yiyingOaApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'ngTable',
            'restangular',
            'angular-loading-bar',
            'angular-cache',
            'angular.img'
        ])
        .run(run);

    yiyingOaApp.filter('range', function() {
        return function(input, total) {
            total = parseInt(total);

            for (var i=0; i<total; i++) {
                input.push(i);
            }

            return input;
        };
    });

    yiyingOaApp.filter('percentage', ['$filter', function ($filter) {
        return function (input, decimals) {
            if (input == undefined && decimals == undefined) {
                return "";
            }
            return $filter('number')(input * 100, decimals) + '%';
        };
    }]);

    yiyingOaApp.filter('yyna', ['$filter', function ($filter) {
        return function (input) {
            if (input == undefined) {
                return "N/A";
            }
            return input;
        };
    }]);

    yiyingOaApp.filter('percentage_abs', ['$filter', function ($filter) {
        return function (input, decimals) {
            if (!input && !decimals) {
                return "0%";
            }
            input = input > 1 ? 1 : input;
            input = input < 0 ? 0 : input;
            return $filter('number')(input * 100, decimals) + '%';
        };
    }]);

    yiyingOaApp.filter('yydate', ['$filter', function ($filter) {
        return function (input) {
            if (!input) {
                return '';
            }
            var m = moment(input);
            if (m.isValid()) {
                var l = moment('1900-01-01', 'YYYY-MM-DD');
                var r = moment('3000-01-01', 'YYYY-MM-DD');
                if (m.isBefore(l) || r.isBefore(m)) {
                    return '';
                }

                return m.format(dateFormatDMY());
            }
            return '';
        };
    }]);

    yiyingOaApp.filter('yytime', ['$filter', function ($filter) {
        return function (input) {
            if (!input) {
                return '';
            }
            var m = moment(input);
            if (m.isValid()) {
                return m.format(dateFormatDmyHms());
            }
            return '';
        };
    }]);

    yiyingOaApp.filter('yyhour', ['$filter', function ($filter) {
        return function (input) {
            if (!input) {
                return '';
            }
            var m = moment(input);
            if (m.isValid()) {
                return m.format('HH:mm MM/DD/YYYY');
            }
            return '';
        };
    }]);

    yiyingOaApp.filter('arrprop', ['$filter', function ($filter) {
        return function (input, field) {
            if (!input || input.length <= 0) {
                return '';
            }

            if (field == undefined) {
                return input.join(', ');
            }
            return input.map(function (item) {
                return item[field];
            }).join(', ');
        };
    }]);


    yiyingOaApp.filter('fixed', function() {
        return function(input, bits) {
            if (isNaN(input)) {
                return input;
            }
            return parseFloat(input).toFixed(parseInt(bits));
        };
    });

    yiyingOaApp.filter('yymoney', function() {
        return function(input) {
            if (isNaN(input)) {
                return '0.0';
            }
            return parseFloat(input).toFixed(2);
        };
    });

    yiyingOaApp.filter('yyyesno', function() {
        return function(input) {
            if (input) {
                return '是';
            }
            return '否';
        };
    });

    run.$inject = ['stateHandler', '$rootScope', '$log', 'translationHandler', 'NgTableParams'];

    function run(stateHandler, $rootScope, $log, translationHandler, NgTableParams) {
        stateHandler.initialize();
        translationHandler.initialize();
        PNotify.prototype.options.styling = "fontawesome";
        $.fn.select2.defaults.set( "theme", "bootstrap" );

        $rootScope.defaultNgTableParams = function() {
            return new NgTableParams({ count: 10}, { counts: [10, 25, 50, 100]});
        };

        Object.values = x =>
            Object.keys(x).reduce((y, z) =>
            y.push(x[z]) && y, []);

        Array.prototype.average = function () {
            let s = this;
            if (s && s.length > 0) {
                return s.reduce((a, b) => a + b) / s.length;
            }
            return 0;
        };

        Array.prototype.sum = function () {
            let s = this;
            if (s && s.length > 0) {
                return s.reduce((a, b) => a + b);
            }
            return 0;
        };

        Array.prototype.percentile = function (p) {
            let s = this.filter(i => i != undefined);
            let result = Math.max.apply(null, s);
            if (s && s.length > 0) {
                for (let v of s) {
                    if (s.filter(t => t <= v).length / s.length >= p) {
                        if (result) {
                            result = Math.min(result, v);
                        }
                    }
                }
            }
            return result;
        };

        Array.prototype.flatten = function () {
            return flatten(this)
        };

        function flatten (list) {
            return list.reduce((a, b) => (Array.isArray(b) ? a.push(...flatten(b)) : a.push(b), a), [])
        }

        Array.prototype.unique = function () {
            return [ ...new Set(this) ]
        }
    }
})();
