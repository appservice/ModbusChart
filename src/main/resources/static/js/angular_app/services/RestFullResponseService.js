/**
 * 
 */
// Restangular service that uses setFullResponse
angular.module('myApp.services')
.factory('RestFulResponse', ['Restangular',function(Restangular) {
  return Restangular.withConfig(function(RestangularConfigurer) {
    RestangularConfigurer.setFullResponse(true);
  });
}]);