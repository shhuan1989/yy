'use strict';

describe('Controller Tests', function() {

    describe('Actor Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockActor, MockPictureInfo, MockDictionary;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockActor = jasmine.createSpy('MockActor');
            MockPictureInfo = jasmine.createSpy('MockPictureInfo');
            MockDictionary = jasmine.createSpy('MockDictionary');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Actor': MockActor,
                'PictureInfo': MockPictureInfo,
                'Dictionary': MockDictionary
            };
            createController = function() {
                $injector.get('$controller')("ActorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:actorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
