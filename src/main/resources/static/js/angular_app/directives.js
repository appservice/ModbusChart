/**
 * 
 */

angular
		.module('myApp.directives', [])

		.directive(
				'ngDygraphs',
				[
						'$window',
						'$sce',
						function($window, $sce) {
							return {
								restrict : 'E',
								scope : { // Isolate scope
									data : '=',
									options : '=',
									legend : '=?'
								},
								template : '<div class="ng-dygraphs">'
										+ // Outer div to hold the whole
										// directive
										'<div class="graph"></div>'
										+ // Div for graph
										'<div class="legend" ng-if="LegendEnabled">'
										+ // Div for legend
										'<div class="series-container">'
										+ // Div for series
										'<div ng-repeat="series in legendSeries" class="series">'
										+ '<a ng-click="selectSeries(series)">'
										+ '<span ng-bind-html="seriesLine(series)"></span>'
										+ '<span ng-style="seriesStyle(series)">{{series.label}}</span>'
										+ '</a>' + '</div>' + // Repeat
										'</div>' + // Series Div
										'</div>' + // Legend Div
										'<div class="dypopover"></div>'
										+ '</div>', // Outer div
								link : function(scope, element, attrs) {
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

									var graph = new Dygraph(chartDiv,
											scope.data, scope.options);
									scope
											.$watch(
													"data",
													function() {
														var options = scope.options;
														if (options === undefined) {
															options = {};
														}
														options.file = scope.data;
														options.highlightCallback = scope.highlightCallback;
														options.unhighlightCallback = scope.unhighlightCallback;
														if (options.showPopover === undefined)
															options.showPopover = true;

														if (scope.legend !== undefined) {
															options.labelsDivWidth = 0;
														}
														graph
																.updateOptions(options);
														graph.resetZoom();

														resize();
													}, true);

									scope
											.$watch(
													"legend",
													function() {
														// Clear the legend
														var colors = graph
																.getColors();
														var labels = graph
																.getLabels();

														scope.legendSeries = {};

														if (scope.legend !== undefined
																&& scope.legend.dateFormat === undefined) {
															scope.legend.dateFormat = 'MMMM Do YYYY, h:mm:ss a';
														}

														// If we want our own
														// legend, then create
														// it
														if (scope.legend !== undefined
																&& scope.legend.series !== undefined) {
															var cnt = 0;
															for ( var key in scope.legend.series) {
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

									scope
											.$watch(
													"options",
													function(newOptions) {
														graph
																.updateOptions(newOptions);
														resize();
													}, true);

									scope.highlightCallback = function(event,
											x, points, row) {
										if (!scope.options.showPopover)
											return;
										// console.log(event, x, points, row);
										var html = "<table><tr><th colspan='2'>";
										if (typeof moment === "function"
												&& scope.legend !== undefined) {
											html += moment(x).format(
													scope.legend.dateFormat);
										} else {
											html += x;
										}
										html += "</th></tr>";

										angular
												.forEach(
														points,
														function(point) {
															var color;
															var label;
															var value;
															if (scope.legendSeries[point.name] !== undefined) {
																label = scope.legendSeries[point.name].label;
																color = "style='color:"
																		+ scope.legendSeries[point.name].color
																		+ ";'";
																if (scope.legendSeries[point.name].format) {
																	value = point.yval
																			.toFixed(scope.legendSeries[point.name].format);
																} else {
																	value = point.yval;
																}
															} else {
																label = point.name;
																color = "";
															}
															html += "<tr "
																	+ color
																	+ "><td>"
																	+ label
																	+ "</td>"
																	+ "<td>"
																	+ value
																	+ "</td></tr>";
														});
										html += "</table>";
										popover.html(html);
										popover.show();
										var table = popover.find('table');
										popoverWidth = table.outerWidth(true);
										popoverHeight = table.outerHeight(true);

										// Provide some hysterises to the popup
										// position to stop it flicking back and
										// forward
										if (points[0].x < 0.4) {
											popoverPos = false;
										} else if (points[0].x > 0.6) {
											popoverPos = true;
										}
										var x;
										if (popoverPos == true) {
											x = event.pageX - popoverWidth - 20;
										} else {
											x = event.pageX + 20;
										}
										popover.width(popoverWidth);
										popover.height(popoverHeight);
										popover
												.animate(
														{
															left : x + 'px',
															top : (event.pageY - (popoverHeight / 2))
																	+ 'px'
														}, 20);

										// console.log("Moving", {left: x +
										// 'px', top: (event.pageY -
										// (popoverHeight / 2)) + 'px'})
									};

									scope.unhighlightCallback = function(event,
											a, b) {
										if (!scope.options.showPopover) {
											popover.hide();
											return;
										}
										// Check if the cursor is still within
										// the chart area
										// If so, ignore this event.
										// This stops flickering if we get an
										// even when the mouse covers the
										// popover
										if (event.pageX > chartArea.left
												&& event.pageX < chartArea.right
												&& event.pageY > chartArea.top
												&& event.pageY < chartArea.bottom) {
											var x;
											if (popoverPos == true) {
												x = event.pageX - popoverWidth
														- 20;
											} else {
												x = event.pageX + 20;
											}
											popover.animate({
												left : x + 'px'
											}, 10);
											return;
										}
										// console.log(event, a, b);
										popover.hide();
									};

									scope.seriesLine = function(series) {
										return $sce
												.trustAsHtml('<svg height="14" width="20"><line x1="0" x2="16" y1="8" y2="8" stroke="'
														+ series.color
														+ '" stroke-width="3" /></svg>');
									};

									scope.seriesStyle = function(series) {
										if (series.visible) {
											return {
												color : series.color
											};
										}
										return {};
									};

									scope.selectSeries = function(series) {
										// console.log("Change series", series);
										series.visible = !series.visible;
										graph.setVisibility(series.column,
												series.visible);
									};

									resize();

									var w = angular.element($window);
									w.bind('resize', function() {
										resize();
									});

									function resize() {
										var maxWidth = 0;
										element.find('div.series').each(
												function() {
													var itemWidth = $(this)
															.width();
													maxWidth = Math
															.max(maxWidth,
																	itemWidth)
												});
										element.find('div.series').each(
												function() {
													$(this).width(maxWidth);
												});

										var legendHeight = element.find(
												'div.legend').outerHeight(true);
										/*
										 * console.log("Heights", legendHeight,
										 * parent.height(),
										 * parent.outerHeight(true),
										 * $(mainDiv).outerHeight(),
										 * element.height(),
										 * $(legendDiv).height(),
										 * $(legendDiv).outerHeight(true));
										 */
										graph.resize(parent.width(), parent
												.height()
												- legendHeight);
										chartArea = $(chartDiv).offset();
										chartArea.bottom = chartArea.top
												+ parent.height()
												- legendHeight;
										chartArea.right = chartArea.left
												+ parent.width();
										// console.log("Position",chartArea);
									}
								}
							};
						} ])
		.directive(
				'chartDygraph',
				function() {
					return {
						restrict : 'E',
						scope : { // Isolate scope
							data : '=',
							title : '=?',
							maxSeriesNumber:'=',
							yAxisTitle:'@?'||''
						// options : '=',
						// legend : '=?'
						},
						template : '<div id="div_g" style="width:auto; height:500px;" ></div>'
									+'<div> </div>'
							
							, // Outer
					

						link : function(scope, element, attrs) {

							var g = {};

						//create labels	
						//	console.log(scope.maxSeriesNumber);
						var labelsTable = [ "Czas" ];
						for (var ii = 1; ii < scope.maxSeriesNumber+1; ii++) {
								labelsTable.push("Czujnik-" + ii);
							}

							var pushData = adaptedData(scope.data);

							g = new Dygraph(document.getElementById("div_g"),
									pushData, {
										// drawPoints: true,
										// showRoller: true,
										strokeWidth : 1.5,
										colors : [ "#FF0000", "#00AA00",
												"#0000FF", "#FF00FF",
												"#9900FF", "#FFCC00",
												"#808080", "#000080" ],
										"labelsSeparateLines" : true,
										// valueRange: [0.0, 1.2],
										labels : labelsTable,
										//rightGap : 100,
										title : scope.title,
										ylabel :scope.yAxisTitle,// "Przepływ [m3/s]",
										// xlabel:"Czas",
										drawGapEdgePoints : true,
									// showRangeSelector: true,
									// drawPoints: true,

									});
							
							//console.log(g.numColumns());

							// $scope.isChecked1 = true;
							// $scope.setVisibility1 = function() {

							// console.log($scope.isChecked1);
							// g.setVisibility(0, $scope.isChecked1);

							// }

							scope.$watchCollection("data", function(newData,
									oldData) {
								if (newData.length > 0) {
								
									//add value to number of columns in first row equil number of labels
									while(newData[0].values.length<scope.maxSeriesNumber){
										newData[0].values.push("NaN");
									//	console.log(newData[0].values);
									}
									
									g.updateOptions({
										'file' : adaptedData(newData)
									});
								//	console.log(g.numColumns());
									console.log("ilość danych: "
											+ newData.length);
													// console.log(g.getLabels());
													// console.log('data have
													// changed

								}
							});

						}

					}

					// ------function which adapted my data to Dygraph
					function adaptedData(data) {
						var pushData = [];

						for (var k = 0; k < data.length; k++) {
							var myData = []
							myData.push(new Date(data[k].date));
							for (var ll = 0; ll < data[k].values.length; ll++) {
								myData.push(data[k].values[ll]);
							}
							pushData.push(myData);
						}
						return pushData;

					}
				});
