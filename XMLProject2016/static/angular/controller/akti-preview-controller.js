(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('AktPreviewController', AktPreviewController);
	
	AktPreviewController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'AktiFactory'];

	function AktPreviewController($scope, $location, $sce, $routeParams, AktiFactory) {
		
		var vm = this;
		
		vm.id = $routeParams.id;

		vm.previewHTML = function() {
			AktiFactory.getHTML(vm.id).then(
					function(data) {
						if(data == null){
							alert("Greska prilikom generisanja HTML-a!");
						}
						else{
							vm.html = $sce.trustAsHtml(data);;
						}
			});
		}
		vm.previewHTML();

	}

})(angular);