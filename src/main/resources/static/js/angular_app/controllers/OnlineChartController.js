/**
 * 
 */
/**
 * =============websocket controller======================
 */

angular
		.module('myApp.controllers')
		.controller(
				"OnlineChartController",
				function($scope, Restangular) {

					var currentLocation = window.location;
					console.log(currentLocation);
					$scope.myData = [];
					$scope.dataToShow = null;
					$scope.closeButtonText = "Stop";
					var websocket = null;
					$scope.squaresTable = [];
					var isFirstMessage = true;
					var server = null;
					var dataTableSize = 60;
					;// myServer.sensorsName;
					var wsUrl;
					if (window.location.protocol == 'http:') {
						wsUrl = 'ws://' + currentLocation.host
								+ '/ModbusChart/measurementHandler';
					} else {
						wsUrl = 'wss://' + currentLocation.host
								+ '/ModbusChart/measurementHandler';
					}
					// $scope.onStartWebsocket=function(){

					Restangular
							.one('rest/servers', 1)
							.get()
							.then(
									function(myServer) {
										$scope.is_loaded = true;
										$scope.seriesName = myServer.sensorsName;

										console.log("start is starting!");
										testWebsocket();

										// }

										function testWebsocket() {
											websocket = new WebSocket(wsUrl);

											websocket.onopen = function(evt) {
												onOpen(evt)
											};
											websocket.onmessage = function(
													message) {
												onMessage(message)
											};
											websocket.onclose = function(evt) {
												onClose(evt)
											};

											// websocket.onmessage =
											// function(evt) { onMessage(evt) };
											// websocket.onerror = function(evt)
											// { onError(evt) };
										}
										function onOpen(evt) {
											console.log("on open is on");
											console.log(evt);

										}
										function onMessage(message) {
											var returnedData = JSON
													.parse(message.data);

											if (!(returnedData instanceof Array)) {
												if (returnedData.ip != null) {
													server = returnedData;

												} else {
													// var obj = returnedData;
													// console.log(obj);

													if (isFirstMessage) {
														var emptyValues = [];
														for (var emp_i = 0; emp_i < server.sensorsName.length; emp_i++) {
															emptyValues[emp_i] = null;
														}
														// console.log(obj.date
														// - (dataTableSize
														// )*server.timeInterval);
														for (var j = 0; j < dataTableSize; j++) {
															$scope.myData
																	.push({
																		"date" : returnedData.date
																				- (dataTableSize - j)
																				* server.timeInterval,
																		"values" : emptyValues
																	});

														}

														isFirstMessage = false;
													}
													// }else{

													// console.log(new
													// Date(obj.date));
													// console.log(obj);

													if ($scope.myData.length > dataTableSize) {
														$scope.myData.shift();
													}

													$scope.dataToShow = returnedData;
													$scope.myData
															.push(returnedData);
													// console.log(returnedData);
													$scope.$apply('myData');
												}
											}else
												{
												console.log(returnedData);
												}
										}
										// console.log(message);
										// websocket.close();
										// }

										function onClose(evt) {
											console.log("Websocket Closed");

											// websocket=null;
										}

										// -----------switch off when destroy
										// controller-----------
										$scope
												.$on(
														"$destroy",
														function(event) {
															console
																	.log("destroyed");
															if (websocket.readyState == websocket.OPEN) {
																websocket
																		.close();
																$scope.closeButtonText = "Start";
															}
														})
										// -------toggle button
										// on/off-------------------
										$scope.onStartWebsocket = function() {
											if (websocket.readyState != websocket.CLOSED) {

												websocket.close();
												$scope.closeButtonText = "Start";
												// console.log("closed websocket
												// session");
												//
											} else {
												$scope.myData = [];
												isFirstMessage = true;
												testWebsocket();
												$scope.closeButtonText = "Stop"

											}
										}
									});
				});