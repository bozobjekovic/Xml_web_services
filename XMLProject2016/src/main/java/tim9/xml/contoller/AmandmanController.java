package tim9.xml.contoller;

import static org.apache.xerces.jaxp.JAXPConstants.JAXP_SCHEMA_LANGUAGE;
import static org.apache.xerces.jaxp.JAXPConstants.W3C_XML_SCHEMA;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
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

import tim9.xml.DTO.XmlObjectDTO;
import tim9.xml.model.amandman.Amandman;
import tim9.xml.services.AmandmanService;
import tim9.xml.transformation.TransformationAmandman;

@Controller
@RequestMapping(value="xmlWS/amandman")
public class AmandmanController implements ErrorHandler {

	@Autowired
	AmandmanService amandmanService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Amandman> saveAmandman(@RequestBody XmlObjectDTO xmlObjectDTO) {
		
		/* Preuzmem string xml-a amandmana */
		String xml = xmlObjectDTO.getXml();
		
		String putanjaDoSeme = System.getProperty("user.dir") + "/data/amandman_schema.xsd";
		putanjaDoSeme = putanjaDoSeme.replace("\\", "/");

		xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.tim9.com/amandman file:/"
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
			
			/* Generisanje ID-a */
			int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
			
			/* Dodavanje nedostajucih elemenata i atributa */
			Element amandmanElement = doc.getDocumentElement();
			amandmanElement.setAttribute("id", id + "");
			// ####################### ABOUT
			
			/* META PODACI */
			/* Detektuju eventualne greske */
			if (doc != null)
				System.out.println("[INFO] File parsed with no errors.");
			
			Amandman amandman = amandmanService.save(doc, id);
			return new ResponseEntity<Amandman>(amandman, HttpStatus.CREATED);
			
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
	
	@RequestMapping(value = "/html/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> getHTML(@PathVariable String id){
		String amandmanXML = amandmanService.getOne(id);
		String html = "";
		
		if(amandmanXML == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try {
			html = new TransformationAmandman().generateHTML(amandmanXML);
		} catch (TransformerException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(html, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Amandman>> getAll() {
		List<Amandman> retVal = amandmanService.findAll();
		
		return new ResponseEntity<List<Amandman>>(retVal, HttpStatus.OK);
	}
	
	@Override
	public void error(SAXParseException exception) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warning(SAXParseException err) throws SAXException {
		System.out.println("[WARN] Warning, line: " + err.getLineNumber() + ", uri: " + err.getSystemId());
		System.out.println("[WARN] " + err.getMessage());
	}

}