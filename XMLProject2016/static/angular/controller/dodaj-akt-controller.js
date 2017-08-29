(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('DodajAktController', DodajAktController);
	
	DodajAktController.$inject = ['$scope', '$location', 'DodajAktFactory'];

	function DodajAktController($scope, $location, DodajAktFactory) {
		
		var vm = this;
		
		function setupXonomy() {
			
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
					"Akt" : {
						menu : [
							{
								caption : "Dodaj <Preambula>",
								action : Xonomy.addPreambulaChildElement,
								actionParameter : "<Preambula/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Preambula");
								}
							}, {
								caption : "Dodaj <Deo>",
								action : Xonomy.addDeoChildElement,
								actionParameter : "<Deo/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Član");
								}
							}, {
								caption : "Dodaj <Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<Član/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Deo");
								}
							}, {
								caption : "Dodaj @naslov",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "naslov",
									value : ""
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("naslov");
								}
							}
						],
						attributes : {
							"naslov" : {
								asker : Xonomy.askString
							}
						}
					},
					
					// ########################################
					// --------- Preambula elements ----------
					"Preambula" : {
						menu : [ {
							caption : "Dodaj novi <PravniOsnov>",
							action : Xonomy.newElementChild,
							actionParameter : "<PravniOsnov/>",
							hideIf : function(jsElement) {
								return jsElement.hasChildElement("PravniOsnov");
							}

						}, {
							caption : "Dodaj novi <NazivOrgana>",
							action : Xonomy.newElementChild,
							actionParameter : "<NazivOrgana/>",
							hideIf : function(jsElement) {
								return jsElement.hasChildElement("NazivOrgana");
							}

						}, {
							caption : "Dodaj novu <Oblast>",
							action : Xonomy.newElementChild,
							actionParameter : "<Oblast/>",
							hideIf : function(jsElement) {
								return jsElement.hasChildElement("Oblast");
							}

						} ]
					},
					
					"PravniOsnov" : {
						hasText : true,
						menu : [ {
							caption : "Obriši",
							action : Xonomy.deleteElement
						} ],
						mustBeBefore : [ "Deo" ]
					},

					"NazivOrgana" : {
						hasText : true,
						asker : Xonomy.askPicklist,
						askerParameter : [ "Skupština Grada Novog Sada" ],
						menu : [ {
							caption : "Obriši",
							action : Xonomy.deleteElement
						} ],
						mustBeAfter : [ "PravniOsnov" ]
					},

					"Oblast" : {
						hasText : true,
						asker : Xonomy.askPicklist,
						askerParameter : [ "Zdravstvo", "Školstvo", "Vojska",
								"Policija", "Pravosuđe" ],
						menu : [ {
							caption : "Obriši",
							action : Xonomy.deleteElement
						} ],
						mustBeAfter : [ "NazivOrgana" ]
					}
				}
			};
			
			var xml = "<Akt></Akt>";
			
			var insEditor = document.getElementById("xmlEditor");
			
			Xonomy.setMode("laic");
			Xonomy.render(xml, insEditor, docSpec);
		}
		setupXonomy();
		
	}

})(angular);