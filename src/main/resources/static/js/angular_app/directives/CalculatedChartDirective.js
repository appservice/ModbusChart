/**
 * 
 */
angular.module('myApp.directives')
.directive('calculatedChart', function($window, Restangular) {
	return {
		restrict : 'E',
		scope : { // Isolate scope
			data : '=',
			chartTitle : '=?',
			//maxSeriesNumber : '=',
			seriesName:'=',
			yAxisTitle : '@?' || '',
			price:'=',
			//retunedData:'='

		},
		template : 
			'<table class="digraph-table" style="width: 100%;">'
			+'	<tr>'
		+'	<td style="width: 85%">'
		+		'<div id="div_g" class="div_graph" style="width: auto; height: 450px;"></div>'
		+'	</td>'
		+'	<td class="dygraph-td-legend">'
		+'		<button id="saveExcelBtn" class="btn btn-default on-top" style="width: 90px" data-ng-click="resetZoom()">'
		+'		<!-- 	<i class="glyphicon glyphicon-zoom-out"></i> zoom -->'
		+'<small>Reset zoom</small>'
		+		'</button>'
		+		'<button id="saveBtn" class="btn btn-default on-top" style="width: 90px;text-align:justify; " data-ng-click="downloadGraph();">'
		+		'	<i class="glyphicon glyphicon-picture"></i> PNG'
		+		'</button>'
		+		'<div id="div_l" class="dygraph-div-legend"></div>'
		+	'</td>'
		+'</tr>'
		+'<tr>'
		+'	<td colspan="2">'
		+'		<div class="series-container">'
		+'			<label data-ng-style="{color:serie.color}">'
		+'				<input id="chk-{{mySerie.id}}" type="checkbox" checked="checked" data-ng-click="showVisible(serie);">'
		+'				<strong>{{serie.label}}</strong>'
		+'			</label>'
		+'		</div>'
		+'	</td>'
		+'</tr>'
	     +'</table>'
,

		/**
		 * 
		 *========= LINK====================================================================================================
		 * 
		 */
		link : function(scope, element, attrs) {

			var divGraph = element.find(".div_graph")[0];

			// create labels
			var labelsTable = [ "Czas" ];
			labelsTable.push("Koszt");

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
				y2label: 'Zużycie energii [kWh]',
				yLabelWidth : 18,
				digitsAfterDecimal : 4,
				drawGapEdgePoints : true,


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


			scope.serie={
					'id':0,
					'isVisible':true,
					'color':'red',
					'label':'Koszt'
						
					
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
	
					if (firstData == null) {
						firstData = newData;
					}

					g.updateOptions({
						'labels' : labelsTable,
						'file' : adaptedData(newData,scope.seriesName.length,scope),
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
	function adaptedData(data,seriesCount,$scope) {

		console.log(data);

		var pushData = [];

		//data without the last which is null
		for (var k = 0; k < data.length; k++) {
			var myData = [];
			myData.push(new Date(data[k].date));
			var valueSum=0;
			for (var ll = 0; ll < seriesCount; ll++) {
				valueSum+=data[k].values[ll];//*$scope.price;

			}

			
		myData.push(data[k].energyConsumption*$scope.price/valueSum);
		//console.log(myData);
			
			pushData.push(myData);
			
		}
//console.log(pushData);
		return pushData;

	}
	;

});
