(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
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
