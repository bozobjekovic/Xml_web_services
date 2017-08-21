(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('RegistrationFactory', RegistrationFactory);
	
	RegistrationFactory.$inject = ['Restangular'];

	function RegistrationFactory(Restangular) {
		
		var retVal = {};
		
		retVal.registrate = function(user) {
			return Restangular.one('user/registration').customPOST(user).then(
				function(data) {
					return data;
				},
				function() {
					return null;
				}
			);
		}
		
		return retVal;

	}

})(angular);