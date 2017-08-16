(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('AktiFactory', AktiFactory);
	
	AktiFactory.$inject = ['Restangular'];

	function AktiFactory(Restangular) {
		
		var retVal = {};
		
		
		return retVal;

	}

})(angular);