/**
 * 
 */
var myApp = angular.module('myApp', [ 'ui.router','ngResource' ]);
myApp.config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/');

	$stateProvider.state('home', {
		url : '/',
		templateUrl : 'view/home.html'///ModbusChart
	});

	
});
