'use strict';

describe('Controller Tests', function() {

    describe('ProjectProgressItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProjectProgressItem, MockEmployee, MockProjectProgressTable;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProjectProgressItem = jasmine.createSpy('MockProjectProgressItem');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockProjectProgressTable = jasmine.createSpy('MockProjectProgressTable');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProjectProgressItem': MockProjectProgressItem,
                'Employee': MockEmployee,
                'ProjectProgressTable': MockProjectProgressTable
            };
            createController = function() {
                $injector.get('$controller')("ProjectProgressItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yyOaApp:projectProgressItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
