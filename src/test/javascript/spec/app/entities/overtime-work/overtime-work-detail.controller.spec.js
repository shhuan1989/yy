'use strict';

describe('Controller Tests', function() {

    describe('OvertimeWork Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOvertimeWork, MockEmployee, MockApprovalRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOvertimeWork = jasmine.createSpy('MockOvertimeWork');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockApprovalRequest = jasmine.createSpy('MockApprovalRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OvertimeWork': MockOvertimeWork,
                'Employee': MockEmployee,
                'ApprovalRequest': MockApprovalRequest
            };
            createController = function() {
                $injector.get('$controller')("OvertimeWorkDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:overtimeWorkUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
