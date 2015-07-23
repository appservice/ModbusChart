/**
 * 
 */
angular.module('myApp.controllers', [])
/**
 * ============================================================================================================
 * Custom period chart controller
 * ============================================================================================================
 */
.controller('CustomPeriodChartController', [ '$scope', 'Restangular', function($scope, Restangular) {

	$scope.chartTitle = 'Wykres z n dni';
	$scope.myData = [];
	// $scope.maxSeriesNumber = 8;
	$scope.showChart = false;

	var today = new Date();
	var tomorrow = new Date();
	tomorrow.setDate(today.getDate() + 1);
	$scope.untilMaxDate = tomorrow;

	//

	$scope.createChart = function() {
		// console.log($scope.fromDate);
		var startDate = new Date($scope.fromDate);
		var endDate = new Date($scope.untilDate);

		$scope.chartTitle = 'Wykres od: ' + startDate.toLocaleString() + ' do ' + endDate.toLocaleString();

		$scope.isLoading = true;

		Restangular.one('rest/servers/1/').customGET('measurements', {
			"startDate" : startDate.getTime(),
			"endDate" : endDate.getTime()
		})

		.then(function(data) {
			$scope.myData = data.plain();

			$scope.isLoading = false;

			if (data.length > 0) {
				$scope.showChart = true;
			} else {
				$scope.showChart = false;
				alert("Brak danych do wyświetlenia");

			}

		}, function(error) {
			$scope.isLoading = false;
			$scope.errorView(error);
			console.log(error);
		});

	}



} ])

/**
 * ================================================================================================
 * 2 hours chart controller
 * ================================================================================================
 */
.controller('TwoHoursChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {

	$scope.myData = [];
	// $scope.maxSeriesNumber = 7;
	$scope.isLoading = true;
	$scope.chartTitle = 'Wykres z ostatnich 2 godzin';

	DataDownloader.getData(2 * 60 * 60 * 1000).then(function(data) {
		$scope.isLoading = false;
		$scope.myData = data;

	});

} ])

/**
 * ================================================================================================
 * 8 hours chart controller
 * ================================================================================================
 */
.controller('EightHoursChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {

	$scope.myData = [];
	// $scope.maxSeriesNumber = 7;
	$scope.isLoading = true;
	$scope.chartTitle = 'Wykres z ostatnich 8 godzin';

	DataDownloader.getData(8 * 60 * 60 * 1000).then(function(data) {
		$scope.isLoading = false;
		$scope.myData = data;

	});

} ])

/**
 * ================================================================================================
 * Day chart controller
 * ================================================================================================
 */
.controller('DayChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {

	$scope.myData = [];
	// $scope.maxSeriesNumber = 7;
	$scope.chartTitle = 'Wykres z ostatnich 24 godzin';
	$scope.isLoading = true;

	DataDownloader.getData(24 * 60 * 60 * 1000).then(function(data) {
		$scope.isLoading = false;
		$scope.myData = data;

	});

} ])

/**
 * ================================================================================================
 * 7 days chart controller
 * ================================================================================================
 */
.controller('SevenDaysChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {

	$scope.myData = [];
	// $scope.maxSeriesNumber = 7;
	$scope.chartTitle = 'Wykres z ostatnich 7 dni';
	$scope.isLoading = true;

	DataDownloader.getData(7 * 24 * 60 * 60 * 1000).then(function(data) {
		$scope.isLoading = false;
		$scope.myData = data;

	});

} ])

/**
 * ================================================================================================
 * 31 days chart controller
 * ================================================================================================
 */
.controller('ThirtyOneDaysChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {
	$scope.myData = [];
	// $scope.maxSeriesNumber = 7;
	$scope.isLoading = true;
	$scope.chartTitle = 'Wykres z ostatnich 31 dni';

	DataDownloader.getData(31 * 24 * 60 * 60 * 1000).then(function(data) {
		$scope.isLoading = false;
		$scope.myData = data;

	});

} ])

/**
 * ============================================================================================================
 * chart online controller
 * ======================================================================================================
 */
