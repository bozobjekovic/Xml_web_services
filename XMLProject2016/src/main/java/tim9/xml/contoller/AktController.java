package tim9.xml.contoller;

import static org.apache.xerces.jaxp.JAXPConstants.JAXP_SCHEMA_LANGUAGE;
import static org.apache.xerces.jaxp.JAXPConstants.W3C_XML_SCHEMA;

import java.io.StringReader;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import tim9.xml.DTO.XmlObjectDTO;
import tim9.xml.model.akt.Akt;
import tim9.xml.services.AktService;
import tim9.xml.services.AmandmanService;
import tim9.xml.transformation.TransformationAkt;

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
		
		String putanjaDoSeme = System.getProperty("user.dir") + "/data/akt_schema.xsd";
		putanjaDoSeme = putanjaDoSeme.replace("\\", "/");

		xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.tim9.com/akt file:/"
						+ putanjaDoSeme + "\" ");
		
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
			// ####################### ABOUT
			
			// META PODACI
			/* Detektuju eventualne greske */
			if (doc != null)
				System.out.println("[INFO] File parsed with no errors.");
			
			Akt akt = aktService.save(doc, id);
			
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
	
	@RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getAktForPDF(@PathVariable String id) {
		String aktXML = aktService.getOne(id);
		
		if (aktXML == null) {
			System.out.println("NIJE PRONASAO AKT!");
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
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Akt>> getAll() {
		List<Akt> retVal = aktService.findAll();
		
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
