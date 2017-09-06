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
		
		vm.downloadPDF = function(id) {
			AmandmaniFactory.getAmdPDF(id).then(
				function(data) {
					if (data == null) {
						alert("Greska prilikom preuzimanja PDF-a!");
					} else {
						var fileName = "amandman_" + id + ".pdf";
						
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
		
		vm.downloadJSON = function (id) {
			AmandmaniFactory.downloadJSON(id)
				.then(function(data) {
					
					if (data == null) {
						alert("Greska prilikom preuzimanja JSON-a!");
					}
					
					var fileName = "amandman_" + id + ".json";
						
			        var a = document.createElement("a");
			        document.body.appendChild(a);
			        var file = new Blob([data], {type: 'application/json'});
			        var fileURL = window.URL.createObjectURL(file);
			        a.href = fileURL;
			        a.download = fileName;
			        a.click();
		            
				});
		}
		
		vm.downloadRDF = function (id) {
			AmandmaniFactory.downloadRDF(id)
				.then(function(data) {
					
					if (data == null) {
						alert("Greska prilikom preuzimanja RDF-a!");
					}
					
					var fileName = "amandman_" + id + ".rdf";
						
			        var a = document.createElement("a");
			        document.body.appendChild(a);
			        var file = new Blob([data], {type: 'application/rdf+xml'});
			        var fileURL = window.URL.createObjectURL(file);
			        a.href = fileURL;
			        a.download = fileName;
			        a.click();
		            
				});
		}
	}

})(angular);