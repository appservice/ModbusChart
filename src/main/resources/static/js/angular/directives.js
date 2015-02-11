/**
 * 
 */

angular
		.module('myApp.directives', [])
		.directive(
				'lukeAmChart',
				function() {
					return {
						restrict : 'E',
						replace : true,
						scope : {
							chartData : '=data',
							chartType : '@?'
						},

						template : '<div id="chartdiv" style="min-width: 310px; height: 500px; margin: 0 auto"></div>',
						link : function(scope, element, attrs) {

							var chart = false;

							/*				*/

							/*
							 * chartData.push( { "year" : "2006", "value" : 0.37
							 * }); // var initChart = function() {
							 */

							if (chart)
								chart.destroy();
							var config = scope.config || {};
							var chart = AmCharts
									.makeChart(
											"chartdiv",
											{
												"type" : scope.chartType,// serial
												"theme" : "none",
												"marginLeft" : 20,
												"pathToImages" : "http://www.amcharts.com/lib/3/images/",
												"decimalSeparator" : ",",
												"thousandsSeparator" : ".",
												"dataProvider" : scope.chartData,
												"valueAxes" : [ {
													"axisAlpha" : 0,
													"inside" : true,
													"position" : "left",
													"ignoreAxisWidth" : true
												} ],
												/*
												 * "graphs" : [ { "balloonText" :
												 * "[[category]]<br><b><span
												 * style='font-size:14px;'>[[value]]</span></b>",
												 * "bullet" : "round",
												 * "bulletSize" : 6, "lineColor" :
												 * "#d1655d", "lineThickness" :
												 * 2, "negativeLineColor" :
												 * "#637bb6", "type" :
												 * "smoothedLine", "valueField" :
												 * "value" } ],
												 */

												"graphs" : [
														{
															"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",
																
															//"bullet" : "round",
															//"bulletSize":1,
															"id" : "AmGraph-1",
															"title" : "Czujnik 1",
															"valueField" : "column-1"
														},
														{
															"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",
																
														//	"bullet" : "square",	
														//	"bulletSize":1,
															"id" : "AmGraph-2",
															"title" : "Czujnik 2",
															"valueField" : "column-2"
														} ],

												"chartScrollbar" : {},
												"chartCursor" : {
													"categoryBalloonDateFormat" : "YYYY",
													"cursorAlpha" : 0,
													"cursorPosition" : "mouse"
												},
												/*
												 * "dataDateFormat" : "YYYY",
												 * "categoryField" : "year",
												 */

												"categoryField" : "date",
												"dataDateFormat" : "YYYY-MM-DD HH:NN:SS",
												"categoryAxis" : {
													"minPeriod" : "ss",
													"parseDates" : true,
													 "minorGridAlpha" : 0.1,
													  "minorGridEnabled" : true 
												},
												"chartCursor" : {
													"categoryBalloonDateFormat" : "JJ:NN:SS"
												},
												"legend": {
										            "useGraphSettings": true,
										            "spacing":32,
										            "valueWidth":150,
										            "valueAlign": "left"
										          },
										          "amExport": {
										              "exportPNG" : true,
										              "imageFileName" : "wykres",
										              "buttonTitle" : "Zapisz wykres jako obraz .png"
										              }

											/*
											 * "categoryAxis" : { "minPeriod" :
											 * "YYYY", "parseDates" : true,
											 * "minorGridAlpha" : 0.1,
											 * "minorGridEnabled" : true }
											 */
											});
							
							 var now=new Date().toISOString();
							 chart.amExport.imageFileName="wykres_"+now;

							// initChart();

							/*
							 * scope.$watch('chartData', function(val) { if (val ==
							 * undefined || val == null) return;
							 * chart.dataProvider = val; chart.validateData();
							 * });
							 */
							//console.log(chart);

						},
						controller : function($scope) {
							$scope.chartType = $scope.chartType || 'serial';

						}

					}
				});

