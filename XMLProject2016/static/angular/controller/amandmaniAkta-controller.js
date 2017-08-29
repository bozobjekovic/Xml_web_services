(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('AmandmaniAktaController', AmandmaniAktaController);
	
	AmandmaniAktaController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'AmandmaniFactory'];

	function AmandmaniAktaController($scope, $location, $sce, $routeParams, AmandmaniFactory) {
		
		var vm = this;
		vm.dokumenti = [];
		
		vm.prikaziDokumente = function() {
			AmandmaniFactory.amandmaniAkta().then(
				function(data) {
					vm.dokumenti = data;
				});
			}
		vm.prikaziDokumente();
	}

})(angular);