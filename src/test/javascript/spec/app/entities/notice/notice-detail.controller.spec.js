'use strict';

describe('Controller Tests', function() {

    describe('Notice Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNotice, MockProject, MockEmployee, MockDept;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNotice = jasmine.createSpy('MockNotice');
            MockProject = jasmine.createSpy('MockProject');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockDept = jasmine.createSpy('MockDept');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Notice': MockNotice,
                'Project': MockProject,
                'Employee': MockEmployee,
                'Dept': MockDept
            };
            createController = function() {
                $injector.get('$controller')("NoticeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yyOaApp:noticeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
