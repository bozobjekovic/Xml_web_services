(function(angular) {
	'use strict';

	angular.module('xmlWebServices.controllers').controller(
			'PredloziController', PredloziController);

	PredloziController.$inject = [ '$scope', '$location', '$localStorage',
			'PredloziFactory' ];

	function PredloziController($scope, $location, $localStorage,
			PredloziFactory) {

		var vm = this;

		if ($localStorage.user == null) {
			$location.path('/login');
		} else {
			vm.getAktsForUser = function() {
				PredloziFactory
						.getAkts($localStorage.user)
						.then(
								function(data) {
									if (data == null) {
										alert("Error loading akts and amands from server!");
									} else {
										vm.predlozi = data;
									}
								});
			}
			vm.getAktsForUser();

			vm.getAmandmaneForUser = function() {
				PredloziFactory
						.getAmandmane($localStorage.user)
						.then(
								function(data) {
									if (data == null) {
										alert("Error loading akts and amands from server!");
									} else {
										vm.predloziAmd = data;
									}
								});
			}
			vm.getAmandmaneForUser();
		}

		vm.povuciAkt = function(id) {
			PredloziFactory.povuciAkt(id).then(function(data) {
				vm.povuciAmandmaneAkta(id);
				for (var i = 0; i < vm.predlozi.length; i++) {
					var obj = vm.predlozi[i];
					if (obj.id === id) {
						vm.predlozi.splice(i, 1);
					}
				}
			});
		}
		
		vm.povuciAmandmaneAkta = function(id) {
			PredloziFactory.povuciAmandmaneAkta(id).then(function(data) {
				for (var i = 0; i < vm.predloziAmd.length; i++) {
					var obj = vm.predloziAmd[i];
					if (obj.id === id) {
						console.log("OVDE");
						vm.predloziAmd.splice(i, 1);
					}
				}
			});
		}
		
		vm.povuciAmandman = function(id) {
			PredloziFactory.povuciAmandman(id).then(function(data) {
				//ISPRAVITI ! 
				for (var i = 0; i < vm.predloziAmd.length; i++) {
					var obj = vm.predloziAmd[i];
					if (obj.id === id) {
						vm.predloziAmd.splice(i, 1);
					}
				}
			});
		}

	}

})(angular);