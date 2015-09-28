/**
 * 
 */
angular
		.module('myApp.controllers', []);

		/**
		 * ================================================================================================
		 * 2 hours chart controller
		 * ================================================================================================
		 */
		/*
		.controller('TwoHoursChartController',
				[ '$scope', 'DataDownloader', 'ServerDataDownloader', function($scope, DataDownloader, ServerDataDownloader) {

					$scope.myData = [];
					// $scope.maxSeriesNumber = 7;
					$scope.isLoading = true;
					$scope.isInitializedGraph = false;
					$scope.chartTitle = 'Wykres z ostatnich 2 godzin';
					$scope.seriesName = [ "raz", "dwa", "trzy" ];

					ServerDataDownloader.getData(1).then(function(server) {
						$scope.seriesName = server.sensorsName;
						$scope.isInitializedGraph = true;
						DataDownloader.getData(2 * 60 * 60 * 1000).then(function(data) {
							// $scope.seriesName=["trzy"];
							console.log(data);
							// $scope.isLoading = false;
							$scope.myData = data;
							$scope.isLoading = false;
							// $scope.$apply('myData');

						});
					});

				} ])

		/**
		 * ================================================================================================
		 * 8 hours chart controller
		 * ================================================================================================
		 *//*
		.controller('EightHoursChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {

			$scope.myData = [];
			// $scope.maxSeriesNumber = 7;
			$scope.isLoading = true;
			$scope.chartTitle = 'Wykres z ostatnich 8 godzin';

			DataDownloader.getData(8 * 60 * 60 * 1000).then(function(data) {
				$scope.isLoading = false;
				$scope.myData = data;

			});

		} ])

		/**
		 * ================================================================================================
		 * Day chart controller
		 * ================================================================================================
		 *//*
		.controller('DayChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {

			$scope.myData = [];
			// $scope.maxSeriesNumber = 7;
			$scope.chartTitle = 'Wykres z ostatnich 24 godzin';
			$scope.isLoading = true;

			DataDownloader.getData(24 * 60 * 60 * 1000).then(function(data) {
				$scope.isLoading = false;
				$scope.myData = data;

			});

		} ])

		/**
		 * ================================================================================================
		 * 7 days chart controller
		 * ================================================================================================
		 *//*
		.controller('SevenDaysChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {

			$scope.myData = [];
			// $scope.maxSeriesNumber = 7;
			$scope.chartTitle = 'Wykres z ostatnich 7 dni';
			$scope.isLoading = true;

			DataDownloader.getData(7 * 24 * 60 * 60 * 1000).then(function(data) {
				$scope.isLoading = false;
				$scope.myData = data;

			});

		} ])

		/**
		 * ================================================================================================
		 * 31 days chart controller
		 * ================================================================================================
		 *//*
		.controller('ThirtyOneDaysChartController', [ '$scope', 'DataDownloader', function($scope, DataDownloader) {
			$scope.myData = [];
			// $scope.maxSeriesNumber = 7;
			$scope.isLoading = true;
			$scope.chartTitle = 'Wykres z ostatnich 31 dni';

			DataDownloader.getData(31 * 24 * 60 * 60 * 1000).then(function(data) {
				$scope.isLoading = false;
				$scope.myData = data;

			});

		} ])

		/**
		 * ============================================================================================================
		 * chart online controller
		 * ======================================================================================================
		 *//*
		.controller('ChartOnlineController', [ '$rootScope', '$scope', 'Restangular', 'poller', function($rootScope, $scope, Restangular, poller) {

			$scope.myData = [];
			$scope.maxSeriesNumber = 7;

			// don't show befoure get the data from server
			$scope.is_loaded = false;

			// Restangular.one('rest/servers', 1).get().then(function(myServer)
			// {
			// console.log(myServer);

			$scope.myServer = $rootScope.mainServer;// myServer;//
			// console.log($scope.myServer);

			if ($scope.myServer != null) {
				$scope.is_loaded = true;

				$scope.maxSeriesNumber = $scope.myServer.sensorsName.length; // ||
				// 0;
				$scope.serverTimeIterval = $scope.myServer.timeInterval;

				var dataTableSize = 60;

				var isFirstData = true;

				// ----------------poller----------------

				var myPoller = poller.get($scope.myServer.one("measurement-online"), {

					action : 'get',
					delay : $scope.serverTimeIterval,
				// catchError : true
				});

				myPoller.promise.then(null, null, function(data) {

					// check the empty response
					if (data === undefined) {
						console.log("Brak pomiarów!");
						myPoller.stop();
					} else {

						// fill measurements table by empty measurements
						if (isFirstData) {
							var emptyValues = [];
							for (var emp_i = 0; emp_i < $scope.mySeriesNumber; emp_i++) {
								emptyValues[emp_i] = null;
							}
							for (var j = 0; j < dataTableSize; j++) {
								$scope.myData.push({
									"date" : data.date - (dataTableSize - j) * $scope.serverTimeIterval,
									"values" : emptyValues
								});

							}

							isFirstData = false;
						}

						// if response is not empty and server is run

						$scope.dataToShow = data.plain();
						// console.log($scope.dataToShow);

						if ($scope.myData[$scope.myData.length - 1].date != data.date || $scope.myData[$scope.myData.length - 2].date != data.date) {

							if ($scope.myData.length > dataTableSize - 1)
								$scope.myData.shift();

							$scope.myData.push(data.plain());
							// console.log($scope.myData);

						} else {

							myPoller.stop();

						}
					}
				});
			}

			// });

			// });

		} ])

		/**
		 * ============================================================================================================
		 * download controller
		 *//*
		.controller('DownloadController', [ '$scope', 'Restangular', function($scope, Restangular) {

		} ]);

*/



