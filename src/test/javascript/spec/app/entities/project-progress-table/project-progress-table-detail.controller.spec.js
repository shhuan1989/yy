'use strict';

describe('Controller Tests', function() {

    describe('ProjectProgressTable Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProjectProgressTable, MockProjectProgressItem, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProjectProgressTable = jasmine.createSpy('MockProjectProgressTable');
            MockProjectProgressItem = jasmine.createSpy('MockProjectProgressItem');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProjectProgressTable': MockProjectProgressTable,
                'ProjectProgressItem': MockProjectProgressItem,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("ProjectProgressTableDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:projectProgressTableUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
