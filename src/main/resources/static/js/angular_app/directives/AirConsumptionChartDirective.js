/**
 * 
 */
angular.module('myApp.directives')
.directive('airConsumptionChart', function($window, Restangular) {
	return {
		restrict : 'E',
		scope : { // Isolate scope
			data : '=',
			chartTitle : '=?',
			//maxSeriesNumber : '=',
			seriesName:'=',
			yAxisTitle : '@?' || '',

		},
		templateUrl : 'js/angular_app/templates/chart-dygraph-template.html',

		/**
		 * 
		 *========= LINK====================================================================================================
		 * 
		 */
		link : function(scope, element, attrs) {

			var divGraph = element.find(".div_graph")[0];

			// create labels
			var labelsTable = [ "Czas" ];
			for (var ii = 0; ii < scope.seriesName.length ; ii++) {
				labelsTable.push(scope.seriesName[ii]);
			}
			labelsTable.push("Zużycie energii");

			// create table of data
			var pushData = [];

			// create array of series colors
			var colorsArray = [ "#FF0000", "#00AA00", "#0000FF", "#FF00FF", "#800080", "#FFCC00", "#808080", "#000080", "#FFA500", "#0AE200" ];

			var firstData;

			// ---create dygraph----------
			var g = new Dygraph(divGraph, pushData, {
				// drawPoints: true,
				//showRoller : true,
				strokeWidth : 1.5,
				// colorValue:0.8,
				// colorSaturaion:0.5,
				colors : colorsArray,
				"labelsSeparateLines" : true,
				// valueRange: [0.0, 0.5],
				labels : labelsTable,
				animatedZooms : true,
				//labelsKMG2 : true,
				labelsKMB:true,
				// legend:"follow",
				labelsDiv : div_l,
				legend : 'auto',// 'always',//
				title : scope.chartTitle,
				ylabel : scope.yAxisTitle,
				y2label: 'Zużycie energii [kWh]',
				yLabelWidth : 18,
				digitsAfterDecimal : 2,
				drawGapEdgePoints : true,
				 series: {
		              'Zużycie energii': {
		                axis: 'y2'}},
		         axes: {
		                    y2: {
		                      // set axis-related properties here
		                    //  labelsKMG2: true,
		                      independentTicks: true,
		                      strokeWidth : 2,
		                      strokePattern:Dygraph.DASHED_LINE
		                    }
		                  }
			//	zoomCallback : rangeSelectingCallback,
			 //   showRangeSelector: true,


			});

			
			
			
			/**=============================================================== 
			 * resetZoom function 
			 * ===============================================================
			 */
			scope.resetZoom = function() {
				scope.data = firstData;
				g.updateOptions({
					dateWindow : null,
					valueRange : null
				});
			}

			/**
			 * =============================================================== 
			 * grab screen function
			 * ===============================================================
			 */
			var imgExport = element.append('<img style="visibility:hidden"></img>');
			scope.downloadGraph = function() {
				//console.log("download as file");
				var myNav = navigator.userAgent.toLowerCase();
				var startDate = new Date(g.xAxisRange()[0]);

				var endDate = new Date(g.xAxisRange()[1]);

				var myTitle = 'Wykres od: ' + startDate.toLocaleString() + ' do: ' + endDate.toLocaleString();
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

			/**
			 * ========================================================== 
			 * grab excel function
			 * ==========================================================
			 */
			scope.downloadExcel = function() {

				var startDate = parseInt(g.xAxisRange()[0]);
				var endDate = parseInt(g.xAxisRange()[1]);

				var link = "/ModbusChart/rest/servers/1/measurements/download/excel?startDate=" + startDate + "&endDate=" + endDate;
				$window.location.href = link;
			}

			// -------generete series--------
			scope.series = [];
			for (var j = 0; j < scope.seriesName.length+1; j++) {
				var legendSeries = {};
				legendSeries.id = j;
				legendSeries.isVisible = true;
				legendSeries.label = labelsTable[j + 1];
				legendSeries.color = colorsArray[j];
				scope.series.push(legendSeries);
			}

			/**
			 * =============================================================== 
			 *function for showing series
			 * ================================================================
			 */
			scope.showVisible = function(serie) {
				serie.isVisible = !serie.isVisible;
				g.setVisibility(serie.id, serie.isVisible);
			};

			/**
			 * ===================================================================================
			 * watchCollection event -is executing when data collection is changed
			 * ===================================================================================
			 */
			
			var dataCollection=[];
			scope.$watchCollection("data", function(newData, oldData) {
				
				if (newData != oldData) {

					if (firstData == null) {
						firstData = newData;
						console.log(firstData.length);
						dataCollection=adaptedData(newData,scope.seriesName.length);


					}else{

						var signelData=adaptedSingleData(newData[newData.length-2],scope.seriesName.length);
						dataCollection.splice(newData.length-2,0,signelData);
						
					}

					g.updateOptions({
					//	'labels' : labelsTable,
						'file' :dataCollection// adaptedData(newData,scope.seriesName.length),
					//	title : scope.chartTitle
					});

				}

			});

			/**
			 * ===================================================================================
			 * function for callback of range selecting
			 * ===================================================================================
			 */
			function rangeSelectingCallback(minX, maxX, yRanges) {

				function myRangeSelection(range) {

					var oldXRange = scope.data[scope.data.length - 1].date - scope.data[0].date;
					if ((oldXRange > range) && (maxX - minX < range)) { // ||oldXRange<range
						Restangular.one('rest/servers/1/').customGET('measurements', {
							"startDate" : parseInt(minX),
							"endDate" : parseInt(maxX)
						}).then(function(data) {
							scope.data = data.plain();
						});
						console.log("range changed");

					}

				}
				myRangeSelection(24 * 3600 * 1000);
				myRangeSelection(7 * 24 * 3600 * 1000);

			}

		}

	}

	/**
	 * ====================================================================
	 * function which adapted my data to Dygraph
	 * ====================================================================
	 */
	
	function adaptedData(data,seriesCount) {

		// var sTime=new Date().getTime();
		//console.log(seriesCount);
	//console.log(data);

		var pushData = [];

		for (var k = 0; k < data.length; k++) {
			var myData = [];
			myData.push(new Date(data[k].date));
			for (var ll = 0; ll < seriesCount; ll++) {
				myData.push(data[k].values[ll]);
			//	console.log('g');
				//console.log(myData);
			}
			myData.push(data[k].energyConsumption);
			pushData.push(myData);
		}
	//	pushData.push(33);
//console.log(pushData);
		return pushData;

	}
	
	function adaptedSingleData(data,seriesCount){
		//var pushData=[];
		var myData = [];
		myData.push(new Date(data.date));
		for (var ll = 0; ll < seriesCount; ll++) {
			myData.push(data.values[ll]);
		//	console.log('g');
			//console.log(myData);
		}
		myData.push(data.energyConsumption);
		return myData;
	}

});