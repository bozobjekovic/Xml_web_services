(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('AktiController', AktiController);
	
	AktiController.$inject = ['$scope', '$location', 'AktiFactory'];

	function AktiController($scope, $location, AktiFactory) {
		
		var vm = this;
		
		vm.html = "";
		vm.filter = {
			status : "Svi",
			oblast : "Sve",
			minDatumPredaje : "",
			maxDatumPredaje : "",
			minDatumObjave : "",
			maxDatumObjave : "",
		}
		
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
		
		vm.downloadJSON = function (id) {
			AktiFactory.downloadJSON(id)
				.then(function(data) {
					
					if (data == null) {
						alert("Greska prilikom preuzimanja JSON-a!");
					}
					
					var fileName = "akt_" + id + ".json";
						
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
			AktiFactory.downloadRDF(id)
				.then(function(data) {
					
					if (data == null) {
						alert("Greska prilikom preuzimanja RDF-a!");
					}
					
					var fileName = "akt_" + id + ".rdf";
						
			        var a = document.createElement("a");
			        document.body.appendChild(a);
			        var file = new Blob([data], {type: 'application/rdf+xml'});
			        var fileURL = window.URL.createObjectURL(file);
			        a.href = fileURL;
			        a.download = fileName;
			        a.click();
		            
				});
		}
		
		vm.previewHTML = function(id) {
			$location.path('/preview/' + id);
		}
		
		vm.search = function() {
			AktiFactory.search(vm.filter).then(
					function(data) {
						if(data != null){
							vm.akti = data;
						}
					}
				);
		}
		
		vm.searchByText = function() {
			AktiFactory.searchByText(vm.searchText).then(
				function(data) {
					if (data != null) {
						vm.akti = data;
					}
				}
			);
		}

	}

})(angular);