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

															// "bullet" :
															// "round",
															// "bulletSize":1,
															"id" : "AmGraph-1",
															"title" : "Czujnik 1",
															"valueField" : "column-1"
														},
														{
															"balloonText" : "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",

															// "bullet" :
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

											/*
											 * "categoryAxis" : { "minPeriod" :
											 * "YYYY", "parseDates" : true,
											 * "minorGridAlpha" : 0.1,
											 * "minorGridEnabled" : true }
											 */
											});

							var now = new Date().toISOString();
							chart.amExport.imageFileName = "wykres_" + now;

						},
						controller : function($scope) {
							$scope.chartType = $scope.chartType || 'serial';

						}

					}
				});
