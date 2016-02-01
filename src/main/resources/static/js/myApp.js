
"use strict";
var app = angular.module('myApp', [ 'ui.router', 'restangular',
		 'myApp.directives', 'myApp.controllers',
		'myApp.services','ngLocale']);//'ui.bootstrap'
app.config(function($stateProvider, $urlRouterProvider) {
	
	$urlRouterProvider.otherwise('/');
   // $locationProvider.html5Mode(true);
	$stateProvider.state('home', {
		url : '/',
		templateUrl : "view/home.html",
		controller:'HomeController'// /ModbusChart
	}).state('settings', {
		url : '/settings',
		templateUrl : 'view/admin/settings.html',
		controller : 'SettingsController'

	}).state('help', {
		url : '/help',
		templateUrl : 'view/help.html',
		controller:'HelpController'

	}).state('onlineChart', {
		url : '/online-chart',
		templateUrl : 'view/online-chart.html',
		controller : 'OnlineChartController',//'ChartOnlineController'

	}).state('403', {
		url : '/403',
		templateUrl : 'view/403.html'
		//controller : ''

	}).state('air-consumption-chart',{
		url:'/air-consumption-chart',
		templateUrl:'view/air-consumption-chart.html',
		controller:'AirConsumptionController'
	}).state('filepath',{
		url:'/filepath',
		templateUrl:'view/file-path.html',
		controller:'FilePathController'
	}).state('login', {
		url : '/login',
		templateUrl : 'login.html'
		//controller : ''

	})
	.state('calculatedChart', {
		url : '/calculated-chart',
		templateUrl : 'view/calculated-chart.html',
		controller : 'CalcuatedChartController',

	});
	
});
app.config([ 'RestangularProvider', function(RestangularProvider) {
	RestangularProvider.setBaseUrl('/ModbusChart');
	 // RestangularProvider.setRequestSuffix('.json');


} ]);

app.run(function($rootScope,$state,Restangular,$window) {
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
	
	


	Restangular.one('rest/loggedUser', 1).get().then(function(currentUser) {
	
	$rootScope.currentUser = currentUser.plain();
	console.log($rootScope.currentUser);
	

	
	
	});
	
	//$rootScope.preferences.currentPrice=0;
	Restangular.one('rest/preferences').get().then(function(data){
		$rootScope.preferences=data.plain();
		
	});
	
	
	 
});
