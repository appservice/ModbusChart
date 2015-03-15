/**
 * 
 */
var myApp = angular.module('myApp', [ 'ui.router', 'restangular',
		'emguo.poller', 'myApp.directives', 'myApp.controllers',
		'myApp.services','mgcrea.ngStrap' ,'ngLocale']);//'ui.bootstrap'
myApp.config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/');

	$stateProvider.state('home', {
		url : '/',
		templateUrl : "view/home.html"// /ModbusChart
	}).state('chart', {
		url : '/2-hours-chart',
		templateUrl : 'view/day-chart.html',
		controller : 'TwoHoursChartController'})
	.state('8-hours-chart', {
		url : '/8-hours-chart',
		templateUrl : 'view/day-chart.html',
		controller : 'EightHoursChartController'})

	.state('1-day-chart', {
		url : '/1-day-chart',
		templateUrl : 'view/day-chart.html',
		controller : 'DayChartController'

	}).state('7-days-chart', {
		url : '/7-days-chart',
		templateUrl : 'view/day-chart.html',
		controller : 'SevenDaysChartController'

	}).state('31-days-chart', {
		url : '/31-days-chart',
		templateUrl : 'view/day-chart.html',
		controller : 'ThirtyOneDaysChartController'

	}).state('settings', {
		url : '/settings',
		templateUrl : 'view/admin/settings.html',
		controller : 'SettingsController'

	}).state('help', {
		url : '/help',
		templateUrl : 'view/help.html',
		controller:'HelpController'

	}).state('chartOnline', {
		url : '/chart-online',
		templateUrl : 'view/chart-online.html',
		controller : 'ChartOnlineController'

	}).state('403', {
		url : '/403',
		templateUrl : 'view/403.html',
		//controller : ''

	}).state('download',{
		url:'/download',
		templateUrl:'view/download.html',
		controller:'DownloadController'
	}).state('allDataChart',{
		url:'/all-data-chart',
		templateUrl:'view/all-data-chart.html',
		controller:'AllDataChartController'
	}).state('customPeriodChart',{
		url:'/custom-period-chart',
		templateUrl:'view/custom-period-chart.html',
		controller:'CustomPeriodChartController'
	});
	
});
myApp.config([ 'RestangularProvider', function(RestangularProvider) {
	RestangularProvider.setBaseUrl('/ModbusChart');

} ]);

/*
 * // ----stop poller when change site myApp.config(function(pollerConfig) {
 * pollerConfig.stopOnStateChange(true); // If you use $stateProvider from //
 * ui-router. });
 */

myApp.config(function(pollerConfig) {
	pollerConfig.stopOnStateChange = true; // If you use $stateProvider from
	// ui-router.
	// pollerConfig.stopOnRouteChange = true; // If you use $routeProvider from
	// ngRoute.
	pollerConfig.resetOnStateChange = true;
});

myApp.run(function($rootScope,$state) {
	$rootScope.$on('$stateChangeError',
		    function(event, toState, toParams, fromState, fromParams, error){
//		        console.log('stateChangeError');
//		        console.log(toState, toParams, fromState, fromParams, error);

		        if(error.status == 401){
		            console.log("error 401");

		            $state.go("login");
		        }else
		          if(error.status==403){
		        	  console.log("error 403")
		        	  $state.go("403");
		          }
		    });
	
})
