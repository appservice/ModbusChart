/**
 * 
 */
angular
		.module('myApp.controllers', [])
		.controller('ChartController', [ '$scope', function($scope) {


			$scope.height='500px';

			$scope.someData = [{

			    "id": 1448,
			    "date": 1424625141783,
			    "serverId": 2,
			    "measuredValue": 

			    [
			        11,
			        3,
			        12,
			        42,
			        4,
			        1
			    ]

			},
			{

			    "id": 1449,
			    "date": 1424625142783,
			    "serverId": 2,
			    "measuredValue": 

			    [
			        11,
			        3,
			        12,
			        42,
			        4,
			        1
			    ]

			}];

		} ])

		// ------------------------------------------------------------------------------------------
		.controller('ChartOnlineController',['$scope','Restangular','poller','getLastMeasurement', function($scope,Restangular,poller,getLastMeasurement) {
			$scope.myData = [];
			

		 var baseMeasurements = Restangular.all('servers/1/measurements');
			 
			 baseMeasurements.getList().then(function(measurements){
				 
				 $scope.myData=measurements;
				// console.log(measurements);
				 
				 var myPoller = poller.get(getLastMeasurement);
				 
					
				    var myPoller = poller.get(Restangular.one('servers/1/measurements/last') , { //Restangular.all('servers/2/measurements/last').getList() 
				    	action: 'get',
					        
					        delay: 5000});
				 
			
					   	myPoller.promise.then(null, null,function(data){
					   	   console.log(measurements[measurements.length-1].id);
							/*console.log(data[0]+" myData: "+ $scope.myData);*/
							console.log("tekst "+new Date());
							if (measurements[measurements.length-1].id!=data[0].id){
								console.log("dodamy");
								$scope.myData.shift();
								$scope.myData.push(data[0]);
							}else{
								console.log("niedodamy");
							}
						});
					   	
				 
			 });
			 
		
		//	console.log(getLastMeasurement);
			 
			 // Get poller.
			 
	


			    

		}])
		
		/**
		 * ============================================================================================================
		 * SettingsController -for site settings
		 */
		.controller(
				'SettingsController',
				[
						'$scope',
						'Restangular',
						function($scope, Restangular) {
							$scope.serversList = [];
							$scope.readedDataTypes=[
							           {"type":"FLOAT","name":"FLOAT (32 bit)"},
							           {"type":"INTEGER","name":"INTEGER (16 bit)"}
							                        ];

							
							var errorResponseFunctoin = function(response) {
								console.log(response.status);
								if (response.status == 405)
									alert("Do tej operacji ma uprawnienia tylko administrator!");

							}
							// ---------get all servers-------------------
							var baseServers = Restangular.all('servers');
							// callback
							baseServers.getList().then(function(servers) {
								$scope.serversList = servers;

							});

							
							/*
							 * $scope.getOneServer=function(){ }
							 */
							
							// ----------remove server----------------
							$scope.removeServer = function(server) {
								// $scope.serversList.splice(id, 1);
								server.remove().then(
										function() {
											var index = $scope.serversList
													.indexOf(server);
											if (index > -1)
												$scope.serversList.splice(
														index, 1);
										}, errorResponseFunctoin);
							}

							// --------add new server------------------

							$scope.addNewServer = function(newServer) {
								baseServers.post(newServer).then(
										function(response) {
											$scope.serversList.push(response);
											console.log("added id:"
													+ response.id + "+ name: "
													+ response.name + " ip: "
													+ response.ip + " port: "
													+ response.port
													+ " timeInterval: "
													+ response.timeInterval);

										}, errorResponseFunctoin);
							}

							// --------update server-----------------
							$scope.preEditServer = function(server) {
								$scope.updatedServer = server;
							}

							$scope.updateServer = function(server) {
								// var editedServer=Restangular.copy(server);

								// $scope.updateServer=server;
								server.put();

							}

							// ---------run server (run excecutor)----
							$scope.runServer = function(server) {
								var id=server.id;
								server.post('executor').then(function() {
									console.log("run");
									
									// -----------checking if the server is
									// running or occurred error
									baseServers.customGET(id+"/executor").then(function(response){
										console.log(response);
										if(response.connectedToServer){
											alert("Zapis do bazy danych z serwera: "+server.name+ " włączony!");

										}else{
											alert("Wystąpił błąd podczas połączenia z serwerem: "+ server.name+"\nSprawdź połączenie!");	

										}
									
									});
									
									// angular.element()
								}, errorResponseFunctoin

								);
							}

							// ---------stop server (run excecutor)----
							$scope.stopServer = function(server) {
								var id = server.id;
								baseServers.customDELETE(id + "/executor")
										.then(function() {
											alert("Nasłuchiwanie i zapis danych do bazy wyłączony!")

										}, errorResponseFunctoin);
							}
						} ]);
