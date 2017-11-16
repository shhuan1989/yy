'use strict';

describe('Controller Tests', function() {

    describe('AssetBorrowRecord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAssetBorrowRecord, MockEmployee, MockAsset;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAssetBorrowRecord = jasmine.createSpy('MockAssetBorrowRecord');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockAsset = jasmine.createSpy('MockAsset');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AssetBorrowRecord': MockAssetBorrowRecord,
                'Employee': MockEmployee,
                'Asset': MockAsset
            };
            createController = function() {
                $injector.get('$controller')("AssetBorrowRecordDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yyOaApp:assetBorrowRecordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
