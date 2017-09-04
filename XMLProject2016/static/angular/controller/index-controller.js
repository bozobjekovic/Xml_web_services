(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('IndexController', IndexController);
	
	IndexController.$inject = ['$scope', '$localStorage', '$location'];

	function IndexController($scope, $localStorage, $location) {
		
		var vm = this;
		
		vm.prikaz = function() {
			var korisnik = $localStorage.user.email;
			if(korisnik == null)
				return true;
			else
				return false;
		} 
		
		vm.logout = function() {
			 $localStorage.user = [];
			 $location.path("/");
		}
	}

})(angular);