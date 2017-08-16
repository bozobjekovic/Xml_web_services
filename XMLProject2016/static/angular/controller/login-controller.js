(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('LoginController', LoginController);
	
	LoginController.$inject = ['$scope', 'LoginFactory'];

	function LoginController($scope, LoginFactory) {

	}

})(angular);