/*
 * .directive('amChart', function() { return { restrict : "E", transclude :
 * true, // scope: {}, controller : function($scope) { this.addGraph =
 * function(graph) { $scope.chart.addGraph(graph); }
 * 
 * this.validateData = function() { $scope.chart.validateData();
 * $scope.chart.validateNow(); } }, template : "<div id='chart'></div>", link :
 * function(scope, element, attrs) { var chart = new AmCharts.AmSerialChart();
 * chart.categoryField = "year"; chart.plotAreaBorderAlpha = 1; // AXES //
 * category var categoryAxis = chart.categoryAxis; categoryAxis.gridAlpha = 0.1;
 * categoryAxis.axisAlpha = 0; categoryAxis.gridPosition = "start"; // var graph =
 * new AmCharts.AmGraph(); // graph.title = "Europe"; // graph.labelText =
 * "[[value]]"; // graph.valueField = "europe"; // graph.type = "column"; //
 * graph.lineAlpha = 0; // graph.fillAlphas = 1; // graph.lineColor = "#C72C95"; //
 * graph.balloonText = "<span // style='color:#555555;'>[[category]]</span><br><span //
 * style='font-size:14px'>[[title]]:<b>[[value]]</b></span>"; //
 * chart.addGraph(graph); // value var valueAxis = new AmCharts.ValueAxis();
 * valueAxis.stackType = "regular"; valueAxis.gridAlpha = 0.1;
 * valueAxis.axisAlpha = 0; chart.addValueAxis(valueAxis); // LEGEND var legend =
 * new AmCharts.AmLegend(); legend.borderAlpha = 0.2; legend.horizontalGap = 10;
 * chart.addLegend(legend);
 * 
 * chart.write("chart");
 * 
 * scope.chart = chart;
 * 
 * scope.$watch('chartData', function(val) { if (val == undefined || val ==
 * null) return; chart.dataProvider = val; chart.validateData(); });
 * 
 * scope.$watch('categoryField', function(val) { if (val == undefined || val ==
 * null) return; chart.categoryField = val; chart.validateData(); });
 * 
 * scope.$watch('plotAreaBorderAlpha', function(val) { if (val == undefined ||
 * val == null) return; chart.plotAreaBorderAlpha = val; chart.validateNow();
 * }); } }; })
 * 
 * .directive('amGraph', function() {
 * 
 * return { restrict : "E", require : "^amChart", replace : true, link :
 * function(scope, element, attrs, amChartController) { console.log('link'); var
 * graph = new AmCharts.AmGraph(); graph.title = "Europe"; graph.labelText =
 * "[[value]]"; graph.valueField = "europe"; graph.type = "column";
 * graph.lineAlpha = 0; graph.fillAlphas = 1; graph.lineColor = "#C72C95"; //
 * graph.balloonText = "<span // style='color:#555555;'>[[category]]</span><br><span //
 * style='font-size:14px'>[[title]]:<b>[[value]]</b></span>"; // add to
 * parent via controller amChartController.addGraph(graph); // //
 * $scope.$watch('title', function(val) { // graph.title = val; //
 * amChartController.validateData(); // }); // // $scope.$watch('labelText',
 * function(val) { // graph.labelText = val; //
 * amChartController.validateData(); // }); // // $scope.$watch('valueField',
 * function(val) { // graph.valueField = val; //
 * amChartController.validateData(); // }); // $scope.$watch('type',
 * function(val) { // graph.type = val; // amChartController.validateData(); //
 * }); // $scope.$watch('lineAlpha', function(val) { // graph.lineAlpha = val; //
 * amChartController.validateData(); // }); // $scope.$watch('fillAlphas',
 * function(val) { // graph.fillAlphas = val; //
 * amChartController.validateData(); // }); // $scope.$watch('balloonText',
 * function(val) { // graph.balloonText = val; //
 * amChartController.validateData(); // }); } }; });
 */