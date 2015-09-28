/**
 * 
 */

angular.module('myApp.directives', [])

.directive('chartDygraph', function($window, Restangular) {
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
				labelsKMG2 : true,
				// legend:"follow",
				labelsDiv : div_l,
				legend : 'auto',// 'always',//
				title : scope.chartTitle,
				ylabel : scope.yAxisTitle,
				yLabelWidth : 18,
				digitsAfterDecimal : 4,
				drawGapEdgePoints : true,
				zoomCallback : rangeSelectingCallback,
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
			for (var j = 0; j < scope.seriesName.length; j++) {
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
			scope.$watchCollection("data", function(newData, oldData) {

				if (newData != oldData) {
					//console.log(newData);
					// add value to number of
					// columns in first row
					// equil number of labels
					if (firstData == null) {
						firstData = newData;
					}

					// console.log(newData);
				/*	if (newData.length > 0) {
						while (newData[0].values.length >= (labelsTable.length)) {
							labelsTable.push("Czujnik-" + labelsTable.length);

							var legendSeries2 = {};
							legendSeries2.id = labelsTable.length - 2;
							legendSeries2.isVisible = true;
							legendSeries2.label = labelsTable[labelsTable.length - 1];
							legendSeries2.color = colorsArray[labelsTable.length - 2];
							scope.series.push(legendSeries2);
							// console.log(legendSeries2);

						}
						while (newData[0].values.length < scope.seriesName.length) {
							newData[0].values.push("NaN");
						}
					}*/

					g.updateOptions({
						'labels' : labelsTable,
						'file' : adaptedData(newData,scope.seriesName.length),
						title : scope.chartTitle
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

		var pushData = [];

		for (var k = 0; k < data.length; k++) {
			var myData = [];
			myData.push(new Date(data[k].date));
			for (var ll = 0; ll < seriesCount; ll++) {
				myData.push(data[k].values[ll]);
				//console.log(myData);
			}
			pushData.push(myData);
		}

		return pushData;

	}
	;

});

// =============================================================================================
/*.directive('chartDygraph', function($window, Restangular) {
	return {
		restrict : 'E',
		scope : { // Isolate scope
			data : '=',
			chartTitle : '=?',
			maxSeriesNumber : '=',
			seriesName:'=',
			yAxisTitle : '@?' || '',

		},
		templateUrl : 'js/angular_app/templates/chart-dygraph-template.html',

		/**
		 * 
		 *========= LINK====================================================================================================
		 * 
		
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
				labelsKMG2 : true,
				// legend:"follow",
				labelsDiv : div_l,
				legend : 'auto',// 'always',//
				title : scope.chartTitle,
				ylabel : scope.yAxisTitle,
				yLabelWidth : 18,
				digitsAfterDecimal : 4,
				drawGapEdgePoints : true,
				zoomCallback : rangeSelectingCallback,
			 //   showRangeSelector: true,


			});

			
			
			
			/**=============================================================== 
			 * resetZoom function 
			 * ===============================================================
		
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
		
			scope.downloadExcel = function() {

				var startDate = parseInt(g.xAxisRange()[0]);
				var endDate = parseInt(g.xAxisRange()[1]);

				var link = "/ModbusChart/rest/servers/1/measurements/download/excel?startDate=" + startDate + "&endDate=" + endDate;
				$window.location.href = link;
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

			/**
			 * =============================================================== 
			 *function for showing series
			 * ================================================================
		
			scope.showVisible = function(serie) {
				serie.isVisible = !serie.isVisible;
				g.setVisibility(serie.id, serie.isVisible);
			};

			/**
			 * ===================================================================================
			 * watchCollection event -is executing when data collection is changed
			 * ===================================================================================
		
			scope.$watchCollection("data", function(newData, oldData) {

				if (newData != oldData) {

					// add value to number of
					// columns in first row
					// equil number of labels
					if (firstData == null) {
						firstData = newData;
					}

					// console.log(newData);
					if (newData.length > 0) {
						while (newData[0].values.length >= (labelsTable.length)) {
							labelsTable.push("Czujnik-" + labelsTable.length);

							var legendSeries2 = {};
							legendSeries2.id = labelsTable.length - 2;
							legendSeries2.isVisible = true;
							legendSeries2.label = labelsTable[labelsTable.length - 1];
							legendSeries2.color = colorsArray[labelsTable.length - 2];
							scope.series.push(legendSeries2);
							// console.log(legendSeries2);

						}
						while (newData[0].values.length < scope.maxSeriesNumber) {
							newData[0].values.push("NaN");
						}
					}

					g.updateOptions({
						'labels' : labelsTable,
						'file' : adaptedData(newData),
						title : scope.chartTitle
					});

				}

			});

			/**
			 * ===================================================================================
			 * function for callback of range selecting
			 * ===================================================================================
		
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

	function adaptedData(data) {

		// var sTime=new Date().getTime();

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
	;

});*/
