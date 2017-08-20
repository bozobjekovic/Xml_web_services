(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('AmandmanPreviewController', AmandmanPreviewController);
	
	AmandmanPreviewController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'AmandmaniFactory'];

	function AmandmanPreviewController($scope, $location, $sce, $routeParams, AmandmaniFactory) {
		
		var vm = this;
		vm.id = $routeParams.id;

		vm.previewHTML = function() {
			AmandmaniFactory.getHTML(vm.id).then(
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