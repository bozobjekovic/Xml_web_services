(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('ZakazivanjeController', ZakazivanjeController);
	
	ZakazivanjeController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'SedniceFactory'];

	function ZakazivanjeController($scope, $location, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.dokumenti = [];
		vm.sednica = {};
		vm.datum = '';
		vm.vreme = '';
		
		vm.dobaviSednicu = function(){
			$location.path('/uNacelu');
			/*SedniceFactory.dobaviSednicu().then(function(data){
				if (data != null)
					$location.path('/odrzavanje');
			});*/
		}
		vm.dobaviSednicu();

		vm.prikaziDokumente = function() {
			SedniceFactory.amandmaniAkta("U proceduri").then(
				function(data) {
					vm.dokumenti = data;
				});
			}
		vm.prikaziDokumente();

		vm.zakazivanje = function() {
			var datumSednice = vm.datum;
			datumSednice.setHours(vm.vreme.getHours());
			datumSednice.setMinutes(vm.vreme.getMinutes());
			vm.sednica.datum = datumSednice;
			
			vm.sednica.brojPrisutnih = 0;
			vm.sednica.status = 'Zakazana';
			
			SedniceFactory.zakazi(vm.sednica).then(function(data){
				$location.path('/odrzavanje');
			});
		}
	}

})(angular);