(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('AktiFactory', AktiFactory);
	
	AktiFactory.$inject = ['Restangular'];

	function AktiFactory(Restangular) {
		
		var retVal = {};
		
		retVal.getAll = function() {
			return Restangular.all('akt').getList().then(
					function (data) {
						return data;	
					}
				);
		}
		
		retVal.getAktPDF = function(id) {
			return Restangular.one('akt/pdf/', id).withHttpConfig({ responseType: 'arraybuffer' }).get().then(
					function(data) {
						return data;
					},
					function(err) {
						return null;
					}
			);
		}
		
		retVal.getHTML = function(id) {
			return Restangular.one('akt/html/', id).get().then(
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