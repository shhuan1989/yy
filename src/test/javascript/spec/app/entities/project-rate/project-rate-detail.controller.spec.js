'use strict';

describe('Controller Tests', function() {

    describe('ProjectRate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProjectRate, MockEmployee, MockProject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProjectRate = jasmine.createSpy('MockProjectRate');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockProject = jasmine.createSpy('MockProject');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProjectRate': MockProjectRate,
                'Employee': MockEmployee,
                'Project': MockProject
            };
            createController = function() {
                $injector.get('$controller')("ProjectRateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:projectRateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
