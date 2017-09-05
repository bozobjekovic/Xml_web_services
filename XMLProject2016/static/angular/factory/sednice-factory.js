(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.factories')
		.factory('SedniceFactory', SedniceFactory);
	
	SedniceFactory.$inject = ['Restangular'];

	function SedniceFactory(Restangular) {
		
		var retVal = {};
		
		retVal.amandmaniAkta = function(status) {
			return Restangular.one('amandman/status', status).getList().then(
				function(data) {
					return data;
				},
				function(err) {
					return null;
				}
			);
		}
		
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
				},
				function(err) {
					return null;
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
		
		retVal.prihvatiUNacelu = function(id) {
			return Restangular.one('akt/prihvatiUNacelu', id).get().then(
				function(data) {
					return data;
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
		
		retVal.glasajAmandman = function(rezultati) {
			return Restangular.one('sednica/glasajAmandman').customPOST(rezultati).then(
				function(data) {
					return data;
				},
				function(data) {
					return null;
				}
			);
		}
		
		return retVal;
	}
})(angular);