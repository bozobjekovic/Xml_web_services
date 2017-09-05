(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('DodajAmandmanFactory', DodajAmandmanFactory);
	
	DodajAmandmanFactory.$inject = ['Restangular'];

	function DodajAmandmanFactory(Restangular) {
		
		var retVal = {};
		
		retVal.addAmandman = function(xmlObject) {
			return Restangular.one('amandman').customPOST(xmlObject).then(
				function(data) {
					return data;
				}, function(err) {
					return null;
				}
			);
		}
		
		retVal.dobaviSednicu = function() {
			return Restangular.one('sednica/zakazanaSednica').get().then(
				function(data) {
					return data;
				},
				function(err) {
					return null;
				}
			);
		}
		
		return retVal;

	}

})(angular);