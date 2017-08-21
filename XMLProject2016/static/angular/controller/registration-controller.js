(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('RegistrationController', RegistrationController);
	
	RegistrationController.$inject = ['$scope', '$location', '$timeout', 'RegistrationFactory'];

	function RegistrationController($scope, $location, $timeout, RegistrationFactory) {

		var vm = this;
		
		vm.user = {};
		vm.showError = false;
		vm.showSuccess = false;
		
		
		vm.registration = function() {
			return RegistrationFactory.registrate(vm.user).then(
				function(data) {
					if (data == null) {
						vm.showError = true;
					} else {
						vm.showError = false;
						vm.showSuccess = true;
						$timeout(function(){$location.path('/');}, 3000);
					}
				}
			);
		}
		
	}

})(angular);