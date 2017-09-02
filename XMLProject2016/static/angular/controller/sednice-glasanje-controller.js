(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('GlasanjeController', GlasanjeController);
	
	GlasanjeController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'SedniceFactory'];

	function GlasanjeController($scope, $location, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.rezultati = {};
		vm.dokUNacelu = [];
		
		vm.dokumentiUNacelu = function() {
			SedniceFactory.amandmaniAkta("U nacelu").then(
				function(data) {
					vm.dokUNacelu = data;
				});
			}
		vm.dokumentiUNacelu();
		
		vm.glasanjeAmd = function(amandman) {
			vm.rezultati.brojGlasovaZa = amandman.brojGlasovaZa;
			vm.rezultati.brojGlasovaProtiv = amandman.brojGlasovaProtiv;
			vm.rezultati.brojSuzdrzanih = amandman.brojSuzdrzanih;
			vm.rezultati.id = amandman.id;
			
			SedniceFactory.glasajAmandman(vm.rezultati).then(function(data){
				if (data == null) {
					alert("Ponovi glasanje!");
					return;
				}

				document.getElementById(amandman.id + 'za').disabled = true;
				document.getElementById(amandman.id + 'protiv').disabled = true;
				document.getElementById(amandman.id + 'suzdrzano').disabled = true;
				document.getElementById(amandman.id + 'dugme').disabled = true;
				document.getElementById(amandman.id + 'statVal').innerHTML = data.preambula.status.value;
				
				var aktId = amandman.aktURL.split('/')[1];
				var aktDisable = false;

				for (var i = 0; i < vm.dokUNacelu.length; i++) {
					var obj = vm.dokUNacelu[i];					
					if (obj.akt.id === aktId) {
						for (var j = 0; j < obj.amandmani.length; j++) {
							if (obj.amandmani[j].id == data.id)
								obj.amandmani[j] = data;
							
							if (obj.amandmani[j].preambula.status.value == 'U proceduri') {
								aktDisable = true;
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