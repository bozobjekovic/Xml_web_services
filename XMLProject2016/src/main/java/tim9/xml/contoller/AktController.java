package tim9.xml.contoller;

import static org.apache.xerces.jaxp.JAXPConstants.JAXP_SCHEMA_LANGUAGE;
import static org.apache.xerces.jaxp.JAXPConstants.W3C_XML_SCHEMA;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import tim9.xml.DTO.SearchDTO;
import tim9.xml.DTO.XmlObjectDTO;
import tim9.xml.model.akt.Akt;
import tim9.xml.model.korisnik.Korisnik;
import tim9.xml.rdf.AktSPARQL;
import tim9.xml.services.AktService;
import tim9.xml.services.AmandmanService;
import tim9.xml.transformation.TransformationAkt;
import tim9.xml.util.Util;
import tim9.xml.xquery.XQueryAkt;

@Controller
@RequestMapping(value="xmlWS/akt")
public class AktController implements ErrorHandler {
	
	@Autowired
	AktService aktService;
	
	@Autowired
	AmandmanService amandmanService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Akt> saveAkt(@RequestBody XmlObjectDTO xmlObjectDTO) {
		
		// preuzmem string xml-a amandmana
		String xml = xmlObjectDTO.getXml();
		
		xml = addNamespaces(xml, xmlObjectDTO);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(true);
		dbFactory.setNamespaceAware(true);
		dbFactory.setIgnoringComments(true);
		dbFactory.setIgnoringElementContentWhitespace(true);
		
		/* Validacija u odnosu na XML šemu. */
		dbFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			/* Postavlja error handler. */
			dBuilder.setErrorHandler(this);
			
			Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
			
			// generisanje ID-a
			int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
			
			// dodavanje nedostajucih elemenata i atributa
			Element aktElement = doc.getDocumentElement();
			aktElement.setAttribute("id", id + "");
			aktElement.setAttribute("about", "http://www.tim9.com/akt/" + id);
			
			// META PODACI
			/* Detektuju eventualne greske */
			if (doc != null)
				System.out.println("[INFO] File parsed with no errors.");
			
			Akt akt = aktService.save(doc, id, xmlObjectDTO.getUser());
			
			return new ResponseEntity<Akt>(akt, HttpStatus.CREATED);
		} catch (SAXParseException e) {
			System.out.println("[ERROR] Parsing error, line: " + e.getLineNumber() + ", uri: " + e.getSystemId());
			System.out.println("[ERROR] " + e.getMessage());
			System.out.print("[ERROR] Embedded exception: ");

			Exception embeddedException = e;
			if (e.getException() != null)
				embeddedException = e.getException();

			embeddedException.printStackTrace();

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	
	private String addNamespaces(String xml, XmlObjectDTO xmlObjectDTO) {
		xml = xml.replaceAll("property=\"", "property=\"pred:");
		
		String putanjaDoSeme = System.getProperty("user.dir") + "/data/akt_schema.xsd";
		putanjaDoSeme = putanjaDoSeme.replace("\\", "/");
		
		xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.tim9.com/akt file:/"
						+ putanjaDoSeme + "\" ");
		
		xml = xml.replace("xmlns:akt=\"http://www.tim9.com/akt\"", "xmlns:akt=\"http://www.tim9.com/akt\" xmlns=\"http://www.w3.org/ns/rdfa#\" "
				+ "xmlns:pred=\"http://www.tim9.com/akt/rdf/predikati/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema#\"");
		
		xml = xml.replace("</akt:Akt>",
							"<korisnik:Korisnik>"
									+ "<korisnik:Uloga>" + xmlObjectDTO.getUser().getUloga() + "</korisnik:Uloga>"
									+ "<korisnik:Ime>" + xmlObjectDTO.getUser().getIme() + "</korisnik:Ime>"
									+ "<korisnik:Prezime>" + xmlObjectDTO.getUser().getPrezime() + "</korisnik:Prezime>"
									+ "<korisnik:Email>" + xmlObjectDTO.getUser().getEmail() + "</korisnik:Email>"
									+ "<korisnik:Lozinka>" + xmlObjectDTO.getUser().getLozinka() + "</korisnik:Lozinka>"
						   + "</korisnik:Korisnik>"
						+ "</akt:Akt>");
		
		return xml;
		
	}
	

	@RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getAktForPDF(@PathVariable String id) {
		String aktXML = aktService.getOne(id);
		
		if (aktXML == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		byte[] pdf = null;
		
		try {
			pdf = new TransformationAkt().generatePDF(aktXML);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<byte[]>(pdf, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/html/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> getHTML(@PathVariable String id){
		String aktXML = aktService.getOne(id);
		String html = "";
		
		if(aktXML == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try {
			html = new TransformationAkt().generateHTML(aktXML);
		} catch (TransformerException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(html, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Akt>> getAll() {
		List<Akt> retVal = aktService.findAll();
		
		return new ResponseEntity<List<Akt>>(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/pretraga", method = RequestMethod.POST)
	public ResponseEntity<List<Akt>> searchByMetaData(@RequestBody SearchDTO searchDTO) {
		List<Akt> retVal = null;
		
		try {
			retVal = AktSPARQL.searchMetaData(Util.loadProperties(), searchDTO);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if (retVal == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<List<Akt>>(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/byUser", method = RequestMethod.POST)
	public ResponseEntity<List<Akt>> predloziKorisnikaAkti(@RequestBody Korisnik korisnik) throws IOException {
		List<Akt> retVal = new ArrayList<>();
		Map<String, String> pronadjeniAkti = new HashMap<>();
		
		pronadjeniAkti = XQueryAkt.searchAktsByUserEmail(Util.loadProperties(), korisnik.getEmail());
		
		for (String docID : pronadjeniAkti.keySet()) {
			Akt akt = aktService.findAktDocId(docID);
			
			if (!akt.getPreambula().getStatus().getValue().equalsIgnoreCase("odbijen")) {
				retVal.add(akt);
			}
		}

		return new ResponseEntity<List<Akt>>(retVal, HttpStatus.OK);
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
