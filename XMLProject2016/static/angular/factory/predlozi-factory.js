(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('PredloziFactory', PredloziFactory);
	
	PredloziFactory.$inject = ['Restangular'];

	function PredloziFactory(Restangular) {
		
		var retVal = {};
		
		retVal.getAkts = function(user) {
			return Restangular.one('akt/byUser').customPOST(user).then(
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