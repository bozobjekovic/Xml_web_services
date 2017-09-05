package tim9.xml.contoller;

import static org.apache.xerces.jaxp.JAXPConstants.JAXP_SCHEMA_LANGUAGE;
import static org.apache.xerces.jaxp.JAXPConstants.W3C_XML_SCHEMA;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import tim9.xml.DTO.AmandmaniAktaDTO;
import tim9.xml.DTO.MetadataAmdDTO;
import tim9.xml.DTO.XmlObjectDTO;
import tim9.xml.model.akt.Akt;
import tim9.xml.model.amandman.Amandman;
import tim9.xml.model.amandman.Preambula;
import tim9.xml.model.korisnik.Korisnik;
import tim9.xml.rdf.AktSPARQL;
import tim9.xml.rdf.AmandmanMetadata;
import tim9.xml.services.AktService;
import tim9.xml.services.AmandmanService;
import tim9.xml.transformation.TransformationAmandman;
import tim9.xml.util.Util;
import tim9.xml.xquery.XQueryAmandman;

@Controller
@RequestMapping(value = "xmlWS/amandman")
public class AmandmanController implements ErrorHandler {

	@Autowired
	AmandmanService amandmanService;

	@Autowired
	AktService aktService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Amandman> saveAmandman(@RequestBody XmlObjectDTO xmlObjectDTO) {

		/* Preuzmem string xml-a amandmana */
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

			/* Generisanje ID-a */
			int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);

			/* Dodavanje nedostajucih elemenata i atributa */
			Element amandmanElement = doc.getDocumentElement();
			amandmanElement.setAttribute("id", id + "");
			amandmanElement.setAttribute("about", "http://www.tim9.com/amandman/" + id);
			amandmanElement.setAttribute("aktURL", "akti/" + xmlObjectDTO.getAktId());
			amandmanElement.setAttribute("korisnikURL", "users/" + xmlObjectDTO.getUser().getEmail());

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

	private String addNamespaces(String xml, XmlObjectDTO xmlObjectDTO) {
		
		xml = xml.replaceAll("property=\"", "property=\"pred:");

		String putanjaDoSeme = System.getProperty("user.dir") + "/data/amandman_schema.xsd";
		putanjaDoSeme = putanjaDoSeme.replace("\\", "/");

		xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.tim9.com/amandman file:/"
						+ putanjaDoSeme + "\" ");
		
		xml = xml.replace("xmlns:amd=\"http://www.tim9.com/amandman\"", "xmlns:amd=\"http://www.tim9.com/amandman\" xmlns=\"http://www.w3.org/ns/rdfa#\" "
				+ "xmlns:pred=\"http://www.tim9.com/amandman/rdf/predikati/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema#\"");
		
