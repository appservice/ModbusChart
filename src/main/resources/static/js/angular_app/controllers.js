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
			
			$scope.chartTitle='Wykres z ostatnich 2 godzin';
			
			$scope.showChart=false;
			$scope.untilMaxDate=new Date();
			
			$scope.createChart=function(){				
			$scope.myMeasurements=[];		

			var baseMeasurements=Restangular.all('rest/servers/1/measurements');

			baseMeasurements.getList({"startDate":$scope.fromDate.getTime(),"endDate":$scope.untilDate.getTime()+1000*3600*24}).then(function(data){
				$scope.myMeasurements=data;
				
			});
		
			$scope.showChart=true;
			}
			
		}])
		

		
		
		 /**
		 * ================================================================================================
		 *  2 hours chart controller
		 * ================================================================================================
		 */
		.controller('TwoHoursChartController', [ '$scope','Restangular', function($scope,Restangular) {
			$scope.height='600px';
			$scope.chartTitle='Wykres z ostatnich 2 godzin';
			$scope.myMeasurements=[];

			var baseMeasurements=Restangular.all('rest/servers/1/measurements');
			baseMeasurements.getList({"timePeriod":2*60*60*1000}).then(function(data){
				
				$scope.myMeasurements=data;
				
			});
		
			


		} ])
		
		 /**
		 * ================================================================================================
		 *  8 hours chart controller
		 * ================================================================================================
		 */
		.controller('EightHoursChartController', [ '$scope','Restangular', function($scope,Restangular) {
			$scope.height='600px';
			$scope.chartTitle='Wykres z ostatnich 8 godzin';
			$scope.myMeasurements=[];

			var baseMeasurements=Restangular.all('rest/servers/1/measurements');
			baseMeasurements.getList({"timePeriod":8*60*60*1000}).then(function(data){
				$scope.myMeasurements=data;
				console.log(data);
				
			});
		
			


		} ])
		
		
		/**
		 * ================================================================================================
		 * Day chart controller
		 * ================================================================================================
		 */
		.controller('DayChartController', [ '$scope','Restangular', function($scope,Restangular) {
			$scope.height='600px';
			$scope.chartTitle='Wykres z ostatnich 24 godzin';
			
			$scope.myMeasurements=[];

			var baseMeasurements=Restangular.all('rest/servers/1/measurements');
			baseMeasurements.getList({"timePeriod":24*60*60*1000}).then(function(data){
				$scope.myMeasurements=data;
				console.log(data);
				
			});			


		} ])
				
		/**
		 * ================================================================================================
		 * 7 days chart controller
		 * ================================================================================================
		 */
		.controller('SevenDaysChartController', [ '$scope','Restangular', function($scope,Restangular) {
			$scope.height='600px';
			$scope.chartTitle='Wykres z ostatnich 7 dni';
			
			$scope.myMeasurements=[];

			var baseMeasurements=Restangular.all('rest/servers/1/measurements');
			baseMeasurements.getList({"timePeriod":7*24*60*60*1000}).then(function(data){
				$scope.myMeasurements=data;
				
			});			


		} ])
					
		/**
		 * ================================================================================================
		 * 31 days chart controller
		 * ================================================================================================
		 */
		.controller('ThirtyOneDaysChartController', [ '$scope','Restangular', function($scope,Restangular) {
			$scope.height='600px';
			$scope.chartTitle='Wykres z ostatnich 31 dni';
			
			$scope.myMeasurements=[];

			var baseMeasurements=Restangular.all('rest/servers/1/measurements');
			baseMeasurements.getList({"timePeriod":31*24*60*60*1000}).then(function(data){
				$scope.myMeasurements=data;
				
			});			


		} ])
		/**
		 * ================================================================================================
		 * All data chart controller
		 * ================================================================================================
		 */
		.controller('AllDataChartController',['$scope','Restangular',function($scope,Restangular){
/*			$scope.myMeasurements=[];
			
			
			
			//var baseMeasurements=Restangular.all('rest/servers/1/measurements');
			Restangular.one('rest/servers/1/').customGET('measurements',{"timePeriod":31*24*60*60*1000}).then(function(data){
				$scope.myMeasurements=data;
				
			});
		
			$scope.height='600px';*/
			
			$scope.isLoading=true;
			//$scope isVisible1=true;
	        
			Restangular.one('rest/servers/1/').customGET('measurements'/*,{"timePeriod":31*24*60*60*1000}*/).then(function(data){
				$scope.isLoading=false;
			
				var g={};
			    	  console.log("ilość danych: "+data.length);
						var pushData=[];
					 	var labelsTable=["Czas"];
					 	
					 
				      	for(var ii=1;ii<9;ii++){
				      		labelsTable.push("Czujnik-"+ii);
				      	}
				      	
					     
				        for(var k=0;k<data.length;k++){
				        	var myData=[]
				        	myData.push(new Date(data[k].date));
				        	for(var ll=0;ll<data[k].values.length;ll++)
				        		{
				        		myData.push(data[k].values[ll]);
				        		}
				        	pushData.push(myData);
				        }
				      	
						 g = new Dygraph(document.getElementById("div_g"), pushData,
		                          {
		                           // drawPoints: true,
		                        //    showRoller: true,
		                            strokeWidth: 2,
		                            colors:["#FF0000","#00AA00","#0000FF","#FF00FF","#9900FF","#FFCC00","#808080","#000080"],
		                          "labelsSeparateLines": true,
		                           // valueRange: [0.0, 1.2],
		                           labels: labelsTable,
		                           rightGap:100,
		                           title:"Wykres całościowy",
		                           ylabel:"Przepływ [m3/s]",
		                           //xlabel:"Czas",
		                           drawGapEdgePoints:true,
		                          // showRangeSelector: true,
		                         //  drawPoints: true,
		                          
		                          });
						
						
					//	g.setVisibility(1, false);
					//	g.setVisibility(2, false);
					//	g.setVisibility(3, false);
						
						
						$scope.isChecked1=true;
						$scope.setVisibility1=   function(){
					//	$scope.isVisible1!=$scope.isVisible1;
					//	g.setVisibility(0, $scope.isVisible1);
							console.log($scope.isChecked1);
							g.setVisibility(0,$scope.isChecked1);
					//	console.log($scope.isVisible1);
						    }

			        
					});
	
			
		}])

		/**
		 * ============================================================================================================
		 * chart online controller
		 * ======================================================================================================
		 */
		.controller('ChartOnlineController',['$scope','Restangular','poller', function($scope,Restangular,poller) {
		/*	$scope.myMeasurements = [];
			$scope.mySeriesNumber=8;
			$scope.height='600px';
			
		Restangular.one('rest/servers',1).get().then(function(myServer){
			console.log(myServer);
			$scope.mySeriesNumber=myServer.readedDataCount||0;
			$scope.serverTimeIterval=myServer.timeInterval;
			var myDate=new Date();
			var dataTableSize=60;
			
			for(j=0;j<dataTableSize;j++){
				$scope.myMeasurements.push({"date":myDate.getTime()-(dataTableSize-j)*$scope.serverTimeIterval,"values":[]});
				
				
				
			}

			console.log($scope.myMeasurements);
			
				 myServer.customGET("measurements/last").then(function(lastMeasurementData){
				//	 $scope.lastData=lastMeasurementData;
					// $scope.myMeasurements.push(lastMeasurementData);
				//	 console.log(lastMeasurementData);
				

			
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
								
							//	console.log($scope.myMeasurements);
							}else{
								console.log("niedodamy");
							}
						});
					   	
				 
			 });
				 
		 });*/
			
			
			
			Restangular.one('rest/servers',1).get().then(function(myServer){
				console.log(myServer);
				//$scope.mySeriesNumber=myServer.readedDataCount||0;
				$scope.serverTimeIterval=myServer.timeInterval;
				var myDate=new Date();
				var dataTableSize=60;
				
				for(j=0;j<dataTableSize;j++){
					$scope.myMeasurements.push({"date":myDate.getTime()-(dataTableSize-j)*$scope.serverTimeIterval,"values":[]});
								
				}
				
			
					$scope.isLoading=true;
	        

					myServer.customGET("measurements/last").then(function(lastMeasurementData){
		
	
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
						
					//	console.log($scope.myMeasurements);
					}else{
						console.log("niedodamy");
					}
				});
			   	
		 
	
	});

			Restangular.one('rest/servers/1/').customGET('measurements'/*,{"timePeriod":31*24*60*60*1000}*/).then(function(data){
				$scope.isLoading=false;
			    	  console.log("ilość danych: "+data.length);
						var pushData=[];
					 	var labelsTable=["Czas"];
					 	
					 
				      	for(var ii=1;ii<9;ii++){
				      		labelsTable.push("Czujnik-"+ii);
				      	}
				      	
					     
				        for(var k=0;k<data.length;k++){
				        	var myData=[]
				        	myData.push(new Date(data[k].date));
				        	for(var ll=0;ll<data[k].values.length;ll++)
				        		{
				        		myData.push(data[k].values[ll]);
				        		}
				        	pushData.push(myData);
				        }
				      	
						var g = new Dygraph(document.getElementById("div_g"), pushData,
		                          {
		                           // drawPoints: true,
		                        //    showRoller: true,
		                            strokeWidth: 2,
		                            colors:["#00DD55","#DD0055","#cc3300","#5500DD","#660066","#00FF00","#FF0000","#0000FF"],
		                          "labelsSeparateLines": true,
		                           // valueRange: [0.0, 1.2],
		                           labels: labelsTable,
		                           rightGap:100,
		                           title:"Wykres całościowy",
		                           ylabel:"Przepływ [m3/s]",
		                           //xlabel:"Czas",
		                           drawGapEdgePoints:true,
		                          // showRangeSelector: true,
		                         //  drawPoints: true,
		                          
		                          });

			        
			});
			    
			});
	
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
			
			//$scope.chartTitle='Wykres z ostatnich 2 godzin';
			
			//$scope.myData=[[new Date("2013-01-01"),12,13],[new Date("2014-01-01"),52,21]];
			
		//	$scope.showChart=false;
		//	$scope.untilMaxDate=new Date();
			
		//	$scope.createChart=function(){				
		//	$scope.myMeasurements=[];	
			
			
			/* $scope.graph = {
			            data: [
			            ],
			            options: {
			                labels: ["x", "A", "B","c"]
			            },
			            legend: {
			                series: {
			                    A: {
			                        label: "Series A"
			                    },
			                    B: {
			                        label: "Series B",
			                        format: 3
			                    },
			                    c: {
			                        label: "Series c",
			                        format: 3
			                    }
			                }
			            }
			        };

			        var base_time = Date.parse("2008/07/01");
			        var num = 24 * 8*4* 365;
			        console.log(num);
			        for (var i = 0; i < num; i++) {
			            $scope.graph.data.push([ new Date(base_time + i * 3600 * 1000),
			                        i + 50 * (i % 40),        // line
			                        i * (num - i) * 4.0 / num , // parabola
			                        Math.sin(i/12)*50
			                       
			            ]);
			            //console.log(i);
			        }
			        console.log("skonoczne");*/
			

			//var baseMeasurements=Restangular.all('rest/servers/1/measurements');
			//baseMeasurements.getList({"timePeriod":31*24*60*60*1000}).then(function(data){
			//	$scope.myMeasurements=data;
				//console.log(data);
				/*for(var i=0;i<data.length;i++){
					var myDataElement=[];
					myDataElement.push(data[i].date);
					
					for(var j=0;j<data[i].values.length;j++){
						myDataElement.push(data[i].values[j]);
					}
					$scope.myData.push(myDataElement);
				}*/
				
				
		//	});			

		
		//	$scope.showChart=true;
			//}
			
			//$(document).ready(function () {
			   /*   var pushData = [];*/
	
			    
			      
			        
			        
			Restangular.one('rest/servers/1/').customGET('measurements'/*,{"timePeriod":31*24*60*60*1000}*/).then(function(data){
				
			    	  console.log("ilość danych: "+data.length);
						var pushData=[];//[[new Date("2015-01-12"),"null","null","null","null","null","null","null","null"]];
					 	var labelsTable=["Czas"];
					 	
					 
				      	for(var ii=1;ii<9;ii++){
				      		labelsTable.push("Czujnik-"+ii);
				      	}
				      	
					     
				        for(var k=0;k<data.length;k++){
				        	var myData=[]
				        	myData.push(new Date(data[k].date));
				        	for(var ll=0;ll<data[k].values.length;ll++)
				        		{
				        		myData.push(data[k].values[ll]);
				        		}
				        	pushData.push(myData);
				        }
				      	
						var g = new Dygraph(document.getElementById("div_g"), pushData,
		                          {
		                           // drawPoints: true,
		                        //    showRoller: true,
		                            strokeWidth: 2,
		                            colors:["#00DD55","#DD0055","#cc3300","#5500DD","#660066","#00FF00","#FF0000","#0000FF"],
		                          "labelsSeparateLines": true,
		                           // valueRange: [0.0, 1.2],
		                           labels: labelsTable,
		                           rightGap:100,
		                           title:"Wykres całościowy",
		                           ylabel:"Przepływ [m3/s]",
		                           //xlabel:"Czas",
		                           drawGapEdgePoints:true,
		                          
		                          // showRangeSelector: true,
		                         //  drawPoints: true,
		                          
		                          });
						
					
	/*	     
			        for(var k=0;k<data.length;k++){
			        	var myData=[]
			        	myData.push(new Date(data[k].date));
			        	for(var ll=0;ll<data[k].values.length;ll++)
			        		{
			        		myData.push(data[k].values[ll]);
			        		}
			        	pushData.push(myData);
			        }*/
			        //console.log(pushData);
			     //   g.updateOptions( { 'file': pushData/*,showRangeSelector: true*/ } );
			        
					});
			    
			
		}]);
		
