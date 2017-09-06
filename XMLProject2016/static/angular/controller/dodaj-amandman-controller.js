angular
	.module('xmlWebServices.controllers')
	.controller('DodajAmandmanController', DodajAmandmanController);

DodajAmandmanController.$inject = ['$scope', '$location', '$localStorage', 'DodajAmandmanFactory', 'AktiFactory'];

function DodajAmandmanController($scope, $location, $localStorage, DodajAmandmanFactory, AktiFactory) {

	var vm = this;
	
	vm.successful = false;
	vm.error = false;
	vm.xmlObject = {};
	
	sviAkti = [];
	sveOdredbeIzabranogAkta = [];
	
	izabranaOdredba = "";
	
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
							caption : "Dodaj pojedinačne amandmane <amd:Sadržaj>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:Sadržaj xmlns:amd=\"http://www.tim9.com/amandman\"/>"
						}
					]
				},
				
				"amd:Naslov" : {
					hasText : true,
					menu : [ {
						caption : "Obriši",
						action : Xonomy.deleteElement
					} ],
				},
				
				"amd:PravniOsnov" : {
					hasText : true,
					mustBeAfter : [ "amd:Naslov" ],
					menu : [ {
						caption : "Obriši",
						action : Xonomy.deleteElement
					} ],
				},
				
				"amd:Sadržaj" : {
					menu : [
						{
							caption : "Dodaj naziv akta <amd:NazivAkta>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:NazivAkta xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:NazivAkta");
							}
						}, {
							caption : "Odaberi <amd:OdredbaAkta>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:OdredbaAkta xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:OdredbaAkta");
							}
						}, {
							caption : "Odaberi <amd:PredloženoRešenje>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:PredloženoRešenje xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:PredloženoRešenje");
							}
						}, {
							caption : "Dodaj <amd:Obrazloženje>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:Obrazloženje xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:Obrazloženje");
							}
						}, {
							caption : "Dodaj <amd:Predlog>",
							action : Xonomy.addPredlozenoResenjeChildElement,
							actionParameter : "<amd:Predlog xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:Predlog");
							}
						}
					],
					mustBeAfter : [ "amd:PravniOsnov" ]
				},
				
				"amd:NazivAkta" : {
					hasText : true,
					asker : Xonomy.askPicklistAkti,
					askerParameter : sviAkti,
				},
				
				"amd:OdredbaAkta" : {
					hasText : true,
					asker : Xonomy.askPicklistOdredbaAkta,
					askerParameter : sveOdredbeIzabranogAkta,
					mustBeAfter : [ "amd:NazivAkta" ]
				},
				
				"amd:PredloženoRešenje" : {
					hasText : true,
					asker : Xonomy.askPicklist,
					askerParameter : [ "Dodavanje", "Izmena", "Brisanje" ],
					mustBeAfter : [ "amd:OdredbaAkta" ]
				},
				
				"amd:Obrazloženje" : {
					menu : [
						{
							caption : "Dodaj <amd:RazlogPodnošenja>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:RazlogPodnošenja xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:RazlogPodnošenja");
							}
						}, {
							caption : "Dodaj <amd:Objašnjenje>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:Objašnjenje xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:Objašnjenje");
							}
						}, {
							caption : "Dodaj <amd:CiljPodnošenja>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:CiljPodnošenja xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:CiljPodnošenja");
							}
						}, {
							caption : "Dodaj <amd:ProcenaUticaja>",
							action : Xonomy.newElementChild,
							actionParameter : "<amd:ProcenaUticaja xmlns:amd=\"http://www.tim9.com/amandman\"/>",
							hideIf : function(jsElement) {
								return jsElement
										.hasChildElement("amd:ProcenaUticaja");
							}
						}
					],
					mustBeAfter : [ "amd:PredloženoRešenje" ]
				},
				
				"amd:Predlog" : {
					menu : [
						{
							caption : "Dodaj <Default>",
							action : Xonomy.newElementChild,
							actionParameter : "<Default/>",
							hideIf : function(jsElement) {
								return jsElement.hasChildElement("Default");
							}
						}, {
							caption : "Obriši",
							action : Xonomy.deleteElement
						}
					],
					mustBeAfter : [ "amd:Obrazloženje" ]
				},
				
				"amd:RazlogPodnošenja" : {
					hasText : true,
					menu : [
						{
							caption : "Obriši",
							action : Xonomy.deleteElement
						}
					]
				},
				
				"amd:Objašnjenje" : {
					hasText : true,
					menu : [
						{
							caption : "Obriši",
							action : Xonomy.deleteElement
						}
					],
					mustBeAfter : [ "amd:RazlogPodnošenja" ]
				},
				
				"amd:CiljPodnošenja" : {
					hasText : true,
					menu : [
						{
							caption : "Obriši",
							action : Xonomy.deleteElement
						}
					],
					mustBeAfter : [ "amd:Objašnjenje" ]
				},
				
				"amd:ProcenaUticaja" : {
					hasText : true,
					menu : [
						{
							caption : "Obriši",
							action : Xonomy.deleteElement
						}
					],
					mustBeAfter : [ "amd:CiljPodnošenja" ]
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
	
		var xml = "<amd:Amandman xmlns:amd=\"http://www.tim9.com/amandman\" " +
					   "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				  "</amd:Amandman>";
		
		var insEditor = document.getElementById("xmlEditor");
		
		Xonomy.setMode("laic");
		Xonomy.render(xml, insEditor, docSpec);
	}
	
	function getAllAkts() {
		AktiFactory.getAll().then(
			function(data) {
				sviAkti = data;
				setupXonomy();
			}
		);
	}
	getAllAkts();
	
	preuzmiSveIdIzabranogAkta = function() {
		nazivId = nazivId.substring(16, nazivId.length - 16);
		nazivId = nazivId.split("-")[1].split(" ")[1];
		// u VM id za cuvanje ...............
		AktiFactory.getIDOdredbeAkta(nazivId).then(
			function(data) {
				if (data == null) {
					alert("Error - preuzimanje  IDs odredbi za izabrani akt!");
				} else {
					sveOdredbeIzabranogAkta = data;
				}
			}
		);
	}
	
	vm.addAmandman = function() {
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
	}

}