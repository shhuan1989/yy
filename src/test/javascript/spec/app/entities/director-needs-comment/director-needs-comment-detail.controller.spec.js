'use strict';

describe('Controller Tests', function() {

    describe('DirectorNeedsComment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDirectorNeedsComment, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDirectorNeedsComment = jasmine.createSpy('MockDirectorNeedsComment');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DirectorNeedsComment': MockDirectorNeedsComment,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("DirectorNeedsCommentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:directorNeedsCommentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
