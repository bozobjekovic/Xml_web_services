(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('UNaceluController', UNaceluController);
	
	UNaceluController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'SedniceFactory'];

	function UNaceluController($scope, $location, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.dokumenti = [];
		
		vm.prikaziDokumente = function() {
			SedniceFactory.amandmaniAkta("U proceduri").then(
				function(data) {
					vm.dokumenti = data;
				});
			}
		vm.prikaziDokumente();
		
		vm.prihvatiUNacelu = function(id) {
			SedniceFactory.prihvatiUNacelu(id).then(function(data) {
				document.getElementById(data.id + 'prihvati').disabled = true;
				document.getElementById(data.id + 'odbij').disabled = true;
				document.getElementById(data.id + 'status').innerHTML = data.preambula.status.value;
			});
		}

		vm.odbijAkt = function(id) {
			SedniceFactory.povuciAkt(id).then(function(data) {
				document.getElementById(id + 'prihvati').disabled = true;
				document.getElementById(id + 'odbij').disabled = true;
				document.getElementById(id + 'status').innerHTML = "Odbijen";
				vm.povuciAmandmaneAkta(id);
				for (var i = 0; i < vm.dokumenti.length; i++) {
					var obj = vm.dokumenti[i];
					if (obj.id === id) 
						vm.dokumenti.splice(i, 1);
				}
			});
		}
		
		vm.povuciAmandmaneAkta = function(id) {
			SedniceFactory.povuciAmandmaneAkta(id).then(function(data) {
			});
		}
		
		vm.predjiNaGlasanje = function() {
			$location.path('/glasanje');
		}
	}

})(angular);