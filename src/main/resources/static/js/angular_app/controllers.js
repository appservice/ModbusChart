/**
 * 
 */
angular
		.module('myApp.controllers', [])
		
		/**
		 * ================================================================================================
		 * Day chart controller
		 * ================================================================================================
		 */
		.controller('DayChartController', [ '$scope','Restangular', function($scope,Restangular) {
			$scope.myMeasurements=[];

			var baseMeasurements=Restangular.all('servers/1/measurements');
			baseMeasurements.getList({"timePeriod":24*60*60*1000}).then(function(data){
				$scope.myMeasurements=data;
				
			});
		
			$scope.height='600px';


		} ])

		// ------------------------------------------------------------------------------------------
		.controller('HourChartController', [ '$scope','Restangular', function($scope,Restangular) {
			$scope.myMeasurements=[];

			var baseMeasurements=Restangular.all('servers/1/measurements');
			baseMeasurements.getList({"timePeriod":60*60*1000}).then(function(data){
				$scope.myMeasurements=data;
				
			});
		
			$scope.height='500px';

			

		} ])
		
		.controller('AllDataChartController',['$scope','Restangular',function($scope,Restangular){
			$scope.myMeasurements=[];
			
			var baseMeasurements=Restangular.all('servers/1/measurements');
			baseMeasurements.getList().then(function(data){
				$scope.myMeasurements=data;
				
			});
		
			$scope.height='600px';

			
		}])

		/**
		 * ============================================================================================================
		 * chart online controller
		 * ======================================================================================================
		 */
		.controller('ChartOnlineController',['$scope','Restangular','poller', function($scope,Restangular,poller) {
			$scope.myMeasurements = [];
		//	$scope.mySeriesNumber=0;
			
		Restangular.one('servers',1).get().then(function(myServer){
			console.log(myServer);
			$scope.mySeriesNumber=myServer.readedDataCount||0;
			$scope.serverTimeIterval=myServer.timeInterval;
			var myDate=new Date();
			var dataTableSize=60;
			
			for(j=0;j<dataTableSize;j++){
				$scope.myMeasurements.push({"date":myDate.getTime()-(dataTableSize-j)*$scope.serverTimeIterval,"measuredValue":[]});
				
				
				
			}

			console.log($scope.myMeasurements);
			
				 myServer.customGET("measurements/last").then(function(lastMeasurementData){
				//	 $scope.lastData=lastMeasurementData;
					// $scope.myMeasurements.push(lastMeasurementData);
					 console.log(lastMeasurementData);
				

			
				 //----------------poller----------------
				 
				    var myPoller = poller.get(myServer.one("measurements/last") , { //Restangular.getOne('servers/2/measurements/last').getList() 
				   	action: 'get',
					        
					        delay: $scope.serverTimeIterval});
				 
			
					   	myPoller.promise.then(null, null,function(myData){
					   		
					   		

							console.log("tekst "+new Date());
							if ($scope.myMeasurements[$scope.myMeasurements.length-1].date!=myData.date){
								console.log("dodamy");
								//$scope.myData.shift();
								
								$scope.myMeasurements.shift();
								$scope.myMeasurements.push(myData);
								
								console.log($scope.myMeasurements);
							}else{
								console.log("niedodamy");
							}
						});
					   	
				 
			 });
				 
		 });
	//	});
		
		//	console.log(getLastMeasurement);
			 
			 // Get poller.
			 
	


			    

		}])
		
		/**
		 * ============================================================================================================
		 * download controller
		 */
		.controller('DownloadController',['$scope','Restangular',function($scope,Restangular){
			$scope.downloadData=function(){
				/*Restangular.one('servers/1/measurements/download/excel').get().then(function(){
					
				});*/
			}
			$scope.getAllData='rest/servers/1/measurements/download/excel';
		}])
		
		/**
		 * ============================================================================================================
		 * Custom period chart controller
		 * ============================================================================================================
		 */
		.controller('CustomPeriodChartController',['$scope','Restangular',function($scope,Restangular){
			$scope.showChart=false;
			
			$scope.today = function() {
			    $scope.dt = new Date();
			  };
			  $scope.today();

			  $scope.clear = function () {
			    $scope.dt = null;
			  };

			  // Disable weekend selection
			  $scope.disabled = function(date, mode) {
			    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
			  };

			  $scope.toggleMin = function() {
			    $scope.minDate = $scope.minDate ? null : new Date();
			  };
			  $scope.toggleMin();

			  $scope.open = function($event) {
			    $event.preventDefault();
			    $event.stopPropagation();

			    $scope.opened = true;
			  };

			  $scope.dateOptions = {
			    formatYear: 'yy',
			    startingDay: 1
			  };

			  $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
			  $scope.format = $scope.formats[0];
			
			
			
			$scope.createChart=function(){
				
			$scope.myMeasurements=[];

			var baseMeasurements=Restangular.all('servers/1/measurements');
			baseMeasurements.getList({"timePeriod":60*60*1000}).then(function(data){
				$scope.myMeasurements=data;
				
			});
		
			$scope.height='500px';
			$scope.showChart=true;
			}
			
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
