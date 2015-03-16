/**
 * 
 */
angular
		.module('myApp.controllers', [])
		/**
		 * ============================================================================================================
		 * Custom period chart controller
		 * ============================================================================================================
		 */
		.controller('CustomPeriodChartController',['$scope','Restangular',function($scope,Restangular){
			
			$scope.chartTitle='Wykres z n dni';
			
			$scope.showChart=false;
			
			var today=new Date();
			var tomorrow=new Date();
			tomorrow.setDate(today.getDate()+1);	
			$scope.untilMaxDate=tomorrow;
			
			$scope.createChart=function(){				
				  $scope.myData=[];
				  $scope.maxSeriesNumber=8;
				    $scope.isLoading=true;
				    $scope.chartTitle='Wykres od: '+$scope.fromDate.toLocaleString()+' do '+$scope.untilDate.toLocaleString();  
				        
				        
				Restangular.one('rest/servers/1/').customGET('measurements',{"startDate":$scope.fromDate.getTime()
					,"endDate":$scope.untilDate.getTime()})
					
					.then(function(data){
						$scope.myData=data;
						$scope.isLoading=false;
						$scope.showChart=true;

				        
						},function(error){
							$scope.isLoading=false;
							switch(error.status){
							case 400:console.log('złe zapytanie');break;
							case 500:console.log('Bład serwera');break;
							case 0:console.log('Brak połączenia z serwerem');break;
							}
							console.log(error);
						});
		
			}
		
/*			$scope.myMeasurements=[];		

			var baseMeasurements=Restangular.all('rest/servers/1/measurements');

			baseMeasurements.getList({"startDate":$scope.fromDate.getTime(),"endDate":$scope.untilDate.getTime()+1000*3600*24}).then(function(data){
				$scope.myMeasurements=data;
				
			});*/
		
			
			
		}])
		

		
		
		 /**
		 * ================================================================================================
		 *  2 hours chart controller
		 * ================================================================================================
		 */
		.controller('TwoHoursChartController', [ '$scope','Restangular', function($scope,Restangular) {

			
		    $scope.myData=[];
		    $scope.maxSeriesNumber=8;
		    $scope.isLoading=true;
		    $scope.chartTitle='Wykres z ostatnich 2 godzin';  
		        
		        
		Restangular.one('rest/servers/1/').customGET('measurements',{"timePeriod":2*60*60*1000}).then(function(data){
			$scope.myData=data;
			$scope.isLoading=false;

		        
				});
		    


		} ])
		
		 /**
		 * ================================================================================================
		 *  8 hours chart controller
		 * ================================================================================================
		 */
		.controller('EightHoursChartController', [ '$scope','Restangular', function($scope,Restangular) {

			
		    $scope.myData=[];
		    $scope.maxSeriesNumber=8;
		    $scope.isLoading=true;
			$scope.chartTitle='Wykres z ostatnich 8 godzin'; 		        
		        
		Restangular.one('rest/servers/1/').customGET('measurements',{"timePeriod":8*60*60*1000}).then(function(data){
			$scope.myData=data;
			$scope.isLoading=false;
		        
			});
			


		} ])
		
		
		/**
		 * ================================================================================================
		 * Day chart controller
		 * ================================================================================================
		 */
		.controller('DayChartController', [ '$scope','Restangular', function($scope,Restangular) {
	
		    $scope.myData=[];
		    $scope.maxSeriesNumber=8;
			$scope.chartTitle='Wykres z ostatnich 24 godzin';
		    $scope.isLoading=true;
		      
		        
		        
		Restangular.one('rest/servers/1/').customGET('measurements',{"timePeriod":24*60*60*1000}).then(function(data){
			$scope.myData=data;
			$scope.isLoading=false;

		        
				});

		} ])
				
		/**
		 * ================================================================================================
		 * 7 days chart controller
		 * ================================================================================================
		 */
		.controller('SevenDaysChartController', [ '$scope','Restangular', function($scope,Restangular) {

		    $scope.myData=[];
		    $scope.maxSeriesNumber=8;
		    $scope.chartTitle='Wykres z ostatnich 7 dni';
		    $scope.isLoading=true;
		      
		        
		        
		Restangular.one('rest/servers/1/').customGET('measurements',{"timePeriod":7*24*60*60*1000}).then(function(data){
			$scope.myData=data;
			$scope.isLoading=false;

		        
				});


		} ])
					
		/**
		 * ================================================================================================
		 * 31 days chart controller
		 * ================================================================================================
		 */
		.controller('ThirtyOneDaysChartController', [ '$scope','Restangular', function($scope,Restangular) {
		    $scope.myData=[];
		    $scope.maxSeriesNumber=8;
		    $scope.isLoading=true;
		    $scope.chartTitle='Wykres z ostatnich 31 dni';  
		        
		        
		Restangular.one('rest/servers/1/').customGET('measurements',{"timePeriod":31*24*60*60*1000}).then(function(data){
			$scope.myData=data;
			$scope.isLoading=false;

		        
				});


		} ])
		/**
		 * ================================================================================================
		 * All data chart controller
		 * ================================================================================================
		 */
		.controller('AllDataChartController',['$scope','Restangular',function($scope,Restangular){
		    $scope.myData=[];
		    $scope.isLoading=true;
		    $scope.maxSeriesNumber=8;
		    $scope.chartTitle='Wykres z ostatnich 31 dni';  
		        
		        
		Restangular.one('rest/servers/1/').customGET('measurements').then(function(data){
			$scope.myData=data;
			$scope.isLoading=false;

		        
				});
			

						
					//	g.setVisibility(1, false);
					//	g.setVisibility(2, false);
					//	g.setVisibility(3, false);
						
						
			/*			$scope.isChecked1=true;
						$scope.setVisibility1=   function(){
					//	$scope.isVisible1!=$scope.isVisible1;
					//	g.setVisibility(0, $scope.isVisible1);
							console.log($scope.isChecked1);
							g.setVisibility(0,$scope.isChecked1);
					//	console.log($scope.isVisible1);
						    }*/

			        
				
	
			
		}])

		/**
		 * ============================================================================================================
		 * chart online controller
		 * ======================================================================================================
		 */
		.controller('ChartOnlineController',['$scope','Restangular','poller', function($scope,Restangular,poller) {
			
			
			
			
			
			$scope.myData = [];
			$scope.maxSeriesNumber=8;
			//$scope.mySeriesNumber=8;
		//	$scope.height='600px';
			
		Restangular.one('rest/servers',1).get().then(function(myServer){
			console.log(myServer);
			$scope.mySeriesNumber=myServer.readedDataCount||0;
			$scope.serverTimeIterval=myServer.timeInterval;
			var myDate=new Date();
			var dataTableSize=60;
			
			for(j=0;j<dataTableSize;j++){
				$scope.myData.push({"date":myDate.getTime()-(dataTableSize-j)*$scope.serverTimeIterval,"values":[null,null,null,null,null,null,null,null]});
				
				
				
			}


				 //----------------poller----------------
				 
				    var myPoller = poller.get(myServer.one("measurements/last") , { //Restangular.getOne('servers/2/measurements/last').getList() 
				   	action: 'get',
					        
					        delay: $scope.serverTimeIterval});
				 
			
					   	myPoller.promise.then(null, null,function(myData){
					   		
					   		//console.log(myData.plain());

							//console.log("tekst "+new Date());
							if ($scope.myData[$scope.myData.length-1].date!=myData.date){
								console.log("dodamy");
								//$scope.myData.shift();
								
								$scope.myData.shift();
								$scope.myData.push(myData.plain());
								
							//	console.log($scope.myMeasurements);
							}else{
								console.log("niedodamy");
								myPoller.stop();
								
							}
						});
					   	
				 
			 });
				 
		// });
			
			
		
	
		}])
		
		/**
		 * ============================================================================================================
		 * download controller
		 */
		.controller('DownloadController',['$scope','Restangular',function($scope,Restangular){
			$scope.downloadData=function(){
			
			}
			$scope.getAllData='rest/servers/1/measurements/download/excel';
		}])
		
		
		
		/**
		 * ============================================================================================================
		 * SettingsController -for site settings
		 */
		.controller(
				'SettingsController',
				[
						'$scope',
						'Restangular','$timeout',
						function($scope, Restangular,$timeout) {
							$scope.serversList = [];
							$scope.readedDataTypes=[
						           {"type":"FLOAT","name":"FLOAT (32 bit)"},
							          // {"type":"INTEGER","name":"INTEGER (16 bit)"}
						                        ];

							
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
									
								//	
									
									var executeGet=function(){
										
										baseServers.customGET(id+"/executor").then(function(response){
											//console.log(response);
											if(response.connectedToServer){
												alert("Zapis do bazy danych z serwera: "+server.name+ " włączony!");

											}else{
												//console.log(response);
												alert("Wystąpił błąd podczas połączenia z serwerem: "+ server.name+"\n" +
														response.errorMessage+"!");	

											}
										
										});
										
									}
									$timeout(executeGet,1000);
									
									
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
							
							$scope.switchOffServer=function(){
								Restangular.one("shutdown").customPOST().then(function(data){
									alert("Aplikacja wyłączona");
								});
							}
						} ])
						
						/**
		 * ============================================================================================================
		 * Help CONTROLLER
		 * ============================================================================================================
		 */
		.controller('HelpController',['$scope','Restangular',function($scope,Restangular){
	
	
			    $scope.myData=[];
			    $scope.isLoading=true;
			      
			        
			        
			Restangular.one('rest/servers/1/').customGET('measurements'/*,{"timePeriod":31*24*60*60*1000}*/).then(function(data){
				$scope.myData=data;
				$scope.isLoading=false;

			        
					});
			    
			
		}]);
		
