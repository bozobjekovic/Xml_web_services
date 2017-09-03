(function(angular) {
	'use strict';
	
	angular.module(
			'xmlWebServices.routes',
			[ 'ngRoute', 'restangular', 'ngStorage', 'lodash']).config(configure).run(runBlock);
	
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
		}).when('/amandmani', {
			templateUrl : 'views/amandmani.html',
			controller  : 'AmandmaniController',
			controllerAs: 'amandmaniCtrl'
		}).when('/preview/:id', {
			templateUrl : 'views/pregledAkta.html',
			controller  : 'AktPreviewController',
			controllerAs: 'aktPreviewCtrl'
		}).when('/previewAmd/:id', {
			templateUrl : 'views/pregledAmandmana.html',
			controller  : 'AmandmanPreviewController',
			controllerAs: 'amandmanPreviewCtrl'
		}).when('/mojiPredlozi', {
			templateUrl : 'views/predlozi.html',
			controller  : 'PredloziController',
			controllerAs: 'predloziCtrl'
		}).when('/dodajAkt', {
			templateUrl : 'views/dodajAkt.html',
			controller  : 'DodajAktController',
			controllerAs: 'dodajAktCtrl'
		}).when('/dodajAmandman', {
			templateUrl : 'views/dodajAmandman.html'
		}).when('/zakazivanje', {
			templateUrl : 'views/zakazivanjeSednice.html',
			controller  : 'ZakazivanjeController',
			controllerAs: 'zakazivanjeCtrl'
		}).when('/odrzavanje', {
			templateUrl : 'views/odrzavanjeSednice.html',
			controller  : 'OdrzavanjeController',
			controllerAs: 'odrzavanjeCtrl'
		}).when('/glasanje', {
			templateUrl : 'views/glasanjeSednice.html',
			controller  : 'GlasanjeController',
			controllerAs: 'glasanjeCtrl'
		}).when('/uNacelu', {
			templateUrl : 'views/uNaceluGlasanje.html',
			controller  : 'UNaceluController',
			controllerAs: 'uNaceluCtrl'
		}).when('/rezultati', {
			templateUrl : 'views/rezultatiSednice.html',
			controller  : 'GlasanjeController',
			controllerAs: 'glasanjeCtrl'
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