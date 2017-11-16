'use strict';

describe('Controller Tests', function() {

    describe('Expense Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockExpense, MockDictionary, MockProject, MockExpenseItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockExpense = jasmine.createSpy('MockExpense');
            MockDictionary = jasmine.createSpy('MockDictionary');
            MockProject = jasmine.createSpy('MockProject');
            MockExpenseItem = jasmine.createSpy('MockExpenseItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Expense': MockExpense,
                'Dictionary': MockDictionary,
                'Project': MockProject,
                'ExpenseItem': MockExpenseItem
            };
            createController = function() {
                $injector.get('$controller')("ExpenseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yiyingOaApp:expenseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
