(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('LoginFactory', LoginFactory);
	
	LoginFactory.$inject = ['Restangular'];

	function LoginFactory(Restangular) {
		
		var retVal = {};
		
		retVal.logn = function(user) {
			return Restangular.one('user/login').customPOST(user).then(
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