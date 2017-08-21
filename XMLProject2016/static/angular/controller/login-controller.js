(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('LoginController', LoginController);
	
	LoginController.$inject = ['$scope', '$location', '$localStorage', 'LoginFactory'];

	function LoginController($scope, $location, $localStorage, LoginFactory) {
		
		var vm = this;
		
		vm.user = {};
		vm.showError = false;

		
		vm.login = function() {
			LoginFactory.logn(vm.user).then(
				function(data) {
					if (data == null) {
						vm.showError = true;
					} else {
						$localStorage.user = data;
						$location.path('/');
					}
				}
			);
		}

	}

})(angular);