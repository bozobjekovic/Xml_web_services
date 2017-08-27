(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('GlasanjeController', GlasanjeController);
	
	GlasanjeController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'SedniceFactory'];

	function GlasanjeController($scope, $location, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.rezultati = {};
		
		vm.uNaceluGlasanje = function() {
			$location.path('/glasanje');
			/*SedniceFactory.glasanje(vm.akt).then(function(response){
				$location.path('/glasanje');
			});*/
		}
		
		vm.glasanjeAmd = function(amandman) {
			vm.rezultati.brojGlasovaZa = amandman.brojGlasovaZa;
			vm.rezultati.brojGlasovaProtiv = amandman.brojGlasovaProtiv;
			vm.rezultati.brojSuzdrzanih = amandman.brojSuzdrzanih;
			
			SedniceFactory.glasajAmandman(vm.rezultati).then(function(data){
				console.log(data);
				vm.rezultati.status = data;
			});
		}
	}

})(angular);