		return xml;
	}

	@RequestMapping(value = "/html/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> getHTML(@PathVariable String id) {
		String amandmanXML = amandmanService.getOne(id);
		String html = "";

		if (amandmanXML == null) {
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
	
	@RequestMapping(value = "/json/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getJSON(@PathVariable String id) throws IOException {
		
		Amandman amandman = amandmanService.getAmandmanDocID("amandmani/" + id);
		
		if(amandman == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Preambula preambula = amandman.getPreambula();
		
		MetadataAmdDTO metadataAmdDTO = new MetadataAmdDTO();
		
		metadataAmdDTO.setBrojGlasovaProtiv(preambula.getBrojGlasovaProtiv().getValue());
		metadataAmdDTO.setBrojGlasovaUzdrzano(preambula.getBrojGlasovaUzdrzano().getValue());
		metadataAmdDTO.setBrojGlasovaZa(preambula.getBrojGlasovaZa().getValue());
		metadataAmdDTO.setStatus(preambula.getStatus().getValue());
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Calendar datumObjave = preambula.getDatumObjave().getValue().toGregorianCalendar();
		metadataAmdDTO.setDatumObjave(formatter.format(datumObjave.getTime()));

		Calendar datumPredaje = preambula.getDatumPredaje().getValue().toGregorianCalendar();
		metadataAmdDTO.setDatumPredaje(formatter.format(datumPredaje.getTime()));
		
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(metadataAmdDTO);

		byte[] jsonFile = jsonString.getBytes(Charset.forName("UTF-8"));

		return new ResponseEntity<byte[]>(jsonFile, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rdf/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getRDF(@PathVariable String id) throws IOException {
		
		Amandman amandman = amandmanService.getAmandmanDocID("amandmani/" + id);

		if (amandman == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		String rdfString = AmandmanMetadata.getMetaData(Util.loadProperties(), id);
		
		byte[] rdf = rdfString.getBytes(Charset.forName("UTF-8"));
		
		return new ResponseEntity<byte[]>(rdf, HttpStatus.OK);
	}

	@RequestMapping(value = "/amandmaniAkta/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Amandman>> amandmaniAkta(@PathVariable String id) throws FileNotFoundException,
			IOException, TransformerConfigurationException, ParserConfigurationException, SAXException {

		ArrayList<String> amandmani = new ArrayList<>();

		amandmani = XQueryAmandman.amandmaniAkta(Util.loadProperties(), id);

		if (amandmani == null)
			return new ResponseEntity<>(HttpStatus.OK);

		List<Amandman> retVal = new ArrayList<>();

		for (String docId : amandmani) {
			Amandman amandman = amandmanService.getAmandmanDocID(docId);
			String idAmandmana = amandman.getAktURL() + "/" + amandman.getId();
			idAmandmana = idAmandmana.replace("akti", "amandmani");
			// String amandmanXML =
			// amandmanService.getAmandmanXMLDocId(idAmandmana);
			// amandman.setTekst(XPathAmandman.generisiTekst(amandmanXML));
			retVal.add(amandman);
		}

		return new ResponseEntity<List<Amandman>>(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
	public ResponseEntity<List<AmandmaniAktaDTO>> amandmaniStatus(@PathVariable String status)
			throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException {
		
		List<AmandmaniAktaDTO> amandmaniAktaDTO = new ArrayList<>();
		List<Akt> akti = AktSPARQL.findByStatus(Util.loadProperties(), status);
		
		if(akti.size() == 0){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		for (Akt akt : akti) {
			ArrayList<String> amandmani = new ArrayList<>();
			amandmani = XQueryAmandman.amandmaniAkta(Util.loadProperties(), akt.getId());
			
			List<Amandman> amandmaniAkta = new ArrayList<>();
			for (String docId : amandmani) {
				Amandman amandman = amandmanService.getAmandmanDocID(docId);
				amandmaniAkta.add(amandman);
			}
			AmandmaniAktaDTO amdAktaDTO = new AmandmaniAktaDTO();
			amdAktaDTO.setAkt(akt);
			amdAktaDTO.setAmandmani(amandmaniAkta);

			amandmaniAktaDTO.add(amdAktaDTO);
		}

		return new ResponseEntity<List<AmandmaniAktaDTO>>(amandmaniAktaDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/korisnikovi", method = RequestMethod.POST)
	public ResponseEntity<List<Amandman>> preuzmiAmandmaneKorisnika(@RequestBody Korisnik korisnik)
			throws FileNotFoundException, IOException {

		List<Amandman> amandmani = new ArrayList<>();

		String korisnikURL = "users/" + korisnik.getEmail();
		amandmani = XQueryAmandman.searchByUserEmail(Util.loadProperties(), korisnikURL);

		return new ResponseEntity<List<Amandman>>(amandmani, HttpStatus.OK);
	}

	@RequestMapping(value = "/povuci/{id}", method = RequestMethod.GET)
	public ResponseEntity<Void> povuciAmandman(@PathVariable String id) throws FileNotFoundException, IOException,
			TransformerFactoryConfigurationError, ParserConfigurationException, TransformerException, SAXException {

		String docId = "amandmani/" + id;
		Amandman amandman = amandmanService.getAmandmanDocID(docId);

		if (amandman == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		amandmanService.delete(docId);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/povuciAmandmaneAkta/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<String>> povuciAmandmaneAkta(@PathVariable String id)
			throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException {

		List<String> amandmani = new ArrayList<>();
		amandmani = XQueryAmandman.amandmaniAkta(Util.loadProperties(), id);
		
		for (String docId : amandmani) {
			amandmanService.delete(docId);
		}

		return new ResponseEntity<>(amandmani, HttpStatus.OK);
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
