/**
 * ============================================================================================================
 * Help CONTROLLER
 * ============================================================================================================
 */
angular.module('myApp.controllers').controller('HelpController', [ '$scope', 'Restangular', 'poller', function($scope, Restangular, poller) {

	$scope.myData = {};

	Restangular.one('rest/servers', 1).get().then(function(myServer) {
		console.log(myServer);
		$scope.mySeriesNumber = myServer.sensorsName.length || 0;
		$scope.serverTimeIterval = myServer.timeInterval;

		// ----------------poller----------------

		var myPoller = poller.get(myServer.one("measurements/last"), { // Restangular.getOne('servers/2/measurements/last').getList()
			action : 'get',

			delay : $scope.serverTimeIterval
		});

		myPoller.promise.then(null, null, function(myData) {

			$scope.myData = myData.plain();
			// console.log($scope.myData);
		});

	});

	/*
	 * Restangular.one('rest/servers/1/').customGET('measurements/last',{"timePeriod":31*24*60*60*1000}).then(function(data){
	 * $scope.myData=data.plain(); console.log($scope.myData);
	 * 
	 * 
	 * });
	 */

} ]);