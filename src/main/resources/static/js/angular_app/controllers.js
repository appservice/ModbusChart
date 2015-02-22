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
		.controller('ChartOnlineController',['$scope','Restangular', function($scope,Restangular) {
			$scope.myData = [];


			 var baseMeasurements = Restangular.all('servers/2/measurements');
			 
			 baseMeasurements.getList().then(function(measurements){
				 
				 $scope.myData=measurements;
		
				 
			 });
			 

		}])
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

							
							/*$scope.getOneServer=function(){
								
							}*/
							
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
									
									//-----------checking if the server is running or occurred error
									baseServers.customGET(id+"/executor").then(function(response){
										console.log(response);
										if(response=="true"){
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
