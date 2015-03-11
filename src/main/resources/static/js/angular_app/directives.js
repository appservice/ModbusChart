/**
 * 
 */

angular
		.module('myApp.directives', [])
		.directive(
				'lukeAmChart',
				function() {
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
					directive.template = '<div id="chartdiv" style="min-width: 310px; height:600px; margin: 0 auto"></div>';

					// link
					directive.link = function(scope, element, attrs) {

						// var chart = false;
						var dataForChart = [];

					/*	scope
								.$watchCollection(
										'chartData',
										function(newChartData, oldChartData) {
											console.log(newChartData);*/

											dataForChart = adaptMyData(scope.chartData);

											var initChart = function() {
												myChart = new AmCharts.AmSerialChart();
												myChart.langulage = "pl";
												myChart.marginLeft = 20;
												myChart.pathToImages = "http://www.amcharts.com/lib/3/images/";
												myChart.decimalSeparator = ",";
												myChart.thousandsSeparator = ".";

												// myChart.
												// VALUE AXES
												var valueAxis = new AmCharts.ValueAxis();
												valueAxis.axisAlpha = "0";
												valueAxis.inside = true;
												valueAxis.position = "left";
												valueAxis.ignoreAxisWidth = true;
												valueAxis.id = "ValueAxis-1";
												valueAxis.title = scope.valueAxisTitle
														|| "";
												valueAxis.titleFontSize=15;

												myChart.addValueAxis(valueAxis);

												var graph = new AmCharts.AmGraph();
												myChart.addGraph(graph);

												var chartScrollbar = new AmCharts.ChartScrollbar();
												myChart.addChartScrollbar(chartScrollbar);

												// CURSOR
												chartCursor = new AmCharts.ChartCursor();
												chartCursor.cursorPosition = "mouse";
												chartCursor.cursorAlpha = 0;
												chartCursor.categoryBalloonDateFormat = "JJ:NN:SS"
												myChart
														.addChartCursor(chartCursor);

												// CATEGORY FIELD
												myChart.categoryField = "date";
												myChart.dataDateFormat = "YYYY-MM-DD HH:NN:SS";

												// CATEGORY AXES
												var categoryAxis = myChart.categoryAxis;

												categoryAxis.parseDates = true;
												categoryAxis.minPeriod = "ss";
												categoryAxis.minorGridAlpha = 0.1;
												categoryAxis.minorGridEnabled = true;
												categoryAxis.dashLenght=3;
												categoryAxis.dateFormats= [{
												    period: 'fff',
												    format: 'JJ:NN:SS'
												}, {
												    period: 'ss',
												    format: 'JJ:NN:SS'
												}, {
												    period: 'mm',
												    format: 'JJ:NN'
												}, {
												    period: 'hh',
												    format: 'DD-MMM JJ:NN'
												}, {
												    period: 'DD',
												    format: 'DD-MMM'
												}, {
												    period: 'WW',
												    format: 'DD-MMM'
												}, {
												    period: 'MM',
												    format: 'MMM YYYY'
												}, {
												    period: 'YYYY',
												    format: 'MMM YYYY'
												}];

												// LEGEND
												var chartLegend = new AmCharts.AmLegend();
												//chartLegend.useGraphSettings = true;
												chartLegend.align="left";
												chartLegend.showEntries=true;
												chartLegend.spacing = 32;
												chartLegend.width = 100;
												chartLegend.position="right";
												chartLegend.valueAlign = "left";
												myChart.addLegend(chartLegend);

												// TITLES		
												myChart.addTitle(scope.chartTitle||"",20);

												// AMEXPORT 											
												myChart.amExport={
														top		: 10,
														right		: 30,
														exportJPG	: true,
														exportPNG	: true,
														exportSVG	: true,
														buttonTitle:'Zapisz obraz jako: '
														
														
												};
												myChart.amExport.imageFileName="Wykres "+new Date().toDateString();

												// -------------------------grpahs--------------------------------------------
												var graphNumber = scope.seriesNumber || 0;
												if (scope.chartData.length > 0)
													graphNumber = scope.seriesNumber
															|| scope.chartData[0].values.length;

												for (var g = 0; g < graphNumber; g++) { //
													myChart.graphs
															.push({
																"balloonText" : "[[category]]<br><b><span style='font-size:15px;'>[[value]]</span></b>",
																"id" : "AmGraph-"
																		+ (g + 1),
																"title" : "Czujnik "
																		+ (g + 1),
																"valueField" : "column-"
																		+ (g + 1),
																"lineThickness" : scope.lineThickness || 1,
															/* "connect":false */
															});
												}

	
												// WRITE
												myChart.write("chartdiv");

											}
											initChart();

											myChart.dataProvider = adaptMyData(scope.chartData);
											myChart.validateData();
											
											

											scope
											.$watchCollection(
													'chartData',
													function(newChartData, oldChartData) {
														//console.log(newChartData);	
														myChart.dataProvider = adaptMyData(scope.chartData);
														myChart.validateData();
											
									});
					};

					/**
					 * function for adapting my data json object to amChart
					 * object
					 */
					var adaptMyData = function(myDataTable) {
						var dataForChart = [];
						// console.log(myDataTable);
						for (var i = 0; i < myDataTable.length; i++) {

							var myDate = new Date(myDataTable[i].date);
						
							var dataToDisplay = {};


							for (var j = 0; j < myDataTable[i].values.length; j++) {

								dataToDisplay["column-" + (j + 1)] = myDataTable[i].values[j];
							}

							dataToDisplay["date"] = myDate;
							dataForChart.push(dataToDisplay);

						}
						return dataForChart;

					};

					return directive;

				});
