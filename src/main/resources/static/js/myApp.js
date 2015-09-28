/**
 * 'emguo.poller',
 * 'mgcrea.ngStrap' ,
 */

var myApp = angular.module('myApp', [ 'ui.router', 'restangular',
		 'myApp.directives', 'myApp.controllers',
		'myApp.services','ngLocale']);//'ui.bootstrap'
myApp.config(function($stateProvider, $urlRouterProvider) {
	
	$urlRouterProvider.otherwise('/');
   // $locationProvider.html5Mode(true);
	$stateProvider.state('home', {
		url : '/',
		templateUrl : "view/home.html",
		controller:'HomeController'// /ModbusChart
	})/*.state('chart', {
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

	}).state('download',{
		url:'/download',
		templateUrl:'view/download.html',
		controller:'DownloadController'
	}).state('customPeriodChart',{
		url:'/custom-period-chart',
		templateUrl:'view/custom-period-chart.html',
		controller:'CustomPeriodChartController'
	})*/.state('settings', {
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
		controller : 'WebsocketController2',//'ChartOnlineController'

	}).state('403', {
		url : '/403',
		templateUrl : 'view/403.html'
		//controller : ''

	}).state('websocket',{
		url:'/websocket',
		templateUrl:'view/websocket.html',
		controller:'WebsocketController'
	}).state('filepath',{
		url:'/filepath',
		templateUrl:'view/file-path.html',
		controller:'FilePathController'
	}).state('login', {
		url : '/login',
		templateUrl : 'login.html'
		//controller : ''

	});
	
});
myApp.config([ 'RestangularProvider', function(RestangularProvider) {
	RestangularProvider.setBaseUrl('/ModbusChart');
	 // RestangularProvider.setRequestSuffix('.json');


} ]);

/*
 * // ----stop poller when change site myApp.config(function(pollerConfig) {
 * pollerConfig.stopOnStateChange(true); // If you use $stateProvider from //
 * ui-router. });
 */
/*
myApp.config(function(pollerConfig) {
	pollerConfig.stopOnStateChange = true; // If you use $stateProvider from
	// ui-router.
	// pollerConfig.stopOnRouteChange = true; // If you use $routeProvider from
	// ngRoute.
	pollerConfig.resetOnStateChange = true;
});
*/
myApp.run(function($rootScope,$state,Restangular,$window) {
	$rootScope.$on('$stateChangeError',
		    function(event, toState, toParams, fromState, fromParams, error){
//		  
		        console.log(toState, toParams, fromState, fromParams, error);

		//var currentLocation = window.location;
		      //  console.log(error);
		        if(error.status == 401){
		            console.log("error 401");
		           $window.location.href="/ModbusChart/logout";
		          //  window.location.href="/login";
		           // $state.go("login");
		        }else
		          if(error.status==403){
		        	  console.log("error 403");
		        	// $window.location.href="/ModbusChart/logout";
		        	  $state.go("403");
		          }
		          else
		        	  if(error.status == 302){
				            console.log("error 302");
				          // $window.location.href="/login";
				         //   $state.go("login");
				        }
		    });
	
	
	$rootScope.errorView=function(error){
		switch(error.status){
		//case 400:alert('Zły przedział czasowy!');break;
		case 500:alert('Bład serwera');break;
		case 0:alert('Brak połączenia z serwerem!\nOdświerz stronę!');break;
		case 401:$window.location.href="/ModbusChart/logout";break}

		
	}
	
	
/*	Restangular.one('rest/servers', 1).get().then(function(myServer) {
		// console.log(myServer);
		$rootScope.mainServer = myServer;});*/

	Restangular.one('rest/loggedUser', 1).get().then(function(currentUser) {
	
	$rootScope.currentUser = currentUser.plain();
	console.log($rootScope.currentUser);
	

		
	
		

	
	});
	
	
	 
});
