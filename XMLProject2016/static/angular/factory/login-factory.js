(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('LoginFactory', LoginFactory);
	
	LoginFactory.$inject = ['Restangular'];

	function LoginFactory(Restangular) {
		
		var retVal = {};
		
		
		return retVal;

	}

})(angular);