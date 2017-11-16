'use strict';

describe('Controller Tests', function() {

    describe('NoticeChat Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNoticeChat, MockEmployee, MockPictureInfo, MockFileInfo, MockNotice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNoticeChat = jasmine.createSpy('MockNoticeChat');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockPictureInfo = jasmine.createSpy('MockPictureInfo');
            MockFileInfo = jasmine.createSpy('MockFileInfo');
            MockNotice = jasmine.createSpy('MockNotice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'NoticeChat': MockNoticeChat,
                'Employee': MockEmployee,
                'PictureInfo': MockPictureInfo,
                'FileInfo': MockFileInfo,
                'Notice': MockNotice
            };
            createController = function() {
                $injector.get('$controller')("NoticeChatDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:noticeChatUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
