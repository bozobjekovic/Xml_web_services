(function(angular) {
	'use strict';
	
	angular.module(
			'xmlWebServices.routes',
			[ 'ngRoute', 'restangular', 'lodash']).config(configure).run(runBlock);
	
	configure.$inject = [ '$routeProvider', '$locationProvider' ];
	runBlock.$inject = [ 'Restangular' ];
	
	function configure($routeProvider, $locationProvider) {
		$locationProvider.hashPrefix('');
		$routeProvider
		.when('/', {
			templateUrl : 'views/main.html',
			controller  : 'IndexController',
			controllerAs: 'indexCtrl'
		}).when('/login', {
			templateUrl : 'views/login.html',
			controller  : 'LoginController',
			controllerAs: 'loginCtrl'
		}).when('/registrate', {
			templateUrl : 'views/registration.html',
			controller  : 'RegistrationController',
			controllerAs: 'registrationCtrl'
		}).when('/akti', {
			templateUrl : 'views/akti.html',
			controller  : 'AktiController',
			controllerAs: 'aktiCtrl'
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