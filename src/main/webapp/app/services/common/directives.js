(function() {
    'use strict';
    angular
        .module('yyOaApp')
        .directive('yyFileChange', function() {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var onChangeHandler = scope.$eval(attrs.yyFileChange);
                element.bind('change', onChangeHandler);
            }
        };
    });

})();
