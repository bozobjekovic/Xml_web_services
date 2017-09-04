(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('OdrzavanjeController', OdrzavanjeController);
	
	OdrzavanjeController.$inject = ['$scope', '$location', '$localStorage', '$sce', '$routeParams', 'SedniceFactory'];

	function OdrzavanjeController($scope, $location, $localStorage, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.sednica = {};
		vm.sednicaUToku = false;
		vm.sednicaNema = false;
		
		vm.prikaz = function() {
			if($localStorage.user == null)
				return false;
			
			if($localStorage.user.uloga == 'odbornik')
				false;
			else
				true;
		}
		
		vm.dobaviSednicu = function(){
			SedniceFactory.dobaviSednicu().then(function(data){
				vm.sednica = data;
				if(data != null) {
					if (new Date() > vm.sednica.datum)
						vm.sednicaUToku = true;
				}
				else
					vm.sednicaNema = true;
			});
		}
		vm.dobaviSednicu();
		
		vm.prekiniSednicu = function(){
			SedniceFactory.prekiniSednicu(vm.sednica.id).then(function(data){
				$location.path('/uNacelu');				
			});
		}
	}

})(angular);