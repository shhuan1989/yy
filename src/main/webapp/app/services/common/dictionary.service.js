(function() {
    'use strict';
    angular
        .module('yiyingOaApp')
        .factory('DictionaryService', DictionaryService);

    DictionaryService.$inject = ['$resource', 'Restangular', '$q', 'CacheFactory', '$timeout'];

    function DictionaryService ($resource, Restangular, $q, CacheFactory, $timeout ) {
        var dictionaryCache = CacheFactory.get('dictionaryCache');
        // Check to make sure the cache doesn't already exist
        if (!dictionaryCache) {
            dictionaryCache = CacheFactory('dictionaryCache', {
                maxAge: 15 * 60 * 1000, // Items added to this cache expire after 15 minutes
                cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
                deleteOnExpire: 'aggressive' // Items will be deleted from this cache when they expire
            });
        }

        var service = {};

        var resourceUrl =  'api/dictionaries';

        service.resource = $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                }
            },
            'update': {method: 'PUT'}
        });

        service.yesnoOptions = yesnoOptions;
        service.hasOrNotoOptions = hasOrNotoOptions;
        service.projectStatusOptions = projectStatusOptions;
        service.costOptions = costOptions;
        service.contractStatusOptions = contractStatusOptions;
        service.contractLevelOptions = contractLevelOptions;
        service.staffTypeOptions = staffTypeOptions;
        service.actorSourceOptions = actorSourceOptions;
        service.countryOptions = countryOptions;
        service.equipmentCategoryOptions = equipmentCategoryOptions;
        service.industryOptions = industryOptions;
        service.clientSourceOptions = clientSourceOptions;
        service.nationalityOptions = nationalityOptions;
        service.childBearingOptions = childBearingOptions;
        service.driverLicenseOptions = driverLicenseOptions;
        service.marriageOptions = marriageOptions;
        service.educationOptions = educationOptions;
        service.genderOptions = genderOptions;
        service.jobPositionStatuusOptions = jobPositionStatuusOptions;
        service.nativePlaceOptions = nativePlaceOptions;
        service.deptOptions = deptOptions;
        service.jobTitleOptions = jobTitleOptions;
        service.setOptions = setOptions;
        service.roomOptions = roomOptions;
        service.meetingStatusOptions = meetingStatusOptions;
        service.vacationTypeOptions = vacationTypeOptions;
        service.approvalStatusOptions = approvalStatusOptions;
        service.contractInstallmentStatusOptions = contractInstallmentStatusOptions;
        service.borrowRecordTypeOptions = borrowRecordTypeOptions;
        service.clientStatusOptions = clientStatusOptions;
        service.payMethodOptions = payMethodOptions;

        var ERROR_NOT_FOUND = "Not found";

        function setOptions(vm) {
            var promises = [];

            var promises2 = [];

            var d1 = $q.defer();
            yesnoOptions().then(function (options) {
                vm.yesnoOptions = options;
                d1.resolve();
            });
            promises.push(d1.promise);

            var d2 = $q.defer();
            hasOrNotoOptions().then(function (options) {
                vm.hasOrNotoOptions = options;
                d2.resolve();
            });
            promises.push(d2.promise);

            var d3 = $q.defer();
            projectStatusOptions().then(function (options) {
                vm.projectStatusOptions = options;
                d3.resolve();
            });
            promises.push(d3.promise);

            var d4 = $q.defer();
            costOptions().then(function (options) {
                vm.costOptions = options;
                d4.resolve();
            });
            promises.push(d4.promise);

            var d5 = $q.defer();
            contractStatusOptions().then(function (options) {
                vm.contractStatusOptions = options;
                d5.resolve();
            });
            promises.push(d5.promise);

            var d6 = $q.defer();
            staffTypeOptions().then(function (options) {
                vm.staffTypeOptions = options;
                d6.resolve();
            });
            promises.push(d6.promise);

            var d7 = $q.defer();
            actorSourceOptions().then(function (options) {
                vm.actorSourceOptions = options;
                d7.resolve();
            });
            promises.push(d7.promise);

            var d8 = $q.defer();
            countryOptions().then(function (options) {
                vm.countryOptions = options;
                d8.resolve();
            });
            promises.push(d8.promise);

            var d9 = $q.defer();
            equipmentCategoryOptions().then(function (options) {
                vm.equipmentCategoryOptions = options;
                d9.resolve();
            });
            promises.push(d9.promise);

            var d11 = $q.defer();
            industryOptions().then(function (options) {
                vm.industryOptions = options;
                d11.resolve();
            });
            promises.push(d11.promise);

            var d12 = $q.defer();
            clientSourceOptions().then(function (options) {
                vm.clientSourceOptions = options;
                d12.resolve();
            });
            promises.push(d12.promise);

            var d13 = $q.defer();
            nationalityOptions().then(function (options) {
                vm.nationalityOptions = options;
                d13.resolve();
            });
            promises.push(d13.promise);

            var d14 = $q.defer();
            childBearingOptions().then(function (options) {
                vm.childBearingOptions = options;
                d14.resolve();
            });
            promises.push(d14.promise);

            var d15 = $q.defer();
            driverLicenseOptions().then(function (options) {
                vm.driverLicenseOptions = options;
                d15.resolve();
            });
            promises.push(d15.promise);

            var d16 = $q.defer();
            marriageOptions().then(function (options) {
                vm.marriageOptions = options;
                d16.resolve();
            });
            promises.push(d16.promise);

            var d17 = $q.defer();
            educationOptions().then(function (options) {
                vm.educationOptions = options;
                d17.resolve();
            });
            promises.push(d17.promise);

            var d18 = $q.defer();
            genderOptions().then(function (options) {
                vm.genderOptions = options;
                d18.resolve();
            });
            promises.push(d18.promise);

            var d19 = $q.defer();
            jobPositionStatuusOptions().then(function (options) {
                vm.jobPositionStatuusOptions = options;
                d19.resolve();
            });
            promises.push(d19.promise);

            var d20 = $q.defer();
            nativePlaceOptions().then(function (options) {
                vm.nativePlaceOptions = options;
                d20.resolve();
            });
            promises.push(d20.promise);

            var d21 = $q.defer();
            deptOptions().then(function (options) {
                vm.deptOptions = options;
                d21.resolve();
            });
            promises.push(d21.promise);

            var d22 = $q.defer();
            jobTitleOptions().then(function (options) {
                vm.jobTitleOptions = options;
                d22.resolve();
            });
            promises.push(d22.promise);

            var d23 = $q.defer();
            vacationTypeOptions().then(function (options) {
                vm.vacationTypeOptions = options;
                d23.resolve();
            });
            promises.push(d23.promise);

            var d24 = $q.defer();
            jobTitleOptions().then(function (options) {
                vm.approvalStatusOptions = options;
                d24.resolve();
            });
            promises.push(d24.promise);

            var d25 = $q.defer();
            jobTitleOptions().then(function (options) {
                vm.contractInstallmentStatusOptions = options;
                d25.resolve();
            });
            promises.push(d25.promise);

            var d26 = $q.defer();
            jobTitleOptions().then(function (options) {
                vm.borrowRecordTypeOptions = options;
                d26.resolve();
            });
            promises.push(d26.promise);

            var d27 = $q.defer();
            clientStatusOptions().then(function (options) {
                vm.clientStatusOptions = options;
                d27.resolve();
            });
            promises.push(d27.promise);

            var d28 = $q.defer();
            contractLevelOptions().then(function (options) {
                vm.contractLevelOptions = options;
                d28.resolve();
            });
            promises.push(d28.promise);

            var d29 = $q.defer();
            payMethodOptions().then(function (options) {
                vm.payMethodOptions = options;
                d29.resolve();
            });
            promises.push(d29.promise);

            return $q.all(promises);
        }

        function arrayOptions(key, noCache) {
            var deferred = $q.defer();
            if (!noCache && dictionaryCache.get(key)) {
                deferred.resolve(dictionaryCache.get(key))
            } else {
                Restangular.all('api/dictionaries?category='+key).getList()
                    .then(
                        function (options) {
                            if(options && options.length > 0) {
                                dictionaryCache.put(key, options);
                                deferred.resolve(options);
                            }
                        },
                        function (err) {
                            deferred.reject(err);
                        }
                    );
            }
            return deferred.promise;
        }

        function filterOutAll(key, noCache) {
            var deferred = $q.defer();
            if (!noCache && dictionaryCache.get(key)) {
                deferred.resolve(dictionaryCache.get(key))
            } else {
                Restangular.all('api/dictionaries?category='+key).getList()
                    .then(function (options) {
                        if(options && options.length > 0) {
                            var result = filterOutAllInopions(options);
                            dictionaryCache.put(key, result);
                            deferred.resolve(result);
                        } else {
                            deferred.reject(ERROR_NOT_FOUND);
                        }
                    }, function (err) {
                        deferred.reject(err);
                    });
            }

            return deferred.promise;
        }

        function firstChild(key, noCache) {
            var deferred = $q.defer();
            if (!noCache && dictionaryCache.get(key)) {
                deferred.resolve(dictionaryCache.get(key))
            } else {
                Restangular.all('api/dictionaries?category='+key).getList()
                    .then(function (options) {
                        if(options && options.length > 0) {
                            var result = filterOutAllInopions(options[0].children);
                            dictionaryCache.put(key, result);
                            deferred.resolve(result);
                        } else {
                            deferred.reject(ERROR_NOT_FOUND)
                        }
                    }, function (err) {
                        deferred.reject(err);
                    });
            }
            return deferred.promise;
        }

        function borrowRecordTypeOptions() {
            return arrayOptions("asset_return_status")
        }
        function contractInstallmentStatusOptions() {
            return arrayOptions('contract_installment_status');
        }
        function approvalStatusOptions() {
            return arrayOptions("approval_status")
        }
        function vacationTypeOptions() {
            return firstChild('假期类型', true);
        }
        function clientStatusOptions() {
            return firstChild('客户状态', true);
        }
        function meetingStatusOptions() {
            return arrayOptions('meeting_status');
        }
        function roomOptions() {
            return firstChild('会议室', true);
        }
        function yesnoOptions() {
            return arrayOptions('yesno');
        }
        function hasOrNotoOptions() {
            return arrayOptions('has');
        }
        function projectStatusOptions() {
            return arrayOptions('project_status');
        }
        function costOptions() {
            return firstChild('支出项目');
        }
        function staffTypeOptions() {
            return firstChild('工作人员类型', true);
        }
        function actorSourceOptions() {
            return firstChild('演员所属', true);
        }
        function countryOptions() {
            return firstChild('国家');
        }
        function equipmentCategoryOptions() {
            return firstChild('器材类别', true);
        }
        function industryOptions() {
            return firstChild('行业类别', true);
        }
        function clientSourceOptions() {
            return firstChild('客户来源', true);
        }
        function payMethodOptions() {
            return firstChild('支付方式', true);
        }
        function nationalityOptions() {
            var deferred = $q.defer();
            var key = 'nationality';
            if (dictionaryCache.get(key)) {
                deferred.resolve(dictionaryCache.get(key))
            } else {
                Restangular.all('api/dictionaries?category='+key).getList()
                    .then(function (options) {
                        var result = options.map(function(item){return item.name;});
                        dictionaryCache.put(key, result);
                        deferred.resolve(result);
                    }, function (err) {
                        deferred.reject(err);
                    });
            }

            return deferred.promise;
        }
        function childBearingOptions() {
            return arrayOptions('childbearing');
        }
        function driverLicenseOptions() {
            return filterOutAll('driver_license');
        }
        function educationOptions() {
            return filterOutAll('education');
        }
        function marriageOptions() {
            return filterOutAll('marriage');
        }
        function jobPositionStatuusOptions() {
            return filterOutAll('job_position_status', true);
        }
        function genderOptions() {
            return filterOutAll('gender');
        }
        function deptOptions() {
            var deferred = $q.defer();
            var key = 'depts';
            if (false && dictionaryCache.get(key)) {// temporarily disable cache
                deferred.resolve(dictionaryCache.get(key))
            } else {
                Restangular.all('api/depts?size=2000').getList()
                    .then(function (options) {
                        var result = filterOutAllInopions(options);
                        dictionaryCache.put(key, result);
                        deferred.resolve(result);
                    }, function (err) {
                        deferred.reject(err);
                    });
            }

            return deferred.promise;
        }
        function jobTitleOptions() {
            var deferred = $q.defer();
            var key = 'job-titles';
            if (false && dictionaryCache.get(key)) {// temporarily disable cache
                deferred.resolve(dictionaryCache.get(key))
            } else {
                Restangular.all('api/job-titles?size=2000').getList()
                    .then(function (options) {
                        var result = filterOutAllInopions(options);
                        dictionaryCache.put(key, result);
                        deferred.resolve(result);
                    }, function (err) {
                        deferred.reject(err);
                    });
            }

            return deferred.promise;
        }
        function nativePlaceOptions() {
            var deferred = $q.defer();
            var key = 'provinces';
            if (dictionaryCache.get(key)) {
                deferred.resolve(dictionaryCache.get(key))
            } else {
                Restangular.all('api/provinces?size=2000').getList()
                    .then(function (options) {
                        var result = filterOutAllInopions(options);
                        dictionaryCache.put(key, result);
                        deferred.resolve(result);
                    }, function (err) {
                        deferred.reject(err);
                    });
            }
            return deferred.promise;
        }

        function contractStatusOptions() {
            return arrayOptions('has');
        }

        function contractLevelOptions() {
            return arrayOptions('contract_level');
        }

        return service;
    }
})();
