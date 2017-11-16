(function () {
    'use strict';
    /*global angular, Blob, URL */

    angular.module('angular.img', [
    ]).directive('httpSrc', ['$http', 'CacheFactory', function ($http, CacheFactory) {

        var pictureCache = CacheFactory.get('pictureCache');
        // Check to make sure the cache doesn't already exist
        if (!pictureCache) {
            pictureCache = CacheFactory('pictureCache', {
                maxAge: 15 * 60 * 1000, // Items added to this cache expire after 15 minutes
                cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
                deleteOnExpire: 'aggressive' // Items will be deleted from this cache when they expire
            });
        }

        return {
            link: function ($scope, elem, attrs) {
                function revokeObjectURL() {
                    if ($scope.objectURL) {
                        URL.revokeObjectURL($scope.objectURL);
                    }
                }

                $scope.$watch('objectURL', function (objectURL) {
                    elem.attr('src', objectURL);
                });

                $scope.$on('$destroy', function () {
                    revokeObjectURL();
                });

                attrs.$observe('httpSrc', function (url) {
                    revokeObjectURL();

                    if(url && url.indexOf('data:') === 0) {
                        $scope.objectURL = url;
                    } else if(url) {
                        $http.get(url, { responseType: 'arraybuffer', cache: pictureCache })
                            .then(function (response) {
                                var blob = new Blob(
                                    [ response.data ],
                                    { type: response.headers('Content-Type') }
                                );
                                $scope.objectURL = URL.createObjectURL(blob);
                            });
                    }
                });
            }
        };
    }]);
}());
