(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('DodajAmandmanController', DodajAmandmanController);
	
	DodajAmandmanController.$inject = ['$scope', '$location', '$localStorage', 'DodajAmandmanFactory'];

	function DodajAmandmanController($scope, $location, $localStorage, DodajAmandmanFactory) {
		
		var vm = this;
		
		vm.successful = false;
		vm.error = false;
		vm.xmlObject = {};
		
		function setupXonomy() {
			
			vm.xmlObject.user = $localStorage.user;
			
			var docSpec = {
				onchange : function() {
					//localStorage.setItem("nedovrseniAkt", Xonomy.harvest());
					console.log("I been changed now!")
				},
				validate : function(obj) {
					console.log("I be validatin' now!")
				},
				elements : {
					// ########################################
					// ------------- Akt elements ------------
					"amd:Amandman" : {
						menu : [
							{
								caption : "Dodaj <amd:Naslov>",
								action : Xonomy.newElementChild,
								actionParameter : "<amd:Naslov xmlns:amd=\"http://www.tim9.com/amandman\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("amd:Naslov");
								}
							}, {
								caption : "Dodaj <amd:PravniOsnov>",
								action : Xonomy.newElementChild,
								actionParameter : "<amd:PravniOsnov xmlns:amd=\"http://www.tim9.com/amandman\"/>",
								hideIf : function(jsElement) {
									return jsElement
											.hasChildElement("amd:PravniOsnov");
								}
							}, {
								caption : "Dodaj pojedinačne amandmane <amd:SadržajAmandmana>",
								action : Xonomy.newElementChild,
								actionParameter : "<amd:SadržajAmandmana xmlns:amd=\"http://www.tim9.com/amandman\"/>"
							}
						]
					}
				}
			};

			var xml = "<amd:Amandman xmlns:amd=\"http://www.tim9.com/amandman\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></amd:Amandman>";
			
			var insEditor = document.getElementById("xmlEditor");
			
			Xonomy.setMode("laic");
			Xonomy.render(xml, insEditor, docSpec);
		}
		setupXonomy();
		
		/*vm.addAmandman = function() {
			vm.xmlObject.xml = Xonomy.harvest();
			DodajAmandmanFactory.addAmandman(vm.xmlObject).then(
				function(data) {
					if (data == null) {
						vm.error = true;
						vm.successful = false;
					} else {
						vm.error = false;
						vm.successful = true;
					}
				}
			);
		}*/
		
	}

})(angular);