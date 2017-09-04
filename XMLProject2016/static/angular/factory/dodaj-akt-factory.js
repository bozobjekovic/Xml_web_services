(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('DodajAktFactory', DodajAktFactory);
	
	DodajAktFactory.$inject = ['Restangular'];

	function DodajAktFactory(Restangular) {
		
		var retVal = {};
		
		retVal.addAkt = function(xmlObject) {
			return Restangular.one('akt').customPOST(xmlObject).then(
				function(data) {
					return data;
				}, function(err) {
					return null;
				}
			);
		}
		
		return retVal;

	}

})(angular);