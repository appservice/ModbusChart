/**
 * 
 */
angular
		.module('myApp.controllers', [])
		.controller('ChartController', [ '$scope', function($scope) {

			/*
			 * $scope.someData = [ { "year" : "1920", "value" : -0.307 }, {
			 * "year" : "1970", "value" : -0.068 }, { "year" : "2000", "value" :
			 * 0.267 }, { "year" : "2001", "value" : 0.411 }, { "year" : "2004",
			 * "value" : 0.445 }, { "year" : "2005", "value" : 0.47 } ];
			 */

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
		.controller(
				'ChartOnlineController',
				[
						'$scope',
						function($scope) {
							$scope.someData = [];

							for (k = 0; k < 24; k++) {
								for (i = 0; i < 60; i++) {
									if (k < 10) {

										if (i < 10) {
											$scope.someData.push({
												"column-1" : Math.random() * 2
														+ Math.cos(i / 12) * 15
														+ 5,
												"column-2" : Math.random() * 2
														+ 10 - i * 0.3,
												"date" : "2014-03-02 0" + k
														+ ":0" + i + ":00"
											});
										} else {

											$scope.someData.push({
												"column-1" : Math.random() * 2
														+ Math.cos(i / 12) * 15
														+ 5,
												"column-2" : Math.random() * 2
														+ 10 - i * 0.3,
												"date" : "2014-03-02 0" + k
														+ ":" + i + ":00"
											});
										}

									} else {

										if (i < 10) {
											$scope.someData.push({
												"column-1" : Math.random() * 2
														+ Math.cos(i / 12) * 15
														+ 5,
												"column-2" : Math.random() * 2
														+ 10 - i * 0.3,
												"date" : "2014-03-02 " + k
														+ ":0" + i + ":00"
											});
										} else {

											$scope.someData.push({
												"column-1" : Math.random() * 2
														+ Math.cos(i / 12) * 15
														+ 5,
												"column-2" : Math.random() * 2
														+ 10 - i * 0.3,
												"date" : "2014-03-02 " + k
														+ ":" + i + ":00"
											});
										}

									}
								}
							}
						} ])
		.controller(
				'SettingsController',
				[
						'$scope',
						'Restangular',
						function($scope, Restangular) {
							$scope.serversList=[];
						/*	if ($scope.serversList == undefined) {
								$scope.serversList = [ {
									'id' : 1,
									'name' : 'Server 1',
									'ip' : '192.168.0.183',
									'port' : 1024,
									'description' : 'something'
								}, {
									'id' : 2,
									'name' : 'Server 2',
									'ip' : '192.168.0.200',
									'port' : 1024,
									'description' : 'something'
								} ];
							}*/

							// ---------get all servers-------------------
							var errorResponseFunctoin = function(response) {
								console.log(response.status);
								if (response.status == 405)
									alert("Do tej operacji ma uprawnienia tylko administrator!");

							}

							var baseServers = Restangular.all('servers');
							// callback
							baseServers.getList().then(function(servers) {
								$scope.serversList = servers;

							});

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
							
							$scope.addNewServer=function(newServer){
								baseServers.post(newServer).then(function(response){
									$scope.serversList.push(response);
									console.log("added id:"+response.id+"+ name: "+response.name+" ip: "+response.ip+" port: "+response.port+" timeInterval: "+response.timeInterval);

								},errorResponseFunctoin);
							}

							// --------update server-----------------
							$scope.preEditServer=function(server){
								$scope.updatedServer=server;
							}
							
							$scope.updateServer=function(server){
							//	var editedServer=Restangular.copy(server);
								
								//$scope.updateServer=server;
								server.put();
								
								
							}

							// ---------run server (run excecutor)----
							$scope.runServer = function(server) {
								server.post('executor').then(function() {
									console.log("run");
									alert("Zapis do bazy danych włączony");
									//angular.element()
								}, errorResponseFunctoin

								);
							}

							// ---------stop server (run excecutor)----
							$scope.stopServer = function(server) {
								var id = server.id;
								baseServers.customDELETE(id + "/executor").then(function(){
									
								},errorResponseFunctoin);
							}
						} ]);
