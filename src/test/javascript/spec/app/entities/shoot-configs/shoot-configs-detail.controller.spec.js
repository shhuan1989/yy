'use strict';

describe('Controller Tests', function() {

    describe('ShootConfigs Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockShootConfigs, MockShootConfig, MockProject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockShootConfigs = jasmine.createSpy('MockShootConfigs');
            MockShootConfig = jasmine.createSpy('MockShootConfig');
            MockProject = jasmine.createSpy('MockProject');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ShootConfigs': MockShootConfigs,
                'ShootConfig': MockShootConfig,
                'Project': MockProject
            };
            createController = function() {
                $injector.get('$controller')("ShootConfigsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:shootConfigsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
