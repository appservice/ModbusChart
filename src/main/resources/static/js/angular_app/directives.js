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
						// chartType : '@?'
						},

						template : '<div id="chartdiv" style="min-width: 310px; height: 500px; margin: 0 auto"></div>',
						link : function(scope, element, attrs) {

							var chart = false;

							scope
									.$watchCollection(
											'chartData',
											function(newChartData, oldChartData) {

												var chart = AmCharts
														.makeChart(
																"chartdiv",
																{
																	"type" : "serial",// scope.chartType,//
																						// serial
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

																	"graphs" :

																	[

																			{
																				"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",

																				"id" : "AmGraph-1",
																				"title" : "Czujnik 1",
																				"valueField" : "column-1"
																			},
																			{
																				"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",

																				"id" : "AmGraph-3",
																				"title" : "Czujnik 3",
																				"valueField" : "column-3"
																			},
																			{
																				"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",

																				"id" : "AmGraph-4",
																				"title" : "Czujnik 4",
																				"valueField" : "column-4"
																			},
																			{
																				"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",

																				"id" : "AmGraph-5",
																				"title" : "Czujnik 5",
																				"valueField" : "column-5"
																			},
																			{
																				"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",

																				"id" : "AmGraph-6",
																				"title" : "Czujnik 6",
																				"valueField" : "column-6"
																			},
																			{
																				"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",

																				// "bullet"
																				// :
																				// "square",
																				// "bulletSize":1,
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

																});
												// }
											});

							// var now = new Date().toISOString();
							// chart.amExport.imageFileName = "wykres_" + now;

						},
						controller : function($scope) {
							$scope.chartType = $scope.chartType || 'serial';

						}

					}
				});
