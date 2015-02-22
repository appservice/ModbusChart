/**
 * 
 */
angular
		.module('myApp.controllers', [])
		.controller('ChartController', [ '$scope', function($scope) {


			$scope.height='500px';

			$scope.someData = [ {
				"column-1" : 1,
				"column-2" : 3,
				"date" : "2014-03-01 20:58:00"
			}, {
				"column-1" : 2,
				"column-2" : 1,
				"date" : "2014-03-01 21:58:01"
			}, {
				"column-1" : 3,
				"column-2" : 2,
				"date" : "2014-03-01 22:58:02"
			}, {
				"column-1" : 1,
				"column-2" : 3,
				"date" : "2014-03-01 23:58:00"
			} ];

		} ])

		// ------------------------------------------------------------------------------------------
		.controller('ChartOnlineController',['$scope','$filter','Restangular', function($scope,$filter,Restangular) {
			$scope.myData = [];


			 var baseMeasurements = Restangular.all('servers/2/measurements');
			 
			 baseMeasurements.getList().then(function(measurements){
				// $scope.measurements=measurements;
				 
			
				for(var i=0;i<measurements.length;i++){
					
					var myDate=new Date(measurements[i].date);
					var myStringDate=$filter('date')(myDate,"yyyy-MM-dd HH:mm:ss");

					var dataToDisplay={};
				 
				 //dynamically create object with measured value
				 for(var j=0;j<measurements[i].measuredValue.length;j++){
					// console.log("jvalue: "+j);
					 dataToDisplay["column-"+(j+1)]=measurements[i].measuredValue[j];
				 };
				 dataToDisplay["date"]=myStringDate;
					
					$scope.myData.push(dataToDisplay);
				}
		
				 
			 });
			 

		}])
		.controller(
				'SettingsController',
				[
						'$scope',
						'Restangular',
						function($scope, Restangular) {
							$scope.serversList = [];

							
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
