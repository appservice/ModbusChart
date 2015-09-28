/**
 * ============================================================================================================
 * Custom period chart controller
 * ============================================================================================================
 */
angular.module('myApp.controllers')
.controller('CustomPeriodChartController', CustomPeriodChartController);
function CustomPeriodChartController($scope, Restangular) {
	$scope.chartTitle = 'Wykres z n dni';
	$scope.myData = [];
	// $scope.maxSeriesNumber = 8;
	$scope.showChart = false;

	var today = new Date();
	var tomorrow = new Date();
	tomorrow.setDate(today.getDate() + 1);
	$scope.untilMaxDate = tomorrow;

	//

	$scope.createChart = function() {
		// console.log($scope.fromDate);
		var startDate = new Date($scope.fromDate);
		var endDate = new Date($scope.untilDate);

		$scope.chartTitle = 'Wykres od: ' + startDate.toLocaleString() + ' do ' + endDate.toLocaleString();

		$scope.isLoading = true;

		Restangular.one('rest/servers/1/').customGET('measurements', {
			"startDate" : startDate.getTime(),
			"endDate" : endDate.getTime()
		})

		.then(function(data) {
			$scope.myData = data.plain();

			$scope.isLoading = false;

			if (data.length > 0) {
				$scope.showChart = true;
			} else {
				$scope.showChart = false;
				alert("Brak danych do wyświetlenia");

			}

		}, function(error) {
			$scope.isLoading = false;
			$scope.errorView(error);
			console.log(error);
		});

	}

};