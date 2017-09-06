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
		
		retVal.search = function(filter) {
			return Restangular.one('akt/filter').customPOST(filter).then(
					function (data) {
						return data;	
					},
					function(err) {
						return null;
					}
				);
		}
		
		retVal.searchByText = function(criteria) {
			return Restangular.one('akt/pretraga/', criteria).getList().then(
				function(data) {
					return data;
				},
				function(err) {
					return null;
				}
			);
		}
		
		retVal.downloadJSON = function(id) {
			return Restangular.one('akt/json/', id).withHttpConfig({ responseType: 'arraybuffer' }).get().then(
					function(data) {
						return data;
					},
					function(err) {
						return null;
					}
			);
		}
		
		retVal.downloadRDF = function(id) {
			return Restangular.one('akt/rdf/', id).withHttpConfig({ responseType: 'arraybuffer' }).get().then(
					function(data) {
						return data;
					},
					function(err) {
						return null;
					}
			);
		}
		
		retVal.getAktAmandmans = function(id) {
			return Restangular.one('amandman/amandmaniAkta', id).getList().then(
					function (data) {
						return data;	
					}
				);
		}
		
		retVal.getIDOdredbeAkta = function(id) {
			return Restangular.one('akt/getIDsOdredbeAkta', id).getList().then(
				function (data) {
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