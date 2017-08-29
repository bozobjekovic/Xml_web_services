(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('AmandmaniFactory', AmandmaniFactory);
	
	AmandmaniFactory.$inject = ['Restangular'];

	function AmandmaniFactory(Restangular) {
		
		var retVal = {};
		
		retVal.getAll = function() {
			return Restangular.all('amandman').getList().then(
				function (data) {
					return data;	
				}
			);
		}
		
		retVal.getHTML = function(id) {
			return Restangular.one('amandman/html/', id).get().then(
				function(data) {
					return data;
				},
				function(err) {
					return null;
				}
			);
		}
		
		retVal.amandmaniAkta = function() {
			return Restangular.all('amandman/uProceduri').getList().then(
				function(data) {
					return data;
				},
				function(err) {
					return null;
				}
			);
		}
		
		retVal.downloadJSON = function(id) {
			return Restangular.one('amandman/json/', id).withHttpConfig({ responseType: 'arraybuffer' }).get().then(
				function(data) {
					return data;
				},
				function(err) {
					return null;
				}
			);
		}
		
		retVal.downloadRDF = function(id) {
			return Restangular.one('amandman/rdf/', id).withHttpConfig({ responseType: 'arraybuffer' }).get().then(
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