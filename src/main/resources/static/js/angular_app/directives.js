/**
 * 
 */

angular
		.module('myApp.directives', [])

		
		.directive(
				'chartDygraph',
				function() {
					return {
						restrict : 'E',
						scope : { // Isolate scope
							data : '=',
							chartTitle : '=?',
							maxSeriesNumber:'=',
							yAxisTitle:'@?'||'',
							//downloadGraph:'=?'
						// options : '=',
						// legend : '=?'
						},
						template :'<table class="digraph-table" style="width:100%;">'
							+'<tr><td style="width:85%"><div id="div_g" style="width:auto; height:450px;" ></div></td>'
									+'<td class="dygraph-td-legend">'
									+'<button id="saveBtn"class="btn btn-default on-top" ng-click="downloadGraph();"><i class="glyphicon glyphicon-download"></i></button>'
									+'<div id="div_l" class="dygraph-div-legend"> </div></td></tr>'
									+'<tr><td colspan="2"><div class="series-container" >'
									+'<span data-ng-repeat="series in labelsTable" ng-style="{color:colorArray[$index-1]}" data-ng-if="$index>0">'
									+'<input type="checkbox" checked="checked" ng-click="showVisible($parent);" >{{series}}</input>'
									+'</span>'
									+'</div></td></tr></table>'
							
							, 
					

						link : function(scope, element, attrs) {

						
					
							

						var labelsTable = [ "Czas" ];
						for (var ii = 1; ii < scope.maxSeriesNumber+1; ii++) {
								labelsTable.push("Czujnik-" + ii);
							}
							scope.labelsTable=labelsTable;
							var pushData = adaptedData(scope.data);

							g = new Dygraph(document.getElementById("div_g"),
									pushData, {
										// drawPoints: true,
										// showRoller: true,
										strokeWidth : 1.5,
									//	colorValue:0.8,
									//	colorSaturaion:0.8,
										colors : [ "#FF0000", "#00AA00",
												"#0000FF", "#FF00FF",
												"#9900FF", "#FFCC00",
												"#808080", "#000080" ],
										"labelsSeparateLines" : true,
										// valueRange: [0.0, 1.2],
										labels : labelsTable,
										animatedZooms:true,
									//	legend:"follow",
									    labelsDiv:div_l,
										//rightGap : 100,
									    legend:'auto',
										title : scope.chartTitle,
										ylabel :scope.yAxisTitle,// "Przepływ [m3/s]",
										// xlabel:"Czas",
										drawGapEdgePoints : true,
										
									// showRangeSelector: true,
									// drawPoints: true,

									});
							//console.log(g.getColors());
							//console.log(g.getLabels());
							
							
							//-----grab screan------------
							var imgExport=element.append("<img/>");
							scope.downloadGraph=function (){
								console.log("download as file");
								 Dygraph.Export.asPNG(g,imgExport);					
								 download(imgExport.src,"wykres.png","image/png");
							}
							
							//var isVisible=true;
							scope.showVisible=function(element){
							//	isVisible=!isVisible;
								g.setVisibility(element.$index-1, false);
								console.log(element.$index+": "+isVisible);
							};
							
							
							
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
									//console.log(g.getColors());
								//	console.log(g.numColumns());
									scope.colorArray=g.getColors();
									console.log("ilość danych: "
											+ newData.length);
				
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
				.directive(
						'chartDygraphOnline',
						function() {
							return {
								restrict : 'E',
								scope : { // Isolate scope
									data : '=',
									dataLength:'=',
									chartTitle : '=?',
									maxSeriesNumber:'=',
									yAxisTitle:'@?'||'',
									//downloadGraph:'=?'
								// options : '=',
								// legend : '=?'
								},
								template :'<table class="digraph-table" style="width:100%;">'
									+'<tr><td style="width:85%"><div id="div_g" style="width:auto; height:450px;" ></div></td>'
											+'<td class="dygraph-td-legend">'
											+'<button id="saveBtn"class="btn btn-default on-top" ng-click="downloadGraph();"><i class="glyphicon glyphicon-download"></i></button>'
											+'<div id="div_l" class="dygraph-div-legend"> </div></td></tr>'
											+'<tr><td colspan="2"><div class="series-container" >'
											+'<span data-ng-repeat="series in labelsTable" ng-style="{color:colorArray[$index-1]}" data-ng-if="$index>0">'
											+'<input type="checkbox" checked="checked" ng-click="showVisible($parent);" >{{series}}</input>'
											+'</span>'
											+'</div></td></tr></table>'
									
									, 
							

								link : function(scope, element, attrs) {

								
							
									

								var labelsTable = [ "Czas" ];
								for (var ii = 1; ii < scope.maxSeriesNumber+1; ii++) {
										labelsTable.push("Czujnik-" + ii);
									}
									scope.labelsTable=labelsTable;
								//	var pushData = adaptedData(scope.data);
									var pushData=[];
									for (var iii=0;iii<scope.dataLength;iii++){
										pushData.push({})
									}

									g = new Dygraph(document.getElementById("div_g"),
											pushData, {
												// drawPoints: true,
												// showRoller: true,
												strokeWidth : 1.5,
											//	colorValue:0.8,
											//	colorSaturaion:0.8,
												colors : [ "#FF0000", "#00AA00",
														"#0000FF", "#FF00FF",
														"#9900FF", "#FFCC00",
														"#808080", "#000080" ],
												"labelsSeparateLines" : true,
												// valueRange: [0.0, 1.2],
												labels : labelsTable,
												animatedZooms:true,
											//	legend:"follow",
											    labelsDiv:div_l,
												//rightGap : 100,
											    legend:'auto',
												title : scope.chartTitle,
												ylabel :scope.yAxisTitle,// "Przepływ [m3/s]",
												// xlabel:"Czas",
												drawGapEdgePoints : true,
												
											// showRangeSelector: true,
											// drawPoints: true,

											});
									//console.log(g.getColors());
									//console.log(g.getLabels());
									
									
									//-----grab screan------------
									var imgExport=element.append("<img/>");
									scope.downloadGraph=function (){
										console.log("download as file");
										 Dygraph.Export.asPNG(g,imgExport);					
										 download(imgExport.src,"wykres.png","image/png");
									}
									
									//var isVisible=true;
									scope.showVisible=function(element){
									//	isVisible=!isVisible;
										g.setVisibility(element.$index-1, false);
										console.log(element.$index+": "+isVisible);
									};
									
									
									
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
											//console.log(g.getColors());
										//	console.log(g.numColumns());
											scope.colorArray=g.getColors();
											console.log("ilość danych: "
													+ newData.length);
						
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
							
							
							
						});

