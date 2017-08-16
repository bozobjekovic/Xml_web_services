(function(angular) {
	'use strict';
	
	angular.module(
			'webShopApp',
			[ 'ngRoute', 'restangular', 'lodash']).config(configure).run(runBlock);
	
	configure.$inject = [ '$routeProvider', '$locationProvider' ];
	runBlock.$inject = [ 'Restangular' ];
	
	function configure($routeProvider, $locationProvider) {
		$locationProvider.hashPrefix('');
		$routeProvider.when('/', {
			templateUrl : 'views/index.html',
			controller  : 'IndexController',
			controllerAs: 'indexCtrl'
		}).otherwise({
			redirectTo : '/'
		});
	}
	
	function runBlock(Restangular, $log) {
		Restangular.setBaseUrl('xmlWS');
		Restangular.setErrorInterceptor(function(response) {
			if (response.status === 500) {
				//$log.info("internal server error");
				return true;
			}
			return true;
		});
	}
	
})(angular);