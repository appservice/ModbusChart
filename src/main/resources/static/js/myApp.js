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
		templateUrl : 'view/chart.html',
		controller : 'ChartController'

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

	});

});
myApp.config([ 'RestangularProvider', function(RestangularProvider) {
	RestangularProvider.setBaseUrl('/ModbusChart/rest');

} ]);

/*// ----stop poller when change site
myApp.config(function(pollerConfig) {
	pollerConfig.stopOnStateChange(true); // If you use $stateProvider from
								// ui-router.
});*/


myApp.config(function (pollerConfig) {
    pollerConfig.stopOnStateChange = true; // If you use $stateProvider from ui-router.
   // pollerConfig.stopOnRouteChange = true; // If you use $routeProvider from ngRoute.
    pollerConfig.resetOnStateChange = true;
});
