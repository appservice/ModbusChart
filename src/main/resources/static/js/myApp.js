/**
 * 
 */
var myApp = angular.module('myApp', [ 'ui.router','ngResource','myApp.directives',
                                      'myApp.controllers','myApp.services']);
myApp.config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/');

	$stateProvider
	.state('home', {
		url : '/',
		templateUrl : "view/home.html"///ModbusChart
	})
	.state('chart',{
		url:'/chart',
		templateUrl:'view/chart.html',
		controller: 'ChartController'
		
	})
	.state('settings',{
		url:'/settings',
		templateUrl:'view/admin/settings.html',
		controller:'SettingsController'
	
	})
		.state('help',{
		url:'/help',
		templateUrl:'view/help.html'
	
	})
	.state('chartOnline',{
		url:'/chart-online',
		templateUrl:'view/chart-online.html',
		controller: 'ChartOnlineController'
	
	});

	
});
