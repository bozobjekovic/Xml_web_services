(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('RegistrationFactory', RegistrationFactory);
	
	RegistrationFactory.$inject = ['Restangular'];

	function RegistrationFactory(Restangular) {
		
		var retVal = {};
		
		
		return retVal;

	}

})(angular);