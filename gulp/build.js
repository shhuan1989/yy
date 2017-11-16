'use strict';

var fs = require('fs'),
    gulp = require('gulp'),
    lazypipe = require('lazypipe'),
    footer = require('gulp-footer'),
    sourcemaps = require('gulp-sourcemaps'),
    rev = require('gulp-rev'),
    htmlmin = require('gulp-htmlmin'),
    ngAnnotate = require('gulp-ng-annotate'), // adds and removes AngularJS dependency injection annotations.
    prefix = require('gulp-autoprefixer'),
    cssnano = require('gulp-cssnano'), // minify css
    uglify = require('gulp-uglify'), // Minify JavaScript with UglifyJS2.
    useref = require("gulp-useref"), // merge multiple js/css files between <!-- build...> and <!-- endbuild --> into one file
    revReplace = require("gulp-rev-replace"), // rewrite occurrences of filenames which have been renamed by gulp-rev
    plumber = require('gulp-plumber'), // Prevent pipe breaking caused by errors from gulp plugins
    gulpIf = require('gulp-if'),
    debug = require('gulp-debug'),
    bom = require('gulp-bom'),
    handleErrors = require('./handle-errors');

var config = require('./config');

var initTask = lazypipe()
    .pipe(sourcemaps.init);
var jsTask = lazypipe()
    .pipe(ngAnnotate)
    .pipe(uglify);
var cssTask = lazypipe()
    .pipe(prefix)
    .pipe(cssnano, {zindex: false});

module.exports = function() {
    var templates = fs.readFileSync(config.tmp + '/templates.js');
    var manifest = gulp.src(config.revManifest);

    return gulp.src([config.app + '**/*.html',
        '!' + config.app + 'app/**/*.html',
        '!' + config.app + 'swagger-ui/**/*',
        '!' + config.bower + '**/*.html'])
        .pipe(bom())
        .pipe(plumber({errorHandler: handleErrors}))
        //init sourcemaps and prepend semicolon
        .pipe(useref({}, initTask))
        //append html templates
        .pipe(gulpIf('**/app.js', footer(templates)))
        .pipe(gulpIf('*.js', jsTask()))
        .pipe(gulpIf('*.css', cssTask()))
        .pipe(gulpIf('*.html', htmlmin({collapseWhitespace: true})))
        .pipe(gulpIf('**/*.!(html)', rev()))
        .pipe(revReplace({manifest: manifest}))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(config.dist));
};
