(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('GlasanjeController', GlasanjeController);
	
	GlasanjeController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'SedniceFactory'];

	function GlasanjeController($scope, $location, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.rezultati = {};
		vm.dokumenti = [];
		
		vm.prikaziDokumente = function() {
			SedniceFactory.amandmaniAkta().then(
				function(data) {
					vm.dokumenti = data;
				});
			}
		vm.prikaziDokumente();
		
		// Na sledecoj stranici mi izlistava onda samo te sa tim statusom (Treba metoda da se odradi - sutra)
		
		// Kod glasanja u nacelu
		vm.prihvatiUNacelu = function(id) {
			SedniceFactory.prihvatiUNacelu(id);
		}

		vm.odbijAkt = function(id) {
			SedniceFactory.povuciAkt(id).then(function(data) {
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
		
		vm.predjiNaGlasanje = function() {
			$location.path('/glasanje');
		}
		
		vm.glasanjeAmd = function(amandman) {
			vm.rezultati.brojGlasovaZa = amandman.brojGlasovaZa;
			vm.rezultati.brojGlasovaProtiv = amandman.brojGlasovaProtiv;
			vm.rezultati.brojSuzdrzanih = amandman.brojSuzdrzanih;
			vm.rezultati.id = amandman.id;
			
			SedniceFactory.glasajAmandman(vm.rezultati).then(function(data){
				document.getElementById(amandman.id + 'za').disabled = true;
				document.getElementById(amandman.id + 'protiv').disabled = true;
				document.getElementById(amandman.id + 'suzdrzano').disabled = true;
				document.getElementById(amandman.id + 'dugme').disabled = true;
				
				if(vm.rezultati.brojGlasovaZa > vm.rezultati.brojGlasovaProtiv)
					document.getElementById(amandman.id + 'statVal').innerHTML = 'Usvojen';
				else	// Da li cemo ga brisati skroz ili samo setujemo stauts?
					document.getElementById(amandman.id + 'statVal').innerHTML = 'Odbijen';
				
				var aktId = amandman.aktURL.split('/')[1];
				var aktDisable = false;

				for (var i = 0; i < vm.dokumenti.length; i++) {
					var obj = vm.dokumenti[i];					
					if (obj.akt.id === aktId) {
						for (var j = 0; j < obj.amandmani.length; j++) {
							if (obj.amandmani[j].id == data.id)
								obj.amandmani[j] = data;
							
							if (obj.amandmani[j].preambula.status.value == 'U proceduri') {
								aktDisable = true;
								j = obj.amandmani.length;
								break;
							}
						}

						if (aktDisable) {
							document.getElementById(aktId + 'za').disabled = true;
							document.getElementById(aktId + 'protiv').disabled = true;
							document.getElementById(aktId + 'suzdrzano').disabled = true;
							document.getElementById(aktId + 'dugme').disabled = true;
						}
						else {
							document.getElementById(aktId + 'za').disabled = false;
							document.getElementById(aktId + 'protiv').disabled = false;
							document.getElementById(aktId + 'suzdrzano').disabled = false;
							document.getElementById(aktId + 'dugme').disabled = false;
						}
					}
				}
			});
		}
		
		vm.predjiNaRezultate = function() {
			$location.path('/rezultati');
		}
	}

})(angular);