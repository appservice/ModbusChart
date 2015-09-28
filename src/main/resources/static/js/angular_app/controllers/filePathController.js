/**
 * ============================================================================================================
 * File Path CONTROLLER
 * ============================================================================================================
 */
angular.module('myApp.controllers').controller('FilePathController', FilePathController);

function FilePathController($scope, Restangular) {
	$scope.filepathes = [];

	$scope.pageNumbers = [];
	$scope.pageSize = 31;

	var allData = Restangular.oneUrl('rest/filepathes').get({
		page : '0',
		size : $scope.pageSize

	}).then(function(pathes) {
		// console.log(pathes);
		$scope.filepathes = pathes;
		// console.log(pathes.totalPages);
		for (nn = 0; nn < pathes.totalPages; nn++) {
			$scope.pageNumbers.push(nn);

		}

	});
	// console.log(allData);

	$scope.getPagedData = function(pageParam, sizeParam) {
		// console.log("test");
		Restangular.oneUrl('rest/filepathes').get({
			page : pageParam,
			size : sizeParam
		}).then(function(pathes) {
			$scope.filepathes = pathes;

		});
	}

	$scope.deleteFilePath = function(file) {
		var doDelete = confirm("Czy na pewno usunąć plik z serwera?");

		console.log($scope.filepathes);
		console.log(file);
		if (doDelete) {
			$scope.filepathes.customDELETE(file.id).then(function() {

				var index = $scope.filepathes.content.indexOf(file);
				if (index > -1)
					$scope.filepathes.content.splice(index, 1);
			}, function() {
				alert("Brak uprawnień do usuwania. \nZaloguj jako administrator");

			});
		}

	}

};