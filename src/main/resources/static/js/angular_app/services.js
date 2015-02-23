/**
 * 
 */
angular.module('myApp.services',[])
.factory('getLastMeasurement',['Restangular', function(Restangular) {
/*	return {
		getLastMeasurement:Restangular.one('servers/2/measurements/last').getList()
	}*/
	return Restangular.one('servers/2/measurements/last').getList();
	
}]);