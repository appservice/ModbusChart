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
							chartData : '=',
							seriesNumber : '=',
							height : '='
						},

						template : '<div id="chartdiv" style="min-width: 310px; height:{{height}}; margin: 0 auto"></div>',
						link : function(scope, element, attrs) {

							var chart = false;

							scope
									.$watchCollection(
											'chartData',
											function(newChartData, oldChartData) {

												// --------------------amChart
												// object--------------------------------------------------------
												var myChart = {
													"type" : "serial",
													"theme" : "none",
													"marginLeft" : 20,
													"pathToImages" : "http://www.amcharts.com/lib/3/images/",
													"decimalSeparator" : ",",
													"thousandsSeparator" : ".",
													"dataProvider" : scope.chartData,// scope.chartData,
													"valueAxes" : [ {
														"axisAlpha" : 0,
														"inside" : true,
														"position" : "left",
														"ignoreAxisWidth" : true
													} ],
													"graphs" : [],

													"chartScrollbar" : {},
													"chartCursor" : {
														"categoryBalloonDateFormat" : "YYYY",
														"cursorAlpha" : 0,
														"cursorPosition" : "mouse"
													},

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
													"legend" : {
														"useGraphSettings" : true,
														"spacing" : 32,
														"valueWidth" : 150,
														"valueAlign" : "left"
													},
													"amExport" : {
														"exportPNG" : true,
														"imageFileName" : "wykres",
														"buttonTitle" : "Zapisz wykres jako obraz .png"
													}

												};

												// -------------------------grpahs--------------------------------------------
												for (var g = 0; g < scope.seriesNumber; g++) {
													myChart.graphs
															.push({
																"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",
																"id" : "AmGraph-"
																		+ (g + 1),
																"title" : "Czujnik "
																		+ (g + 1),
																"valueField" : "column-"
																		+ (g + 1)
															});
												}

												myChart.amExport.imageFileName = "wykres_"
														+ (new Date())
																.toLocaleString();

												// function makeChart-------
												var chart = AmCharts.makeChart(
														"chartdiv", myChart);

											});

						}

					}
				});
