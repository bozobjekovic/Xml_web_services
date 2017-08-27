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

		// Kod glasanja u nacelu
		vm.odbijAkt = function(id) {
			AmandmaniFactory.povuciAkt(id).then(function(data) {
				vm.povuciAmandmaneAkta(id);
				for (var i = 0; i < vm.dokumenti.length; i++) {
					var obj = vm.dokumenti[i];
					if (obj.id === id) {
						vm.dokumenti.splice(i, 1);
					}
				}
			});
		}
		
		vm.povuciAmandmaneAkta = function(id) {
			AmandmaniFactory.povuciAmandmaneAkta(id).then(function(data) {
				for (var i = 0; i < data.length; i++) {
					for (var j = 0; j < vm.dokumenti.amandmani.length; j++) {
						var obj = 'amandmani/' + vm.dokumenti.amandmani[j].id;
						if (obj == data[i]) {
							vm.dokumenti.amandmani.splice(j, 1);
							console.log(j)
						}
					}
				}
			});
		}
	}

})(angular);