'use strict';

describe('Controller Tests', function() {

    describe('Income Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockIncome, MockDictionary;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockIncome = jasmine.createSpy('MockIncome');
            MockDictionary = jasmine.createSpy('MockDictionary');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Income': MockIncome,
                'Dictionary': MockDictionary
            };
            createController = function() {
                $injector.get('$controller')("IncomeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:incomeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
