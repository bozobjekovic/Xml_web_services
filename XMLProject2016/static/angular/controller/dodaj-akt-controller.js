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
					},
					
					// ########################################
					// ------------ Deo elements -------------
					"Deo" : {
						menu : [
							{
								caption : "Dodaj novu <Glava>",
								action : Xonomy.addGlavaChildElement,
								actionParameter : "<Glava/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Član");
								}

							}, {
								caption : "Dodaj novi <Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<Član/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Glava");
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
							}, {
								caption : "Dodaj @redniBroj",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "redniBroj",
									value : "DEO "
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("redniBroj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						attributes : {
							"naslov" : {
								asker : Xonomy.askString
							},
							"redniBroj" : {
								asker : Xonomy.askString
							}
						},
						mustBeAfter : [ "Korisnik", "naslov" ]
					},
					
					"redniBroj" : {
						hasText : true,
						menu : [ {
							caption : "Obriši",
							action : Xonomy.deleteElement

						} ]
					},
					
					// ########################################
					// ----------- Glava elements -------------
					"Glava" : {
						menu : [
							{
								caption : "Dodaj novi <Odeljak>",
								action : Xonomy.addOdeljakChildElement,
								actionParameter : "<Odeljak/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Član");
								}

							}, {
								caption : "Dodaj novi <Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<Član/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Odeljak");
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
							}, {
								caption : "Dodaj @redniBroj",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "redniBroj",
									value : "Glava "
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("redniBroj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						attributes : {
							"naslov" : {
								asker : Xonomy.askString
							},
							"redniBroj" : {
								asker : Xonomy.askString
							}
						}
					},
					
					// ########################################
					// --------- Odeljak elements -----------
					"Odeljak" : {
						menu : [
							{
								caption : "Dodaj novi <Pododeljak>",
								action : Xonomy.addPododeljakChildElement,
								actionParameter : "<Pododeljak/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Član");
								}
							}, {
								caption : "Dodaj novi <Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<Član/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Pododeljak");
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
							}, {
								caption : "Dodaj @redniBroj",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "redniBroj",
									value : ""
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("redniBroj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						attributes : {
							"naslov" : {
								asker : Xonomy.askString
							},
							"redniBroj" : {
								asker : Xonomy.askString
							}
						}
					},
					
					// ########################################
					// -------- Pododeljak elements ----------
					"Pododeljak" : {
						menu : [
							{
								caption : "Dodaj novi <Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<Član/>"
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
							}, {
								caption : "Dodaj @redniBroj",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "redniBroj",
									value : ""
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("redniBroj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						attributes : {
							"naslov" : {
								asker : Xonomy.askString
							},
							"redniBroj" : {
								asker : Xonomy.askString
							}
						}
					},
					
					// ########################################
					// ----------- Clan elements ------------
					"Član" : {
						menu : [
							{
								caption : "Dodaj novi <Stav>",
								action : Xonomy.addStavChildElement,
								actionParameter : "<Stav/>",
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
							}, {
								caption : "Dodaj @redniBroj",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "redniBroj",
									value : "Clan "
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("redniBroj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						attributes : {
							"naslov" : {
								asker : Xonomy.askString
							},
							"redniBroj" : {
								asker : Xonomy.askString
							}
						}
					},
					
					// ########################################
					// ----------- Stav elements ------------
					"Stav" : {
						menu : [
							{
								caption : "Dodaj novu <Tačka>",
								action : Xonomy.addTackaChildElement,
								actionParameter : "<Tačka/>"
							}, {
								caption : "Dodaj novi <Sadrzaj>",
								action : Xonomy.newElementChild,
								actionParameter : "<Sadrzaj/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Sadrzaj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						]
					},
					
					// ########################################
					// ----------- Tacka elements ------------
					"Tačka" : {
						menu : [
							{
								caption : "Dodaj novi <Podtačka>",
								action : Xonomy.addPodtackaChildElement,
								actionParameter : "<Podtačka/>",
							}, {
								caption : "Dodaj novi <Sadrzaj>",
								action : Xonomy.newElementChild,
								actionParameter : "<Sadrzaj/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Sadrzaj");
								},
							}, {
								caption : "Dodaj @redniBroj",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "redniBroj",
									value : ""
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("redniBroj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						mustBeAfter : [ "Sadrzaj" ],
						attributes : {
							"redniBroj" : {
								asker : Xonomy.askString
							}
						}
					},
					
					// ########################################
					// --------- Podtacka elements -----------
					"Podtačka" : {
						menu : [
							{
								caption : "Dodaj novu <Alineja>",
								action : Xonomy.addAlinejaChildElement,
								actionParameter : "<Alineja/>"
							}, {
								caption : "Dodaj novi <Sadrzaj>",
								action : Xonomy.newElementChild,
								actionParameter : "<Sadrzaj/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("Sadrzaj");
								}
							}, {
								caption : "Dodaj @redniBroj",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "redniBroj",
									value : ""
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("redniBroj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						attributes : {
							"redniBroj" : {
								asker : Xonomy.askString
							}
						}
					},
					
					// ########################################
					// --------- Alineja elements -----------
					"Alineja" : {
						menu : [
							{
								caption : "Dodaj novi <Sadrzaj>",
								action : Xonomy.newElementChild,
								actionParameter : "<Sadrzaj/>"
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						]
					},
					
					"Sadrzaj" : {
						hasText : true,
						menu : [
							{
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						inlineMenu : [
							{
								caption : "Dodaj <Referenca>",
								action : Xonomy.wrap,
								actionParameter : {
									template : "<Referenca>$</Referenca>",
									placeholder : "$"
								}
							}
						],
						mustBeBefore : [ "Tačka", "Podtačka", "Alineja" ]
					},
					
					"Referenca" : {
						hasText : false,
						menu : [ 
							{
								caption : "Obriši <Referenca>",
								action : Xonomy.unwrap
							}, {
								caption : "Dodaj @URL",
								action : Xonomy.newAttribute,
								actionParameter : {
									name : "URL",
									value : "akt"
								},
								hideIf : function(jsElement) {
									return jsElement.hasAttribute("URL");
								}
							}
						],
						attributes : {
							"URL" : {
								asker : Xonomy.askString
							}
						}
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