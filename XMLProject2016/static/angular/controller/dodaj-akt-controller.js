(function(angular) {
	'use strict';

	angular
		.module('xmlWebServices.controllers')
		.controller('DodajAktController', DodajAktController);
	
	DodajAktController.$inject = ['$scope', '$location', '$localStorage', 'DodajAktFactory'];

	function DodajAktController($scope, $location, $localStorage, DodajAktFactory) {
		
		var vm = this;
		
		vm.successful = false;
		vm.error = false;
		vm.invalid = false;
		vm.xmlObject = {};
		
		vm.dobaviSednicu = function(){
			DodajAktFactory.dobaviSednicu().then(function(data){
				if (data == null){
					vm.invalid = true;
				}
			});
		}
		vm.dobaviSednicu();
		
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
					"akt:Akt" : {
						menu : [
							{
								caption : "Dodaj <akt:Preambula>",
								action : Xonomy.addPreambulaChildElement,
								actionParameter : "<akt:Preambula xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Preambula");
								}
							}, {
								caption : "Dodaj <akt:Deo>",
								action : Xonomy.addDeoChildElement,
								actionParameter : "<akt:Deo xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Član");
								}
							}, {
								caption : "Dodaj <akt:Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<akt:Član xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Deo");
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
					"akt:Preambula" : {
						menu : [ {
							caption : "Dodaj novi <akt:PravniOsnov>",
							action : Xonomy.newElementChild,
							actionParameter : "<akt:PravniOsnov xmlns:akt=\"http://www.tim9.com/akt\"/>",
							hideIf : function(jsElement) {
								return jsElement.hasChildElement("akt:PravniOsnov");
							}

						}, {
							caption : "Dodaj novi <akt:NazivOrgana>",
							action : Xonomy.newElementChild,
							actionParameter : "<akt:NazivOrgana xmlns:akt=\"http://www.tim9.com/akt\"/>",
							hideIf : function(jsElement) {
								return jsElement.hasChildElement("akt:NazivOrgana");
							}

						}, {
							caption : "Dodaj novu <akt:Oblast>",
							action : Xonomy.newElementChild,
							actionParameter : "<akt:Oblast xmlns:akt=\"http://www.tim9.com/akt\"/>",
							hideIf : function(jsElement) {
								return jsElement.hasChildElement("akt:Oblast");
							}

						} ]
					},
					
					"akt:PravniOsnov" : {
						hasText : true,
						menu : [ {
							caption : "Obriši",
							action : Xonomy.deleteElement
						} ],
						mustBeBefore : [ "Deo" ]
					},

					"akt:NazivOrgana" : {
						hasText : true,
						asker : Xonomy.askPicklist,
						askerParameter : [ "Skupština Grada Novog Sada" ],
						menu : [ {
							caption : "Obriši",
							action : Xonomy.deleteElement
						} ],
						mustBeAfter : [ "PravniOsnov" ]
					},

					"akt:Oblast" : {
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
					"akt:Deo" : {
						menu : [
							{
								caption : "Dodaj novu <akt:Glava>",
								action : Xonomy.addGlavaChildElement,
								actionParameter : "<akt:Glava xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Član");
								}

							}, {
								caption : "Dodaj novi <akt:Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<akt:Član xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Glava");
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
						mustBeAfter : [ "naslov" ]
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
					"akt:Glava" : {
						menu : [
							{
								caption : "Dodaj novi <akt:Odeljak>",
								action : Xonomy.addOdeljakChildElement,
								actionParameter : "<akt:Odeljak xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Član");
								}

							}, {
								caption : "Dodaj novi <akt:Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<akt:Član xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Odeljak");
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
					"akt:Odeljak" : {
						menu : [
							{
								caption : "Dodaj novi <akt:Pododeljak>",
								action : Xonomy.addPododeljakChildElement,
								actionParameter : "<akt:Pododeljak xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Član");
								}
							}, {
								caption : "Dodaj novi <akt:Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<akt:Član xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Pododeljak");
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
					"akt:Pododeljak" : {
						menu : [
							{
								caption : "Dodaj novi <akt:Član>",
								action : Xonomy.addClanChildElement,
								actionParameter : "<akt:Član xmlns:akt=\"http://www.tim9.com/akt\"/>"
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
					"akt:Član" : {
						menu : [
							{
								caption : "Dodaj novi <akt:Stav>",
								action : Xonomy.addStavChildElement,
								actionParameter : "<akt:Stav xmlns:akt=\"http://www.tim9.com/akt\"/>",
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
					"akt:Stav" : {
						menu : [
							{
								caption : "Dodaj novu <akt:Tačka>",
								action : Xonomy.addTackaChildElement,
								actionParameter : "<akt:Tačka xmlns:akt=\"http://www.tim9.com/akt\"/>"
							}, {
								caption : "Dodaj novi <akt:Sadrzaj>",
								action : Xonomy.newElementChild,
								actionParameter : "<akt:Sadrzaj xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Sadrzaj");
								}
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						]
					},
					
					// ########################################
					// ----------- Tacka elements ------------
					"akt:Tačka" : {
						menu : [
							{
								caption : "Dodaj novi <akt:Podtačka>",
								action : Xonomy.addPodtackaChildElement,
								actionParameter : "<akt:Podtačka xmlns:akt=\"http://www.tim9.com/akt\"/>",
							}, {
								caption : "Dodaj novi <akt:Sadrzaj>",
								action : Xonomy.newElementChild,
								actionParameter : "<akt:Sadrzaj xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Sadrzaj");
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
						mustBeAfter : [ "akt:Sadrzaj" ],
						attributes : {
							"redniBroj" : {
								asker : Xonomy.askString
							}
						}
					},
					
					// ########################################
					// --------- Podtacka elements -----------
					"akt:Podtačka" : {
						menu : [
							{
								caption : "Dodaj novu <akt:Alineja>",
								action : Xonomy.addAlinejaChildElement,
								actionParameter : "<akt:Alineja xmlns:akt=\"http://www.tim9.com/akt\"/>"
							}, {
								caption : "Dodaj novi <akt:Sadrzaj>",
								action : Xonomy.newElementChild,
								actionParameter : "<akt:Sadrzaj xmlns:akt=\"http://www.tim9.com/akt\"/>",
								hideIf : function(jsElement) {
									return jsElement.hasChildElement("akt:Sadrzaj");
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
					"akt:Alineja" : {
						menu : [
							{
								caption : "Dodaj novi <akt:Sadrzaj>",
								action : Xonomy.newElementChild,
								actionParameter : "<akt:Sadrzaj xmlns:akt=\"http://www.tim9.com/akt\"/>"
							}, {
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						]
					},
					
					"akt:Sadrzaj" : {
						hasText : true,
						menu : [
							{
								caption : "Obriši",
								action : Xonomy.deleteElement
							}
						],
						inlineMenu : [
							{
								caption : "Dodaj <akt:Referenca>",
								action : Xonomy.wrap,
								actionParameter : {
									template : "<Rakt:eferenca>$</akt:Referenca>",
									placeholder : "$"
								}
							}
						],
						mustBeBefore : [ "akt:Tačka", "akt:Podtačka", "akt:Alineja" ]
					},
					
					"akt:Referenca" : {
						hasText : false,
						menu : [ 
							{
								caption : "Obriši <akt:Referenca>",
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

			var xml = "<akt:Akt xmlns:akt=\"http://www.tim9.com/akt\" " +
						   "xmlns=\"http://www.w3.org/ns/rdfa#\" " +
						   "xmlns:pred=\"http://www.tim9.com/akt/rdf/predikati/\" " +
						   "xmlns:xs=\"http://www.w3.org/2001/XMLSchema#\" " +
						   "xmlns:korisnik=\"http://www.tim9.com/korisnik\" " +
					       "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
					   "</akt:Akt>";
			
			var insEditor = document.getElementById("xmlEditor");
			
			Xonomy.setMode("laic");
			Xonomy.render(xml, insEditor, docSpec);
		}
		setupXonomy();
		
		vm.addAkt = function() {
			vm.xmlObject.xml = Xonomy.harvest();
			DodajAktFactory.addAkt(vm.xmlObject).then(
				function(data) {
					if (data == null) {
						vm.error = true;
						vm.successful = false;
					} else {
						vm.error = false;
						vm.successful = true;
						$timeout(function(){$location.path('/mojiPredlozi');}, 3000);
					}
				}
			);
		}
		
	}

})(angular);