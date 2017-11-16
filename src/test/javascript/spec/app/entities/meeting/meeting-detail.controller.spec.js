'use strict';

describe('Controller Tests', function() {

    describe('Meeting Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMeeting, MockEmployee, MockRoom, MockProject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMeeting = jasmine.createSpy('MockMeeting');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockRoom = jasmine.createSpy('MockRoom');
            MockProject = jasmine.createSpy('MockProject');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Meeting': MockMeeting,
                'Employee': MockEmployee,
                'Room': MockRoom,
                'Project': MockProject
            };
            createController = function() {
                $injector.get('$controller')("MeetingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:meetingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
