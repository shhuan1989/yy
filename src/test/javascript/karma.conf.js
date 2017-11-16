// Karma configuration
// http://karma-runner.github.io/0.13/config/configuration-file.html

var sourcePreprocessors = ['coverage'];

function isDebug() {
    return process.argv.indexOf('--debug') >= 0;
}

if (isDebug()) {
    // Disable JS minification if Karma is run with debug option.
    sourcePreprocessors = [];
}

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: 'src/test/javascript/'.replace(/[^/]+/g, '..'),

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'src/main/webapp/bower_components/jquery/dist/jquery.js',
            'src/main/webapp/bower_components/messageformat/messageformat.js',
            'src/main/webapp/bower_components/bootstrap/dist/js/bootstrap.js',
            'src/main/webapp/bower_components/json3/lib/json3.js',
            'src/main/webapp/bower_components/lodash/lodash.js',
            'src/main/webapp/bower_components/sockjs-client/dist/sockjs.js',
            'src/main/webapp/bower_components/stomp-websocket/lib/stomp.min.js',
            'src/main/webapp/bower_components/flot/jquery.flot.js',
            'src/main/webapp/bower_components/flot/jquery.flot.pie.js',
            'src/main/webapp/bower_components/holderjs/holder.js',
            'src/main/webapp/bower_components/metisMenu/dist/metisMenu.js',
            'src/main/webapp/bower_components/eve-raphael/eve.js',
            'src/main/webapp/bower_components/mocha/mocha.js',
            'src/main/webapp/bower_components/flot.tooltip/js/jquery.flot.tooltip.js',
            'src/main/webapp/bower_components/bootbox.js/bootbox.js',
            'src/main/webapp/bower_components/respond/dest/respond.src.js',
            'src/main/webapp/bower_components/jquery-ui/jquery-ui.js',
            'src/main/webapp/bower_components/nprogress/nprogress.js',
            'src/main/webapp/bower_components/moment/moment.js',
            'src/main/webapp/bower_components/underscore/underscore.js',
            'src/main/webapp/bower_components/moment-timezone/builds/moment-timezone-with-data-2010-2020.js',
            'src/main/webapp/bower_components/iCheck/icheck.min.js',
            'src/main/webapp/bower_components/validator/validator.js',
            'src/main/webapp/bower_components/parsleyjs/dist/parsley.js',
            'src/main/webapp/bower_components/parsleyjs/dist/i18n/zh_cn.extra.js',
            'src/main/webapp/bower_components/parsleyjs/dist/i18n/zh_cn.js',
            'src/main/webapp/bower_components/select2/dist/js/select2.js',
            'src/main/webapp/bower_components/select2/dist/js/i18n/zh-CN.js',
            'src/main/webapp/bower_components/select2/dist/js/i18n/es.js',
            'src/main/webapp/bower_components/fex-webuploader/dist/webuploader.js',
            'src/main/webapp/bower_components/jquery.inputmask/dist/inputmask/inputmask.js',
            'src/main/webapp/bower_components/jquery.inputmask/dist/inputmask/jquery.inputmask.js',
            'src/main/webapp/bower_components/numeral/numeral.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.animate.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.buttons.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.callbacks.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.confirm.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.desktop.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.history.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.mobile.js',
            'src/main/webapp/bower_components/pnotify/dist/pnotify.nonblock.js',
            'src/main/webapp/bower_components/file-saver/FileSaver.js',
            'src/main/webapp/bower_components/autolink/autolink-min.js',
            'src/main/webapp/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js',
            'src/main/webapp/bower_components/fullcalendar/dist/fullcalendar.js',
            'src/main/webapp/bower_components/fullcalendar/dist/locale-all.js',
            'src/main/webapp/bower_components/bootstrap-waitingfor/build/bootstrap-waitingfor.min.js',
            'src/main/webapp/bower_components/cropperjs/dist/cropper.js',
            'src/main/webapp/bower_components/angular/angular.js',
            'src/main/webapp/bower_components/angular-aria/angular-aria.js',
            'src/main/webapp/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'src/main/webapp/bower_components/angular-cache-buster/angular-cache-buster.js',
            'src/main/webapp/bower_components/angular-cookies/angular-cookies.js',
            'src/main/webapp/bower_components/angular-dynamic-locale/dist/tmhDynamicLocale.js',
            'src/main/webapp/bower_components/ngstorage/ngStorage.js',
            'src/main/webapp/bower_components/angular-loading-bar/build/loading-bar.js',
            'src/main/webapp/bower_components/angular-resource/angular-resource.js',
            'src/main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
            'src/main/webapp/bower_components/angular-translate/angular-translate.js',
            'src/main/webapp/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
            'src/main/webapp/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'src/main/webapp/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'src/main/webapp/bower_components/angular-ui-router/release/angular-ui-router.js',
            'src/main/webapp/bower_components/bootstrap-ui-datetime-picker/dist/datetime-picker.js',
            'src/main/webapp/bower_components/ng-file-upload/ng-file-upload.js',
            'src/main/webapp/bower_components/ngInfiniteScroll/build/ng-infinite-scroll.js',
            'src/main/webapp/bower_components/raphael/raphael.min.js',
            'src/main/webapp/bower_components/restangular/dist/restangular.js',
            'src/main/webapp/bower_components/ng-table/dist/ng-table.js',
            'src/main/webapp/bower_components/bootstrap-wysihtml5/src/bootstrap-wysihtml5.js',
            'src/main/webapp/bower_components/timepicker/lib/timepicker/timepicker.js',
            'src/main/webapp/bower_components/angular-cache/dist/angular-cache.js',
            'src/main/webapp/bower_components/angular-mocks/angular-mocks.js',
            'src/main/webapp/bower_components/morrisjs/morris.js',
            'src/main/webapp/bower_components/daterangepicker/lib/daterangepicker/daterangepicker.js',
            // endbower
            'src/main/webapp/app/app.module.js',
            'src/main/webapp/app/app.state.js',
            'src/main/webapp/app/app.constants.js',
            'src/main/webapp/app/**/*.+(js|html)',
            'src/test/javascript/spec/helpers/module.js',
            'src/test/javascript/spec/helpers/httpBackend.js',
            'src/test/javascript/**/!(karma.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: [],

        preprocessors: {
            './**/*.js': sourcePreprocessors
        },

        reporters: ['dots', 'junit', 'coverage', 'progress'],

        junitReporter: {
            outputFile: '../target/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {
            dir: 'target/test-results/coverage',
            reporters: [
                {type: 'lcov', subdir: 'report-lcov'}
            ]
        },

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        // to avoid DISCONNECTED messages when connecting to slow virtual machines
        browserDisconnectTimeout: 10000, // default 2000
        browserDisconnectTolerance: 1, // default 0
        browserNoActivityTimeout: 4 * 60 * 1000 //default 10000
    });
};
