(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('OdrzavanjeController', OdrzavanjeController);
	
	OdrzavanjeController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'SedniceFactory'];

	function OdrzavanjeController($scope, $location, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.sednica = {};
		vm.sednicaUToku = false;
		
		vm.dobaviSednicu = function(){
			SedniceFactory.dobaviSednicu().then(function(data){
				vm.sednica = data;
				if (new Date() > vm.sednica.datum)
					vm.sednicaUToku = true;
			});
		}
		vm.dobaviSednicu();
		
		vm.prekiniSednicu = function(){
			$location.path('/uNacelu');
			/*SedniceFactory.prekiniSednicu(vm.sednica.id).then(function(data){
				$location.path('/uNacelu');				
			});*/
		}
	}

})(angular);