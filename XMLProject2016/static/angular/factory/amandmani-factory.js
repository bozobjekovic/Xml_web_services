(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('AmandmaniFactory', AmandmaniFactory);
	
	AmandmaniFactory.$inject = ['Restangular'];

	function AmandmaniFactory(Restangular) {
		
		var retVal = {};
		
		
		return retVal;

	}

})(angular);