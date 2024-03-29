package tim9.xml.contoller;

import static org.apache.xerces.jaxp.JAXPConstants.JAXP_SCHEMA_LANGUAGE;
import static org.apache.xerces.jaxp.JAXPConstants.W3C_XML_SCHEMA;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import tim9.xml.DTO.RezultatiDTO;
import tim9.xml.DTO.SednicaDTO;
import tim9.xml.model.akt.Akt;
import tim9.xml.model.amandman.Amandman;
import tim9.xml.model.sednica.Sednica;
import tim9.xml.services.AktService;
import tim9.xml.services.AmandmanService;
import tim9.xml.services.SednicaService;
import tim9.xml.util.PrimeniAmandman;
import tim9.xml.util.Util;
import tim9.xml.xquery.XQueryAmandman;

@Controller
@RequestMapping(value = "xmlWS/sednica")
public class SednicaController implements ErrorHandler {

	@Autowired
	SednicaService sednicaService;
	@Autowired
	AmandmanService amandmanService;
	@Autowired
	AktService aktService;
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");

	@RequestMapping(value = "/zakaziSednicu", method = RequestMethod.POST)
	public ResponseEntity<Sednica> zakaziSednicu(@RequestBody SednicaDTO sednicaDTO) throws SAXException, IOException {

		if (sednicaDTO.getDatum() == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		Sednica sednica = new Sednica();

		// Generisanje XML dokumenta DOM parser
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(true);
		dbFactory.setNamespaceAware(true);
		dbFactory.setIgnoringComments(true);
		dbFactory.setIgnoringElementContentWhitespace(true);

		/* Validacija u odnosu na XML šemu. */
		dbFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			// generisanje ID-a
			int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
			sednica.setId(id+"");

			// Formatiranje datuma za upis u xml i u objekat
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(sednicaDTO.getDatum());
			XMLGregorianCalendar xmlDatum = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			sednica.setDatum(xmlDatum);

			sednica.setStatus(sednicaDTO.getStatus());

			Element sednicaElement = doc.createElement("sednica:Sednica");
			sednicaElement.setAttribute("Id", sednica.getId());
			sednicaElement.setAttribute("xmlns:akt", "http://www.tim9.com/akt");
			sednicaElement.setAttribute("xmlns:sednica", "http://www.tim9.com/sednica");
			sednicaElement.setAttribute("xmlns:amandman", "http://www.tim9.com/amandman");
			sednicaElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			sednicaElement.setAttribute("xsi:schemaLocation", "http://www.tim9.com/sednica ../sednica_schema.xsd");

			// potencijalno
			doc.appendChild(sednicaElement);

			Element datumElement = doc.createElement("sednica:Datum");
			datumElement.appendChild(doc.createTextNode(sednica.getDatum() + ""));
			sednicaElement.appendChild(datumElement);

			Element statusElement = doc.createElement("sednica:Status");
			statusElement.appendChild(doc.createTextNode(sednica.getStatus()));
			sednicaElement.appendChild(statusElement);

			/* Detektuju eventualne greske */
			if (doc != null)
				System.out.println("[INFO] File parsed with no errors.");

			sednicaService.save(doc, sednica.getId());

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Sednica>(sednica, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/zakazanaSednica", method = RequestMethod.GET)
	public ResponseEntity<Sednica> zakazanaSednica() {
		Sednica zakazanaSednica = null;
		List<Sednica> sednice = sednicaService.vratiSveSednice();
		if (sednice.isEmpty())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		for (Sednica s : sednice) {
			if (s.getStatus().equals("Zakazana"))
				zakazanaSednica = s;
		}
		
		return new ResponseEntity<Sednica>(zakazanaSednica, HttpStatus.OK);
	}

	@RequestMapping(value = "/prekiniSednicu/{id}", method = RequestMethod.GET)
	public ResponseEntity<Sednica> prekiniSednicu(@PathVariable String id) {
		Sednica sednica = sednicaService.nadjiSednicu(id);
		
		try {
			sednicaService.azurirajStatusSednice(sednica); // xquery za update
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Sednica>(sednica, HttpStatus.OK);
	}

	@RequestMapping(value = "/glasajAmandman", method = RequestMethod.POST)
	public ResponseEntity<Amandman> glasajAmandman(@RequestBody RezultatiDTO rezultatiDTO) throws SAXException, IOException, TransformerException, ParserConfigurationException, TransformerFactoryConfigurationError, DatatypeConfigurationException {
		
		int za = rezultatiDTO.getBrojGlasovaZa();
		int protiv = rezultatiDTO.getBrojGlasovaProtiv();
		int suzdrzano = rezultatiDTO.getBrojSuzdrzanih();
		String id = rezultatiDTO.getId();
		String status = "";
		
		GregorianCalendar date = new GregorianCalendar();
		XMLGregorianCalendar datum = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);

		Amandman amd = amandmanService.getAmandmanDocID("amandmani/" + id);
		amd.getPreambula().getBrojGlasovaZa().setValue(za);
		amd.getPreambula().getBrojGlasovaProtiv().setValue(protiv);
		amd.getPreambula().getBrojGlasovaUzdrzano().setValue(suzdrzano);
		amd.getPreambula().getDatumObjave().setValue(datum);
		
		if (za == 0 && protiv == 0 && suzdrzano == 0)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(za > protiv) {													// Usvaja se
			amd.getPreambula().getStatus().setValue("Usvojen");
			status = amd.getPreambula().getStatus().getValue();
			
			amandmanService.azurirajStatus(amd, status);
			amandmanService.azurirajMetapodatke(amd.getId());
			
			try {
				primeniAmandman(id);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<Amandman>(amd, HttpStatus.OK);
		}
		else if (za == protiv) {											// Ponovi glasanje
			return new ResponseEntity<Amandman>(HttpStatus.BAD_REQUEST);
		}
		else {																// Odbija se
			amd.getPreambula().getStatus().setValue("Odbijen");
			status = amd.getPreambula().getStatus().getValue();
			
			amandmanService.azurirajStatus(amd, status);
			amandmanService.azurirajMetapodatke(amd.getId());
			return new ResponseEntity<Amandman>(amd, HttpStatus.OK);
		}
	}
	
	private void primeniAmandman(String amdID) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		PrimeniAmandman primeniAmandman = new PrimeniAmandman();
		String amandmanXML = amandmanService.getOne(amdID);
		
		/*
		 * DEKLARACIJE ATRIBUTA 
		 * 
		 * */
		String nazivAkta = "";
		String idAkta = "";
		
		// Parse the input document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(amandmanXML)));
		
		// Set up the transformer to write the output string
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		
		// First child node
		NodeList nl = doc.getDocumentElement().getChildNodes();
		DOMSource source = null;
		
		for (int x = 0; x < nl.getLength(); x++) {
			Node e = nl.item(x);
			if (e instanceof Element && e.getNodeName().equals("amd:Sadrzaj")) {
				NodeList sadrzajLista = e.getChildNodes();
				NodeList predlozenoResenje = null;
				
				for (int i = 0; i < sadrzajLista.getLength(); i++) {
					String nazivCvora = sadrzajLista.item(i).getNodeName().toLowerCase();
					
					if (nazivCvora.contains("naziv"))
						nazivAkta = sadrzajLista.item(i).getTextContent();
					
					if (nazivCvora.contains("odredba"))
						primeniAmandman.setOdredba(sadrzajLista.item(i).getTextContent());
					
					if (nazivCvora.contains("predlozenoresenje"))
						primeniAmandman.setPredlozenoResenje(sadrzajLista.item(i).getTextContent());
					
					if (nazivCvora.contains("predlog"))
						predlozenoResenje = sadrzajLista.item(i).getChildNodes();
				} 
				
				idAkta = nazivAkta.split(" - ")[1];
				primeniAmandman.setDocIDAkt("akti/" + idAkta);
				
				if (primeniAmandman.getPredlozenoResenje().equalsIgnoreCase("brisanje")) {
					XQueryAmandman.primeniAmandman(Util.loadProperties(), primeniAmandman);
				} else {
					for (int i = predlozenoResenje.getLength() - 1; i >= 0; i--) {
						sw = new StringWriter();
						result = new StreamResult(sw);
						
						source = new DOMSource(predlozenoResenje.item(i));
						transformer.transform(source, result);
						
						primeniAmandman.setPatch(sw.toString());
						
						XQueryAmandman.primeniAmandman(Util.loadProperties(), primeniAmandman);
						
						sw.flush();
						result.getWriter().flush();
					}
				}
			}
		}
		
		String aktXML = aktService.getOne(idAkta);
		
		DocumentBuilderFactory factoryAkt = DocumentBuilderFactory.newInstance();
		DocumentBuilder builderAkt = factoryAkt.newDocumentBuilder();
		Document docAkt = builderAkt.parse(new InputSource(new StringReader(aktXML)));
		
		// AZURIRAJ ID
		// AZURIRAJ Redni Broj
		aktService.azuriajAkt(docAkt, primeniAmandman.getDocIDAkt());
		
	}
	
	@RequestMapping(value = "/glasajAkt", method = RequestMethod.POST)
	public ResponseEntity<Akt> glasajAkt(@RequestBody RezultatiDTO rezultatiDTO) throws SAXException, IOException, TransformerException, ParserConfigurationException, TransformerFactoryConfigurationError, DatatypeConfigurationException {
		
		int za = rezultatiDTO.getBrojGlasovaZa();
		int protiv = rezultatiDTO.getBrojGlasovaProtiv();
		int suzdrzano = rezultatiDTO.getBrojSuzdrzanih();
		String id = rezultatiDTO.getId();
		String status = "";
		
		GregorianCalendar date = new GregorianCalendar();
		XMLGregorianCalendar datum = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);

		Akt akt = aktService.findAktDocId("akti/" + id);
		akt.getPreambula().getBrojGlasovaZa().setValue(za);
		akt.getPreambula().getBrojGlasovaProtiv().setValue(protiv);
		akt.getPreambula().getBrojGlasovaUzdrzano().setValue(suzdrzano);
		akt.getPreambula().getDatumObjave().setValue(datum);
		
		if (za == 0 && protiv == 0 && suzdrzano == 0)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(za > protiv) {													// Usvaja se
			akt.getPreambula().getStatus().setValue("Usvojen");
			status = akt.getPreambula().getStatus().getValue();
			
			aktService.azurirajStatusUCelosti(akt, status);
			aktService.azurirajMetapodatke(akt.getId());
			
			return new ResponseEntity<Akt>(akt, HttpStatus.OK);
		}
		else if (za == protiv) {											// Ponovi glasanje
			return new ResponseEntity<Akt>(HttpStatus.BAD_REQUEST);
		}
		else {																// Odbija se
			akt.getPreambula().getStatus().setValue("Odbijen");
			status = akt.getPreambula().getStatus().getValue();
			
			aktService.azurirajStatusUCelosti(akt, status);
			aktService.azurirajMetapodatke(akt.getId());
			return new ResponseEntity<Akt>(akt, HttpStatus.OK);
		}
	}
	
	@Override
	public void error(SAXParseException arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void fatalError(SAXParseException arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void warning(SAXParseException err) throws SAXException {
		System.out.println("[WARN] Warning, line: " + err.getLineNumber() + ", uri: " + err.getSystemId());
		System.out.println("[WARN] " + err.getMessage());
	}

}
