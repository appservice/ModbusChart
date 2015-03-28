/**
 * 
 */

angular
		.module('myApp.directives', [])

		.directive(
				'chartDygraph',
				function($window) {
					return {
						restrict : 'E',
						scope : { // Isolate scope
							data : '=',
							chartTitle : '=?',
							maxSeriesNumber : '=',
							yAxisTitle : '@?' || '',

						},
						template : '<table class="digraph-table" style="width:100%;">'
								+ '<tr><td style="width:85%"><div id="div_g" class="div_graph" style="width:auto; height:450px;"></div></td>'
								+ '<td class="dygraph-td-legend">'
								+ '<button id="saveBtn"class="btn btn-default on-top" ng-click="downloadGraph();"><i class="glyphicon glyphicon-download"></i> PNG</button>'
								+ '<button id="saveExcelBtn"class="btn btn-default on-top" data-ng-click="downloadExcel()" ><i class="glyphicon glyphicon-download"></i> EXCEL</button>'
								+ '<div id="div_l" class="dygraph-div-legend"> </div></td></tr>'
								+ '<tr><td colspan="2"><div class="series-container" >'
								+ '<label data-ng-repeat="mySerie in series" ng-style="{color:mySerie.color}">' // ng-style="{color:scope.series[$index-1].color}"
								+ '<input id="chk-{{mySerie.id}}" type="checkbox" checked="checked" data-ng-click="showVisible(mySerie);"><strong>{{mySerie.label}}</strong></input>'
								+ '</label>' + '</div></td></tr></table>'

						,

						link : function(scope, element, attrs) {

							var divGraph = element.find(".div_graph")[0];

							// create labels
							var labelsTable = [ "Czas" ];
							for (var ii = 1; ii < scope.maxSeriesNumber + 1; ii++) {
								labelsTable.push("Czujnik-" + ii);
							}

							// create table of data
							var pushData = [];

							// create array of series colors
							var colorsArray = [ "#FF0000", "#00AA00", "#0000FF", "#FF00FF", "#800080", "#FFCC00",
									"#808080", "#000080", "#FFA500", "#0AE200" ];

							// ---create dygraph----------
							var g = new Dygraph(divGraph, pushData, {
								// drawPoints: true,
								// showRoller: true,
								strokeWidth : 2,
								// colorValue:0.8,
								// colorSaturaion:0.8,
								colors : colorsArray,
								"labelsSeparateLines" : true,
								// valueRange: [0.0, 1.2],
								labels : labelsTable,
								animatedZooms : true,
								// legend:"follow",
								labelsDiv : div_l,
								legend : 'auto',
								title : scope.chartTitle,
								ylabel : scope.yAxisTitle,// "Przepływ
								// [m3/s]",
								// xlabel:"Czas",
								drawGapEdgePoints : true,

							// showRangeSelector: true,
							// drawPoints: true,

							});

							// -----grab screen function-------
							var imgExport = element.append('<img style="visibility:hidden"></img>');
							scope.downloadGraph = function() {
								console.log("download as file");
								 var myNav = navigator.userAgent.toLowerCase();
								// alert(myNav);
								var startDate = new Date(g.xAxisRange()[0]);

								var endDate = new Date(g.xAxisRange()[1]);

								var myTitle = 'Wykres od: ' + startDate.toLocaleString() + ' do: '
										+ endDate.toLocaleString();
								if (g.xAxisRange()[0] > 0)
									g.updateOptions({
										title : myTitle
									});

								Dygraph.Export.asPNG(g, imgExport);
								download(imgExport.src, "wykres_" + new Date().getTime() + ".png", "image/png");

								g.updateOptions({
									title : scope.chartTitle
								});
							}
							
							//----grab excel function----------
							scope.downloadExcel=function(){
							
							
								var startDate = parseInt(g.xAxisRange()[0]);
								var endDate = parseInt(g.xAxisRange()[1]);
						
								
								var link="/ModbusChart/rest/servers/1/measurements/download/excel?startDate="+startDate+"&endDate="+endDate;
								$window.location.href=link;
					
							}
							
					

							// -------generete series--------
							scope.series = [];
							for (var j = 0; j < scope.maxSeriesNumber; j++) {
								var legendSeries = {};
								legendSeries.id = j;
								legendSeries.isVisible = true;
								legendSeries.label = labelsTable[j + 1];
								legendSeries.color = colorsArray[j];
								scope.series.push(legendSeries);
							}
							// console.log(scope.series);

							// ------function for showing series
							scope.showVisible = function(serie) {
								serie.isVisible = !serie.isVisible;
								g.setVisibility(serie.id, serie.isVisible);
							};

							scope.$watchCollection("data", function(newData, oldData) {
								if (newData.length > 0) {

									// add value to number of
									// columns in first row
									// equil number of labels
									while (newData[0].values.length < scope.maxSeriesNumber) {
										newData[0].values.push("NaN");
									}

									g.updateOptions({
										'file' : adaptedData(newData)
									});
	

								}

							});
							
							

						}

					}

					// ------function which adapted my data to Dygraph
					function adaptedData(data) {
						var pushData = [];

						for (var k = 0; k < data.length; k++) {
							var myData = [];
							myData.push(new Date(data[k].date));
							for (var ll = 0; ll < data[k].values.length; ll++) {
								myData.push(data[k].values[ll]);
							}
							pushData.push(myData);
						}
						return pushData;

					}

				})

		// =============================================================================================

				
				//============gauge directive===============================
				.directive('ngGauge', function() {
					return{
						restrict:'E',
						scope:{
							name:'='
							//data:'=?',
							//seriesNumber:'=?'
							
						},
						template:'<canvas class="canv_gauge" id="c1" ></canvas>',//<div ng-repeat="myGauge in list">ziuta +{{$index}}</div>
				link:function(scope, element, attrs) {
					
					var canvGauge = element.find(".canv_gauge");
					console.log(canvGauge);
					
					scope.list = [1,2];
							scope.gaugesTable=[];
						//	scope.servi
							console.log(scope.seriesNumber);
							/*for(var i=0;i<1;i++){*/
								//console.log(i);
							console.log(scope.name);
					
							//console.log(gauge);
						//	scope.gaugesTable.push(gauge);
							
						/*}*/
							console.log(scope.list);
						},
						controller:function($scope){
							var gauge=new Gauge({
								renderTo:$scope.name,
					/*			width:180,
								height:180,
								title:'Czujnik'*/
								
							});
							
						//	gauge.onready=function(){
									//gauge.setValue(Math.random()*100);
							//		};
							
							gauge.draw();
						}
						
					}
					
				});