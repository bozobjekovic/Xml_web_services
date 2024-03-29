(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('ZakazivanjeController', ZakazivanjeController);
	
	ZakazivanjeController.$inject = ['$scope', '$location', '$localStorage', '$sce', '$routeParams', 'SedniceFactory'];

	function ZakazivanjeController($scope, $location, $localStorage, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.dokumenti = [];
		vm.sednica = {};
		vm.datum = '';
		vm.vreme = '';
		
		vm.prikaz = function() {
			if($localStorage.user == null)
				return false;
			
			if($localStorage.user.uloga != 'predsednik') {
				$location.path('/odrzavanje');
				return false;
			}
			else
				return true;
		}
		vm.prikaz();
		
		vm.dobaviSednicu = function(){
			SedniceFactory.dobaviSednicu().then(function(data){
				if (data != null)
					$location.path('/odrzavanje');
			});
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
			
			if(vm.sednica.datum < new Date()){
				vm.invalid = true;
				return;
			}
			
			vm.sednica.brojPrisutnih = 0;
			vm.sednica.status = 'Zakazana';
			
			SedniceFactory.zakazi(vm.sednica).then(function(data){
				$location.path('/odrzavanje');
			});
		}
	}

})(angular);