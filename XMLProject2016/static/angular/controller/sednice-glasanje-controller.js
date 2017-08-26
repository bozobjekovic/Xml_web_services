(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('GlasanjeController', GlasanjeController);
	
	GlasanjeController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'SedniceFactory'];

	function GlasanjeController($scope, $location, $sce, $routeParams, SedniceFactory) {
		
		var vm = this;
		vm.akt = {};
		vm.amandman = {};
		vm.brojGlasovaZa = '';
		vm.brojGlasovaProtiv = '';
		vm.brojSuzdrzanih = '';

		vm.glasanje = function() {
			
			/*SedniceFactory.glasanje(vm.akt).then(function(response){
				$location.path('/rezultati');
			});*/
		}
	}

})(angular);