/**
 * 
 */

angular
		.module('myApp.directives', [])
		.directive(
				'lukeAmChart',
				function($q) {
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

						dataForChart = adaptMyData(scope.chartData);

						var initChart = function() {
							myChart = new AmCharts.AmSerialChart();
							myChart.langulage = "pl";
							myChart.marginLeft = 20;
							myChart.pathToImages = "http://www.amcharts.com/lib/3/images/";
							myChart.decimalSeparator = ",";
							myChart.thousandsSeparator = ".";
							myChart.startIndex = 1;

							// myChart.
							// VALUE AXES
							var valueAxis = new AmCharts.ValueAxis();
							valueAxis.axisAlpha = "0";
							valueAxis.inside = true;
							valueAxis.position = "left";
							valueAxis.ignoreAxisWidth = true;
							valueAxis.id = "ValueAxis-";
							valueAxis.title = scope.valueAxisTitle || "";
							valueAxis.titleFontSize = 15;

							myChart.addValueAxis(valueAxis);

							// var graph = new AmCharts.AmGraph();
							// myChart.addGraph(graph);

							var chartScrollbar = new AmCharts.ChartScrollbar();
							myChart.addChartScrollbar(chartScrollbar);

							// CURSOR
							chartCursor = new AmCharts.ChartCursor();
							chartCursor.cursorPosition = "mouse";
							chartCursor.cursorAlpha = 0;
							chartCursor.categoryBalloonDateFormat = "JJ:NN:SS"
							myChart.addChartCursor(chartCursor);

							// CATEGORY FIELD
							myChart.categoryField = "date";
							myChart.dataDateFormat = "YYYY-MM-DD HH:NN:SS";

							// CATEGORY AXES
							var categoryAxis = myChart.categoryAxis;

							categoryAxis.parseDates = true;
							categoryAxis.minPeriod = "ss";
							categoryAxis.minorGridAlpha = 0.1;
							categoryAxis.minorGridEnabled = true;
							// categoryAxis.dashLenght=3;
							categoryAxis.dateFormats = [ {
								period : 'fff',
								format : 'JJ:NN:SS'
							}, {
								period : 'ss',
								format : 'JJ:NN:SS'
							}, {
								period : 'mm',
								format : 'JJ:NN'
							}, {
								period : 'hh',
								format : 'DD-MMM JJ:NN'
							}, {
								period : 'DD',
								format : 'DD-MMM'
							}, {
								period : 'WW',
								format : 'DD-MMM'
							}, {
								period : 'MM',
								format : 'MMM YYYY'
							}, {
								period : 'YYYY',
								format : 'MMM YYYY'
							} ];

							// LEGEND
							var chartLegend = new AmCharts.AmLegend();
							// chartLegend.useGraphSettings = true;
							chartLegend.align = "left";
							chartLegend.showEntries = true;
							chartLegend.spacing = 32;
							chartLegend.position = "bottom";
							chartLegend.valueAlign = "left";
							myChart.addLegend(chartLegend);

							// TITLES
							myChart.addTitle(scope.chartTitle || "", 20);

							// AMEXPORT
							myChart.amExport = {
								top : 10,
								right : 30,
								exportJPG : true,
								exportPNG : true,
								exportSVG : true,
								buttonTitle : 'Zapisz obraz jako: '

							};
							myChart.amExport.imageFileName = "Wykres "
									+ new Date().toDateString();

							// -------------------------grpahs--------------------------------------------
							var graphNumber = scope.seriesNumber || 0;
							if (scope.chartData.length > 0)
								graphNumber = scope.seriesNumber
										|| scope.chartData[0].values.length;

							for (var g = 0; g < graphNumber; g++) {
								myChart.graphs
										.push({
											"balloonText" : "[[category]]<br><b><span style='font-size:15px;'>[[value]]</span></b>",
											"id" : "AmGraph-" + (g + 1),
											"title" : "Czujnik " + (g + 1),
											"valueField" : "column-" + (g + 1),
											"lineThickness" : scope.lineThickness || 1
										// "type":"column"
										// "connect":false
										});
							}
							myChart.addListener("dataUpdated", function() {
								console.log("data Updated");
							});

							// WRITE
							myChart.write("chartdiv");

						}
						initChart();
					
					var myAsync=	function asyncGreet(data) {
							  // perform some asynchronous operation, resolve or reject the promise when appropriate.
							  return $q(function(resolve, reject) {
								  myChart.dataProvider = adaptMyData(data);
								  myChart.validateData();
								  
							  });
							}


						//WATCHER
						scope.$watchCollection('chartData', function(newChartData,
								oldChartData) {
							
							myAsync(newChartData);
						/*	myChart.dataProvider = adaptMyData(newChartData);
							console.log(myChart.dataProvider);
							
							if (myChart.dataProvider.length > 0) {
								console.log("before validateData()");
								//FUNCTION WHICH REFRESH DATA ON CHART
								myChart.validateData();
								console.log("after validateData()");
							}*/
							

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

				})
				.directive('graph', function() {
				    return {
				        restrict: 'E', // Use as element
				        scope: { // Isolate scope
				            data: '=', // Two-way bind data to local scope
				            opts: '=?' // '?' means optional
				        },
				        template: "<div></div>", // We need a div to attach graph to
				        link: function(scope, elem, attrs) {

				            var graph = new Dygraph(elem.children()[0], scope.data, scope.opts );
				        }
				    };
				})
				
				 .directive('ngDygraphs', ['$window', '$sce', function ($window, $sce) {
        return {
            restrict: 'E',
            scope: { // Isolate scope
                data: '=',
                options: '=',
                legend: '=?'
            },
            template: '<div class="ng-dygraphs">' +                     // Outer div to hold the whole directive
                '<div class="graph"></div>' +                           // Div for graph
                '<div class="legend" ng-if="LegendEnabled">' +          // Div for legend
                '<div class="series-container">' +                      // Div for series
                '<div ng-repeat="series in legendSeries" class="series">' +
                '<a ng-click="selectSeries(series)">' +
                '<span ng-bind-html="seriesLine(series)"></span>' +
                '<span ng-style="seriesStyle(series)">{{series.label}}</span>' +
                '</a>' +
                '</div>' +                                              // Repeat
                '</div>' +                                              // Series Div
                '</div>' +                                              // Legend Div
                '<div class="dypopover"></div>' +
                '</div>',                                               // Outer div
            link: function (scope, element, attrs) {
                scope.LegendEnabled = true;

                var parent = element.parent();
                var mainDiv = element.children()[0];
                var chartDiv = $(mainDiv).children()[0];
                var legendDiv = $(mainDiv).children()[1];
                var popover = element.find('.dypopover');

                var popoverWidth = 0;
                var popoverHeight = 0;
                var chartArea;
                var popoverPos = false;

                var graph = new Dygraph(chartDiv, scope.data, scope.options);
                scope.$watch("data", function () {
                    var options = scope.options;
                    if (options === undefined) {
                        options = {};
                    }
                    options.file = scope.data;
                    options.highlightCallback = scope.highlightCallback;
                    options.unhighlightCallback = scope.unhighlightCallback;
                    if(options.showPopover === undefined)
                        options.showPopover = true;

                    if (scope.legend !== undefined) {
                        options.labelsDivWidth = 0;
                    }
                    graph.updateOptions(options);
                    graph.resetZoom();

                    resize();
                }, true);

                scope.$watch("legend", function () {
                    // Clear the legend
                    var colors = graph.getColors();
                    var labels = graph.getLabels();

                    scope.legendSeries = {};

                    if (scope.legend !== undefined && scope.legend.dateFormat === undefined) {
                        scope.legend.dateFormat = 'MMMM Do YYYY, h:mm:ss a';
                    }

                    // If we want our own legend, then create it
                    if (scope.legend !== undefined && scope.legend.series !== undefined) {
                        var cnt = 0;
                        for (var key in scope.legend.series) {
                            scope.legendSeries[key] = {};
                            scope.legendSeries[key].color = colors[cnt];
                            scope.legendSeries[key].label = scope.legend.series[key].label;
                            scope.legendSeries[key].format = scope.legend.series[key].format;
                            scope.legendSeries[key].visible = true;
                            scope.legendSeries[key].column = cnt;

                            cnt++;
                        }
                    }

                    resize();
                });
                
                scope.$watch("options", function(newOptions){
                    graph.updateOptions(newOptions);
                    resize();
                }, true);

                scope.highlightCallback = function (event, x, points, row) {
                    if(!scope.options.showPopover)
                        return;
                  //  console.log(event, x, points, row);
                    var html = "<table><tr><th colspan='2'>";
                    if (typeof moment === "function" && scope.legend !== undefined) {
                        html += moment(x).format(scope.legend.dateFormat);
                    }
                    else {
                        html += x;
                    }
                    html += "</th></tr>";

                    angular.forEach(points, function (point) {
                        var color;
                        var label;
                        var value;
                        if (scope.legendSeries[point.name] !== undefined) {
                            label = scope.legendSeries[point.name].label;
                            color = "style='color:" + scope.legendSeries[point.name].color + ";'";
                            if(scope.legendSeries[point.name].format) {
                                value = point.yval.toFixed(scope.legendSeries[point.name].format);
                            }
                            else {
                                value = point.yval;
                            }
                        }
                        else {
                            label = point.name;
                            color = "";
                        }
                        html += "<tr " + color + "><td>" + label + "</td>" + "<td>" + value + "</td></tr>";
                    });
                    html += "</table>";
                    popover.html(html);
                    popover.show();
                    var table = popover.find('table');
                    popoverWidth = table.outerWidth(true);
                    popoverHeight = table.outerHeight(true);

                    // Provide some hysterises to the popup position to stop it flicking back and forward
                    if (points[0].x < 0.4) {
                        popoverPos = false;
                    }
                    else if (points[0].x > 0.6) {
                        popoverPos = true;
                    }
                    var x;
                    if (popoverPos == true) {
                        x = event.pageX - popoverWidth - 20;
                    }
                    else {
                        x = event.pageX + 20;
                    }
                    popover.width(popoverWidth);
                    popover.height(popoverHeight);
                    popover.animate({left: x + 'px', top: (event.pageY - (popoverHeight / 2)) + 'px'}, 20);

                  //  console.log("Moving", {left: x + 'px', top: (event.pageY - (popoverHeight / 2)) + 'px'})
                };

                scope.unhighlightCallback = function (event, a, b) {
                    if(!scope.options.showPopover){
                        popover.hide();
                        return;
                    }
                    // Check if the cursor is still within the chart area
                    // If so, ignore this event.
                    // This stops flickering if we get an even when the mouse covers the popover
                    if(event.pageX > chartArea.left && event.pageX < chartArea.right && event.pageY > chartArea.top && event.pageY < chartArea.bottom) {
                        var x;
                        if (popoverPos == true) {
                            x = event.pageX - popoverWidth - 20;
                        }
                        else {
                            x = event.pageX + 20;
                        }
                        popover.animate({left: x + 'px'}, 10);
                        return;
                    }
                 //   console.log(event, a, b);
                    popover.hide();
                };

                scope.seriesLine = function (series) {
                    return $sce.trustAsHtml('<svg height="14" width="20"><line x1="0" x2="16" y1="8" y2="8" stroke="' +
                        series.color + '" stroke-width="3" /></svg>');
                };

                scope.seriesStyle = function (series) {
                    if (series.visible) {
                        return {color: series.color};
                    }
                    return {};
                };

                scope.selectSeries = function (series) {
                   // console.log("Change series", series);
                    series.visible = !series.visible;
                    graph.setVisibility(series.column, series.visible);
                };

                resize();

                var w = angular.element($window);
                w.bind('resize', function () {
                    resize();
                });

                function resize() {
                    var maxWidth = 0;
                    element.find('div.series').each(function () {
                        var itemWidth = $(this).width();
                        maxWidth = Math.max(maxWidth, itemWidth)
                    });
                    element.find('div.series').each(function () {
                        $(this).width(maxWidth);
                    });

                    var legendHeight = element.find('div.legend').outerHeight(true);
                  /*  console.log("Heights", legendHeight, parent.height(), parent.outerHeight(true),
                        $(mainDiv).outerHeight(), element.height(), $(legendDiv).height(),
                        $(legendDiv).outerHeight(true));*/
                    graph.resize(parent.width(), parent.height() - legendHeight);
                    chartArea = $(chartDiv).offset();
                    chartArea.bottom = chartArea.top + parent.height() - legendHeight;
                    chartArea.right = chartArea.left + parent.width();
                   // console.log("Position",chartArea);
                }
            }
        };
    }]);
    
    
