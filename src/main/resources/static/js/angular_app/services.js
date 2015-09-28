/**
 * 
 */
angular.module('myApp.services', []).factory('DataDownloader', function(Restangular, $q) {
	// var maxSeriesNumber = 8;
	
	return {
		getData : function(myDatePeriod) {
			var deferred = $q.defer();
			Restangular.one('rest/servers/1/').customGET('measurements', {
				"timePeriod" : myDatePeriod
			// 2 * 60 * 60 * 1000
			}).then(function(data) {
				deferred.resolve(data.plain());

			}, function(error) {

				switch (error.status) {
				case 0:
					alert("Brak połączenia z serwerem!");
					break;
				case 500:
					alert("Błąd serwera!");
					break;
				}

				console.log(error);
			});

			return deferred.promise;
		}
	}

}).factory('ServerDataDownloader', function(Restangular, $q) {

	return {
		getData : function(serverNumber) {

			var deferred = $q.defer();
			Restangular.one('rest/servers', serverNumber).get().then(function(myServer) {
				deferred.resolve(myServer.plain());
			}, function(error) {

				switch (error.status) {
				case 0:
					alert("Brak połączenia z serwerem!");
					break;
				case 500:
					alert("Błąd serwera!");
					break;
				}
				console.log(error);
			});

			return deferred.promise;
		}
	}

})