.controller('ChartOnlineController', [ '$scope', 'Restangular', 'poller', function($scope, Restangular, poller) {

	$scope.myData = [];
	$scope.maxSeriesNumber = 7;

	// don't show befoure get the data from server
	$scope.is_loaded = false;

	Restangular.one('rest/servers', 1).get().then(function(myServer) {
		// console.log(myServer);
		$scope.myServer = myServer;

		if ($scope.myServer != null) {
			$scope.is_loaded = true;

			$scope.maxSeriesNumber = myServer.readedDataCount; // || 0;
			$scope.serverTimeIterval = myServer.timeInterval;

			var dataTableSize = 60;

			var isFirstData = true;

			// ----------------poller----------------

			var myPoller = poller.get($scope.myServer.one("measurement-online"), {

				action : 'get',
				delay : $scope.serverTimeIterval,
			// catchError : true
			});

			myPoller.promise.then(null, null, function(data) {

				// check the empty response
				if (data === undefined) {
					console.log("Brak pomiarów!");
					myPoller.stop();
				} else {

					// fill measurements table by empty measurements
					if (isFirstData) {
						var emptyValues = [];
						for (var emp_i = 0; emp_i < $scope.mySeriesNumber; emp_i++) {
							emptyValues[emp_i] = null;
						}
						for (var j = 0; j < dataTableSize; j++) {
							$scope.myData.push({
								"date" : data.date - (dataTableSize - j) * $scope.serverTimeIterval,
								"values" : emptyValues
							});

						}

						isFirstData = false;
					}

					// if response is not empty and server is run

					$scope.dataToShow = data.plain();
					// console.log($scope.dataToShow);

					if ($scope.myData[$scope.myData.length - 1].date != data.date || $scope.myData[$scope.myData.length - 2].date != data.date) {

						if ($scope.myData.length > dataTableSize - 1)
							$scope.myData.shift();

						$scope.myData.push(data.plain());
						// console.log($scope.myData);

					} else {

						myPoller.stop();

					}
				}
			});
		}

	});

	// });

} ])

/**
 * ============================================================================================================
 * download controller
 */
.controller('DownloadController', [ '$scope', 'Restangular', function($scope, Restangular) {

} ])

/**
 * ============================================================================================================
 * SettingsController -for site settings
 */
.controller(
		'SettingsController',
		[
				'$scope',
				'Restangular',
				'$timeout',
				function($scope, Restangular, $timeout) {
					$scope.isButtonDisabled = false;
					$scope.serversList = [];
					$scope.readedDataTypes = [ {
						"type" : "FLOAT",
						"name" : "FLOAT (32 bit)"
					}, {
						"type" : "INTEGER",
						"name" : "INTEGER (16 bit)"
					} ];

					var errorResponseFunctoin = function(response) {
						console.log(response.status);
						if (response.status == 405)
							alert("Do tej operacji ma uprawnienia tylko administrator!");

					}
					// ---------get all servers-------------------
					var baseServers = Restangular.all('rest/servers');
					// callback
					baseServers.getList().then(function(servers) {
						$scope.serversList = servers;
						if ($scope.serversList.length > 0) {
							$scope.isButtonDisabled = true;
						}

					});

					/*
					 * $scope.getOneServer=function(){ }
					 */

					// ----------remove server----------------
					$scope.removeServer = function(server) {
						// $scope.serversList.splice(id, 1);
						var doRemove = confirm("Czy na pewno chcesz usunąć " + server.name + "?\n"
								+ "W przypadku usunięcia zostaną skasowane także wszystkie pomiary! ")

						if (doRemove == true) {

							server.remove().then(function() {
								var index = $scope.serversList.indexOf(server);
								if (index > -1)
									$scope.serversList.splice(index, 1);
								$scope.isButtonDisabled = false;
							}, errorResponseFunctoin);
						}
					}

					// --------add new server------------------

					$scope.addNewServer = function(newServer) {
						newServer.id = 1;
						baseServers.post(newServer).then(
								function(response) {
									$scope.serversList.push(response);
									$scope.isButtonDisabled = true;
									console.log("added id:" + response.id + "+ name: " + response.name + " ip: " + response.ip + " port: "
											+ response.port + " timeInterval: " + response.timeInterval);

								}, errorResponseFunctoin);
					}

					// --------update server-----------------
					$scope.preEditServer = function(server) {
						$scope.updatedServer = server;
						console.log(server);
					}

					$scope.updateServer = function(server) {
						// var editedServer=Restangular.copy(server);

						// $scope.updateServer=server;
						server.put();

					}

					// ---------run server (run excecutor)----
					$scope.runServer = function(server) {
						var id = server.id;
						server.post('executor').then(function() {
							console.log("started");

							// -----------checking if the server is
							// running or occurred error

							//	

							var executeGet = function() {

								baseServers.customGET(id + "/executor").then(function(response) {
									// console.log(response);
									if (response.connectedToServer) {
										alert("Zapis do bazy danych z serwera: " + server.name + " włączony!");

									} else {
										// console.log(response);
										alert("Wystąpił błąd podczas połączenia z serwerem: " + server.name + "\n" + response.errorMessage + "!");

									}

								});

							}
							$timeout(executeGet, 2000);

						}, errorResponseFunctoin

						);
					}

					// ---------stop server (run excecutor)----
					$scope.stopServer = function(server) {
						var id = server.id;
						baseServers.customDELETE(id + "/executor").then(function() {
							alert("Nasłuchiwanie i zapis danych do bazy wyłączony!")

						}, errorResponseFunctoin);
					}

					$scope.switchOffServer = function() {
						Restangular.one("shutdown").customPOST().then(function(data) {
							alert("Aplikacja wyłączona");
						});
					}
				} ])

