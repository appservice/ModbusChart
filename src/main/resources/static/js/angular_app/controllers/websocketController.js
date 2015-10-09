/**
 * =============websocket controller======================
 */
angular.module('myApp.controllers').controller("WebsocketController", function($scope, Restangular) {

	var currentLocation = window.location;
	console.log(currentLocation);
	$scope.myData = [];
	$scope.dataToShow = null;
	$scope.closeButtonText = "Stop";
	$scope.is_loaded = false;
	$scope.squaresTable = [];
	var websocket = null;
	var isFirstMessage = true;
	
	var wsUrl;
	if (window.location.protocol == 'http:') {
	    wsUrl = 'ws://' + currentLocation.host + '/ModbusChart/flowMeasurementHandler';
	} else {
	    wsUrl = 'wss://' + currentLocation.host + '/ModbusChart/flowMeasurementHandler';
	}
	

	Restangular.one('rest/servers', 1).get().then(function(myServer) {

		// $scope.server = myServer;

		// console.log(myServer);
		$scope.seriesName = myServer.sensorsName;
		$scope.is_loaded = true;
		// var dataTableSize = 60 * 8 * 60;

		// $scope.onStartWebsocket=function(){

		console.log("start is starting!");
		testWebsocket();

		// }

		function testWebsocket() {
			websocket = new WebSocket(wsUrl);

			websocket.onopen = function(evt) {
				onOpen(evt)
			};
			websocket.onmessage = function(message) {
				onMessage(message)
			};
			websocket.onclose = function(evt) {
				onClose(evt)
				
			};

		}
		function onOpen(evt) {
			console.log("on open is on");

		}
		function onMessage(message) {
		

			var returnedData = angular.fromJson(message.data);
	console.log(returnedData);

			if (returnedData instanceof Array) {
				if (returnedData[0].date != null) {//returnedData.size>0&&
					$scope.myData = returnedData;
				

					var emptyValues = [];
					for (var emp_i = 0; emp_i < myServer.sensorsName.length; emp_i++) {
						emptyValues[emp_i] = null;
					}

					$scope.myData.push({
						"date" : returnedData[0].date + 28800000,
						"values" : emptyValues
					});

					// console.log(returnedData);
					$scope.$apply('myData');

				} else {

					$scope.squaresTable = returnedData;
					$scope.$apply('squaresTable');

				}
			} else if (returnedData.resetDate != null) {

				console.log(returnedData.resetDate);
				console.log("clear");
				$scope.myData = [];
				// $scope.$apply('myData');
				var emptyValues = [];
				for (var emp_i = 0; emp_i < myServer.sensorsName.length; emp_i++) {
					emptyValues[emp_i] = null;
				}

				$scope.myData.push({
					"date" : returnedData.resetDate + 28800000,
					"values" : emptyValues
				});
			} else if (returnedData.date != null)

			{
	
				var obj = returnedData;

				$scope.dataToShow = obj;
				$scope.myData.splice($scope.myData.length - 1, 0, obj);

				$scope.$apply('myData');
			}
			// }
		}

		function onClose(evt) {
			console.log("Websocket Closed");
			$scope.closeButtonText = "Start";
			$scope.$apply('closeButtonText');

			// websocket=null;
		}

		// -----------switch off when destroy controller-----------
		$scope.$on("$destroy", function(event) {
			console.log("destroyed");
			if (websocket.readyState == websocket.OPEN) {
				websocket.close();
				$scope.closeButtonText = "Start";
			}
		})
		// -------toggle button on/off-------------------
		$scope.onStartWebsocket = function() {
			if (websocket.readyState != websocket.CLOSED) {

				websocket.close();
				$scope.closeButtonText = "Start"; //
			} else {

				isFirstMessage = true;
				testWebsocket();
				$scope.closeButtonText = "Stop"
			}
			//	 
		}
		
		//------ reset sensor --------------
		$scope.onResetSensor=function(sensorNumber){
			var doRest=confirm("Czy wyzerować dane sensora?")
			if(doRest){
			if(websocket.readyState==websocket.OPEN){
				websocket.send("reset:"+sensorNumber);
				console.log('reset sensror'+sensorNumber);
			}}
		}
		
	//$scope.isAdminRole=function(){
			//console.log($rootScope.currentUser.roles.indexOf('ROLE_ADMIN')>-1);
		//if($rootScope.currentUser.roles.indexOf({autority:"ROLE_ADMIN"})>-1)
		//return true
	//	else{
		//	return false;
		//console.log("test");
		//}

	//}
	});
});
