(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('DodajAktFactory', DodajAktFactory);
	
	DodajAktFactory.$inject = ['Restangular'];

	function DodajAktFactory(Restangular) {
		
		var retVal = {};
		
		return retVal;

	}

})(angular);