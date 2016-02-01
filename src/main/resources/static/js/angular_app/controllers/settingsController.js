/**
 * ============================================================================================================
 * SettingsController -for site settings
 */
angular
		.module('myApp.controllers')
		.controller(
				'SettingsController',
				['$rootScope',
						'$scope',
						'Restangular',
						'$timeout',
						function($rootScope,$scope, Restangular, $timeout) {
							$scope.isButtonShowed = false;
							$scope.isButtonDisabled=false;
							
							
							$scope.serversList = [];
							$scope.readedDataTypes = [ {
								"type" : "FLOAT",
								"name" : "FLOAT (32 bit)"
							}, {
								"type" : "INTEGER",
								"name" : "INTEGER (16 bit)"
							} ];

							$scope.securityRoles = [ {
								"type" : "ROLE_USER",
								"name" : "Użytkownik"
							}, {
								"type" : "ROLE_ADMIN",
								"name" : "Administrator"
							} ];
							
							$scope.users = [];

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
									$scope.isButtonShowed = true;
								}

							},$rootScope.errorView);

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
										$scope.isButtonShowed = false;
									}, errorResponseFunctoin);
								}
							}

							// --------add new server------------------

							$scope.addNewServer = function(newServer) {
								newServer.id = 1;
								baseServers.post(newServer).then(
										function(response) {
											$scope.serversList.push(response);
											$scope.isButtonShowed = true;
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

								server.put();

							}

							// ----------------add sensors----------------
							$scope.addSensorsName = function(server, sensorName) {
								server.sensorsName.push(sensorName);
								console.log(sensorName);
								server.put();

							}

							// ----------------add sensors----------------
							$scope.deleteSensorsName = function(server, id) {
								server.sensorsName.splice(id, 1);
								console.log(id);
								server.put();

							}
							// $scope.server.sensorsName=[];

							// ---------run server (run excecutor)----
							$scope.runServer = function(server) {
								
								
								var id = server.id;
								server.post('executor').then(
										function() {
											console.log("started");
											$scope.isButtonDisabled=true;

											// -----------checking if the server is running or occurred error
											var executeGet = function() {

												baseServers.customGET(id + "/executor").then(
														function(response) {
															// console.log(response);
															if (response.connectedToServer) {
																alert("Zapis do bazy danych z serwera: " + server.name + " włączony!");

															} else {
																// console.log(response);
																$scope.isButtonDisabled=false;
																alert("Wystąpił błąd podczas połączenia z serwerem: " + server.name + "\n"
																		+ response.errorMessage + "!");
																

															}

														});

											}
											$timeout(executeGet, 2000);

										}, errorResponseFunctoin

								);

							}

							// ---------stop server (run excecutor)----
							$scope.stopServer = function(server) {
								var doStop = confirm("Czy na pewno chcesz zatrzymać zapis?\n");
								if (doStop == true) {
									var id = server.id;
									baseServers.customDELETE(id + "/executor").then(function() {
										alert("Nasłuchiwanie i zapis danych do bazy wyłączony!")

									}, errorResponseFunctoin);
								}
							}

							$scope.switchOffServer = function() {
								var doStopApplication = confirm("Czy na pewno chcesz zatrzymać apliację?\nPonownie włączyć można tylko bezpośrednio na serwerze!");
								if (doStopApplication) {
									Restangular.one("shutdown").customPOST().then(function(data) {
										$window.alert("Aplikacja wyłączona");
									});
								}
							}

							var usersBase = Restangular.all("rest/users");
							usersBase.getList().then(function(data) {
								$scope.users = data;
							});

							$scope.deleteUser = function(user) {
								console.log(user.plain());

								var doRemove = confirm("Czy na pewno chcesz usunąć " + user.userName + "?\n");

								if (doRemove == true) {

									user.remove().then(function() {
										var index = $scope.users.indexOf(user);
										if (index > -1)
											$scope.users.splice(index, 1);
										console.log("user removed");
									}, function() {
										console.log("not removed");
									});
								}
							}
							
							$scope.addUser = function(user) {
								
								//user.roles = [ "ROLE_USER" ];
								user.enabled = true;
								usersBase.post(user).then(function(response) {
									console.log(response);
									$scope.users.push(response);
									$scope.newUser={};
								}, errorResponseFunctoin);
							}

							// --------update server-----------------
							$scope.preEditUser = function(user) {
								$scope.updatedUser = user;
								console.log(user);
							}
							$scope.updateUser = function(user) {
								// user.roles = [ "ROLE_USER" ];
								// user.enabled = true;
								user.put().then(function(response) {
									console.log(response);
									// $scope.users.push(response);
								}, errorResponseFunctoin);
							}
							
							
							$scope.updateCurrentPrice=function(currentPrice){
								//currentPrice.put();
								Restangular.one("rest/preferences",'currentPrice').customPUT(currentPrice).then(function(){
									$rootScope.preferences.currentPrice=currentPrice;
									
								});
							}
			
						} ]);
