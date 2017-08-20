(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('AmandmaniController', AmandmaniController);
	
	AmandmaniController.$inject = ['$scope', '$location', 'AmandmaniFactory'];

	function AmandmaniController($scope, $location, AmandmaniFactory) {

		var vm = this;
		vm.html = "";
		
		vm.getAll = function() {
			AmandmaniFactory.getAll().then(
				function(data) {
					vm.amandmani = data;
				}
			);
		}
		vm.getAll();
		
		vm.previewHTML = function(id) {
			$location.path('/previewAmd/' + id);
		}
	}

})(angular);