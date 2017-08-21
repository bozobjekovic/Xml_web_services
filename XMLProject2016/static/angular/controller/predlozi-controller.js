(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('PredloziController', PredloziController);
	
	PredloziController.$inject = ['$scope', '$location', '$localStorage', 'PredloziFactory'];

	function PredloziController($scope, $location, $localStorage, PredloziFactory) {
		
		var vm = this;
		
		if ($localStorage.user == null) {
			$location.path('/login');
		} else {
			vm.getAktsForUser = function() {
				PredloziFactory.getAkts($localStorage.user).then(
					function(data) {
						if (data == null) {
							alert("Error loading akts and amands from server!");
						} else {
							vm.predlozi = data;
						}
					}
				);
			}
			vm.getAktsForUser();
		}

	}

})(angular);