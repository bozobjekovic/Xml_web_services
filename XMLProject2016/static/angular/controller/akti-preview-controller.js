(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('AktPreviewController', AktPreviewController);
	
	AktPreviewController.$inject = ['$scope', '$location', '$sce', '$anchorScroll', '$routeParams', 'AktiFactory'];

	function AktPreviewController($scope, $location, $sce, $anchorScroll, $routeParams, AktiFactory) {
		
		var vm = this;
		
		vm.id = $routeParams.id;
		
		if((vm.id).includes('skrol')){
			var idAkta = (vm.id).split('skrol')[0];
			var refID = (vm.id).split('skrol')[1];
			
			vm.previewHTML = function() {
				AktiFactory.getHTML(idAkta).then(
						function(data) {
							if(data == null){
								alert("Greska prilikom generisanja HTML-a!");
							}
							else{
								vm.html = $sce.trustAsHtml(data);
							}
				});
			}
			vm.previewHTML();
			
			vm.getAktAmandmans = function() {
				AktiFactory.getAktAmandmans(idAkta).then(
					function(data) {
						vm.amandmani = data;
						$anchorScroll(refID);
					}
				);
			}
			vm.getAktAmandmans();
		} else {
			vm.previewHTML = function() {
				AktiFactory.getHTML(vm.id).then(
						function(data) {
							if(data == null){
								alert("Greska prilikom generisanja HTML-a!");
							}
							else{
								vm.html = $sce.trustAsHtml(data);
							}
				});
			}
			vm.previewHTML();
			
			vm.getAktAmandmans = function() {
				AktiFactory.getAktAmandmans(vm.id).then(
					function(data) {
						vm.amandmani = data;
					}
				);
			}
			vm.getAktAmandmans();
		}

	}

})(angular);