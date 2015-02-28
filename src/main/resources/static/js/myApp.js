/**
 * 
 */
var myApp = angular.module('myApp', [ 'ui.router', 'restangular',
		'emguo.poller', 'myApp.directives', 'myApp.controllers',
		'myApp.services' ]);
myApp.config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/');

	$stateProvider.state('home', {
		url : '/',
		templateUrl : "view/home.html"// /ModbusChart
	}).state('chart', {
		url : '/chart',
		templateUrl : 'view/day-chart.html',
		controller : 'DayChartController'

	}).state('settings', {
		url : '/settings',
		templateUrl : 'view/admin/settings.html',
		controller : 'SettingsController'

	}).state('help', {
		url : '/help',
		templateUrl : 'view/help.html'

	}).state('chartOnline', {
		url : '/chart-online',
		templateUrl : 'view/chart-online.html',
		controller : 'ChartOnlineController'

	}).state('403', {
		url : '/403',
		templateUrl : 'view/403.html',
		//controller : ''

	});
	
	
});
myApp.config([ 'RestangularProvider', function(RestangularProvider) {
	RestangularProvider.setBaseUrl('/ModbusChart/rest');

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
