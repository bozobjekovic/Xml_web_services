(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('AktiController', AktiController);
	
	AktiController.$inject = ['$scope', 'AktiFactory'];

	function AktiController($scope, AktiFactory) {
		
		var vm = this;
		
		vm.getAll = function() {
			AktiFactory.getAll().then(
				function(data) {
					vm.akti = data;
				}
			);
		}
		vm.getAll();
		
		vm.downloadPDF = function(id) {
			AktiFactory.getAktPDF(id).then(
				function(data) {
					if (data == null) {
						alert("Greska prilikom preuzimanja PDF-a!");
					} else {
						var fileName = "akt_" + id + ".pdf";
						
						var a = document.createElement("a");
				        document.body.appendChild(a);
				        var file = new Blob([data], {type: 'application/pdf'});
				        var fileURL = window.URL.createObjectURL(file);
				        a.href = fileURL;
				        a.download = fileName;
				        a.click();
					}
				}
			);
		}

	}

})(angular);