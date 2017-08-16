(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('RegistrationController', RegistrationController);
	
	RegistrationController.$inject = ['$scope', 'RegistrationFactory'];

	function RegistrationController($scope, RegistrationFactory) {

	}

})(angular);