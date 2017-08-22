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
		
		retVal.getAmandmane = function(user) {
			return Restangular.one('amandman/korisnikovi').customPOST(user).then(
				function(data) {
					return data;
				}, 
				function() {
					return null;
				}
			);
		}
		
		retVal.povuciAkt = function(id) {
			return Restangular.one('akt/povuci', id).get().then(
					function(data) {
						return data;
					}, 
					function() {
						return null;
					}
				);
		}
		
		retVal.povuciAmandmaneAkta = function(id) {
			return Restangular.one('amandman/povuciAmandmaneAkta', id).get().then(
					function(data) {
						return data;
					}
				);
		}
		
		retVal.povuciAmandman = function(id) {
			return Restangular.one('amandman/povuci', id).get().then(
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