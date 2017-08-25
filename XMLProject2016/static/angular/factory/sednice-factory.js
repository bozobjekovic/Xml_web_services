(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('SedniceFactory', SedniceFactory);
	
	SedniceFactory.$inject = ['Restangular'];

	function SedniceFactory(Restangular) {
		
		var retVal = {};
		
		retVal.zakazi = function(sednica) {
			return Restangular.one('sednica/zakaziSednicu').customPOST(sednica).then(
				function(data) {
					return data;
				}
			);
		}
		
		retVal.dobaviSednicu = function() {
			return Restangular.one('sednica/zakazanaSednica').get().then(
				function(data) {
					return data;
				}
			);
		}
		
		retVal.prekiniSednicu = function(id) {
			return Restangular.one('sednica/prekiniSednicu/', id).get().then(
				function(data) {
					return data;
				}
			);
		}
		
		return retVal;
	}
})(angular);