/**
 * ============================================================================================================
 * Help CONTROLLER
 * ============================================================================================================
 */
.controller('HelpController', [ '$scope', 'Restangular', 'poller', function($scope, Restangular, poller) {

	$scope.myData = {};

	Restangular.one('rest/servers', 1).get().then(function(myServer) {
		console.log(myServer);
		$scope.mySeriesNumber = myServer.readedDataCount || 0;
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

} ])
/**
 * =============home controller======================
 */

.controller("HomeController", function($scope) {
	$scope.list = [ 1, 2, 3, 4, 5, 6, 7, 8 ];
	$scope.canvName = "c1";
})

/**
 * =============websocket controller======================
 */

.controller("WebsocketController", function($scope) {

	var currentLocation = window.location;
	console.log(currentLocation);
	$scope.myData = [];
	$scope.dataToShow = null;
	$scope.closeButtonText = "Stop";
	var websocket = null;
	var isFirstMessage = true;
	var server=null;
	var dataTableSize=60;

	// $scope.onStartWebsocket=function(){

	console.log("start is starting!");
	testWebsocket();

	// }

	function testWebsocket() {
		websocket = new WebSocket("ws://" + currentLocation.host + "/ModbusChart/measurementHandler");

		websocket.onopen = function(evt) {
			onOpen(evt)
		};
		websocket.onmessage = function(message) {
			onMessage(message)
		};
		websocket.onclose = function(evt) {
			onClose(evt)
		};

		// websocket.onmessage = function(evt) { onMessage(evt) };
		// websocket.onerror = function(evt) { onError(evt) };
	}
	function onOpen(evt) {
		console.log("on open is on");
		// websocket.send("start");
		// for(var i=0;i<100;i++){
		// websocket.send("kojene messege "+i);
		// }

	}
	function onMessage(message) {
		// console.log("object :"+message.data);
		// var obj=JSON.parse(message.data);
		// if(isFirstMessage){

		

		var returnedData = JSON.parse(message.data);
		if (returnedData.ip != null) {
			server = returnedData;
			console.log(server);

			// 
		} else {
			var obj = angular.fromJson(message.data);
			
			if(isFirstMessage){
				var emptyValues = [];
				for (var emp_i = 0; emp_i < server.readedDataCount; emp_i++) {
					emptyValues[emp_i] = null;
				}
				//console.log(obj.date - (dataTableSize )*server.timeInterval);
				for (var j = 0; j < dataTableSize; j++) {
					$scope.myData.push({
						"date" : obj.date - (dataTableSize - j) * server.timeInterval,
						"values" : emptyValues
					});

				}
				
				isFirstMessage = false;
			}
			// }else{

			
			// console.log(new Date(obj.date));
			// console.log(obj);

			if ($scope.myData.length > dataTableSize) {
				$scope.myData.shift();
			}

			$scope.dataToShow = obj;
			$scope.myData.push(obj);
			$scope.$apply('myData');
		}
	}
	// console.log(message);
	// websocket.close();
	// }

	function onClose(evt) {
		console.log("Websocket Closed");

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
			$scope.closeButtonText = "Start";
			// console.log("closed websocket session");
			//
		} else {
			$scope.myData = [];
			isFirstMessage = true;
			testWebsocket();
			$scope.closeButtonText = "Stop"

		}
	}
});
