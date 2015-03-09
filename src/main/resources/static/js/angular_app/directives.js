/**
 * 
 */

angular
		.module('myApp.directives', [])
		.directive(
				'lukeAmChart',
				function($filter) {
					var directive = {};

					// restrict
					directive.restrict = 'EA';

					directive.replace = true;
					// scope
					directive.scope = {
						chartData : '=',
						seriesNumber : '=',
						height : '=',
						valueAxisTitle : '=',
						chartTitle : '=',
						lineThickness : '='
					};

					// templete
					directive.template = '<div id="chartdiv" style="min-width: 310px; height:{{height}}; margin: 0 auto"></div>';

					// link
					directive.link = function(scope, element, attrs) {

						var chart = false;
						var dataForChart = [];

						scope.$watchCollection(
										'chartData',
										function(newChartData, oldChartData) {
											console.log(newChartData);
											
			
											// this functions adapted my data to
											// data needed by amCharts
											
										var dataForChart=adaptMyData(scope.chartData);

											// --------------------amChart
											// object------------------
											var myChart = {
												"type" : "serial",
												"theme" : "none",
												"language":"pl",
												"marginLeft" : 20,
												"pathToImages" : "http://www.amcharts.com/lib/3/images/",
												"decimalSeparator" : ",",
												"thousandsSeparator" : ".",
												"dataProvider" : dataForChart,
												"valueAxes" : [ {
													"axisAlpha" : 0,
													"inside" : true,
													"position" : "left",
													"ignoreAxisWidth" : true,
													"id" : "ValueAxis-1",
													"title" : scope.valueAxisTitle||""
												} ],
												"graphs" : [],

												"chartScrollbar" : {},
												"chartCursor" : {
													"categoryBalloonDateFormat" : "YYYY",
													"cursorAlpha" : 0,
													"cursorPosition" : "mouse"
												},

												"categoryField" : "date",
												"dataDateFormat" : "YYYY-MM-DD HH:NN:SS",//:SS
												"categoryAxis" : {
													"minPeriod" : "ss",
													"parseDates" : true,
													"minorGridAlpha" : 0.1,
													"minorGridEnabled" : true
												},
												"chartCursor" : {
													"categoryBalloonDateFormat" : "JJ:NN:SS"//:SS
												},
												"legend" : {
													"useGraphSettings" : true,
													"spacing" : 32,
													"valueWidth" : 150,
													"valueAlign" : "left"
												},
												"titles" : [ {
													"id" : "Title-1",
													"size" : 15,
													"text" : scope.chartTitle||""
												} ],
												"amExport" : {
													"exportPNG" : true,
													"imageFileName" : "wykres",
													"buttonTitle" : "Zapisz wykres jako obraz .png"
												}

											};

											// -------------------------grpahs--------------------------------------------
											var graphNumber=scope.seriesNumber||0;
											if(scope.chartData.length>0)
												graphNumber=scope.seriesNumber||scope.chartData[0].values.length;
											
											
											for (var g = 0; g <graphNumber; g++) { //
												myChart.graphs
														.push({
															"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",
															"id" : "AmGraph-"+ (g + 1),																	
															"title" : "Czujnik "+ (g + 1),																	
															"valueField" : "column-"+ (g + 1),																	
															"lineThickness" : scope.lineThickness || 1,
															/*"connect":false*/
														});
											}

											myChart.amExport.imageFileName = "wykres_"
													+ (new Date())
															.toLocaleString();

											// function makeChart-------
											var chart = AmCharts.makeChart(
													"chartdiv", myChart);

										});

					};

					
					
				
					/**
					 * function for adapting my data json object to amChart object
					 */
					var adaptMyData = function(myDataTable) {
						var dataForChart=[];
						//console.log(myDataTable);
						for (var i = 0; i < myDataTable.length; i++) {

							var myDate = new Date(myDataTable[i].date);
							var myStringDate = $filter('date')(myDate,
									"yyyy-MM-dd HH:mm:ss");//:ss

							var dataToDisplay = {};

							// dynamically create object
							// with measured value
							
							for (var j = 0; j < myDataTable[i].values.length; j++) {
	
								dataToDisplay["column-" + (j + 1)] = myDataTable[i].values[j];
							}
							
							dataToDisplay["date"] = myStringDate;

							dataForChart.push(dataToDisplay);
						}
						return dataForChart;

					}

					
					
					return directive;

				});
