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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

import tim9.xml.DTO.AktDTO;
import tim9.xml.DTO.MetadataAktDTO;
import tim9.xml.DTO.SearchDTO;
import tim9.xml.DTO.XmlObjectDTO;
import tim9.xml.model.akt.Akt;
import tim9.xml.model.akt.Preambula;
import tim9.xml.model.korisnik.Korisnik;
import tim9.xml.rdf.AktMetadata;
import tim9.xml.rdf.AktSPARQL;
import tim9.xml.services.AktService;
import tim9.xml.services.AmandmanService;
import tim9.xml.transformation.TransformationAkt;
import tim9.xml.util.Util;
import tim9.xml.xpath.XPathAkt;
import tim9.xml.xquery.XQueryAkt;

@Controller
@RequestMapping(value = "xmlWS/akt")
public class AktController implements ErrorHandler {

	@Autowired
	AktService aktService;

	@Autowired
	AmandmanService amandmanService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Akt> saveAkt(@RequestBody XmlObjectDTO xmlObjectDTO) {

		// preuzmem string xml-a amandmana
		String xml = xmlObjectDTO.getXml();

		if (xmlObjectDTO.getUser() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

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

			Element korisnik = addUser(xmlObjectDTO, doc);
			aktElement.appendChild(korisnik);
			// korisnik.removeAttribute("xmlns");
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

	private Element addUser(XmlObjectDTO xmlObjectDTO, Document doc) {
		Element korisnik = doc.createElement("korisnik:Korisnik");
		Element uloga = doc.createElement("korisnik:Uloga");
		uloga.appendChild(doc.createTextNode(xmlObjectDTO.getUser().getUloga()));
		Element ime = doc.createElement("korisnik:Ime");
		ime.appendChild(doc.createTextNode(xmlObjectDTO.getUser().getIme()));
		Element prezime = doc.createElement("korisnik:Prezime");
		prezime.appendChild(doc.createTextNode(xmlObjectDTO.getUser().getPrezime()));
		Element email = doc.createElement("korisnik:Email");
		email.appendChild(doc.createTextNode(xmlObjectDTO.getUser().getEmail()));
		Element lozinka = doc.createElement("korisnik:Lozinka");
		lozinka.appendChild(doc.createTextNode(xmlObjectDTO.getUser().getLozinka()));

		korisnik.appendChild(uloga);
		korisnik.appendChild(ime);
		korisnik.appendChild(prezime);
		korisnik.appendChild(email);
		korisnik.appendChild(lozinka);

		return korisnik;
	}

	private String addNamespaces(String xml, XmlObjectDTO xmlObjectDTO) {
		xml = xml.replaceAll("property=\"", "property=\"pred:");

		String putanjaDoSeme = System.getProperty("user.dir") + "/data/akt_schema.xsd";
		putanjaDoSeme = putanjaDoSeme.replace("\\", "/");

		xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.tim9.com/akt file:/"
						+ putanjaDoSeme + "\" ");

		xml = xml.replace("xmlns:akt=\"http://www.tim9.com/akt\"",
				"xmlns:akt=\"http://www.tim9.com/akt\" xmlns=\"http://www.w3.org/ns/rdfa#\" "
						+ "xmlns:pred=\"http://www.tim9.com/akt/rdf/predikati/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema#\"");

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
	public ResponseEntity<String> getHTML(@PathVariable String id) {
		String aktXML = aktService.getOne(id);
		String html = "";

		if (aktXML == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
			html = new TransformationAkt().generateHTML(aktXML);
		} catch (TransformerException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(html, HttpStatus.OK);
	}

	@RequestMapping(value = "/json/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getJSON(@PathVariable String id) throws IOException {

		Akt akt = aktService.findAktDocId("akti/" + id);

		if (akt == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Preambula preambula = akt.getPreambula();

		MetadataAktDTO metadataAktDTO = new MetadataAktDTO();

		metadataAktDTO.setBrojGlasovaProtiv(preambula.getBrojGlasovaProtiv().getValue());
		metadataAktDTO.setBrojGlasovaUzdrzano(preambula.getBrojGlasovaUzdrzano().getValue());
		metadataAktDTO.setBrojGlasovaZa(preambula.getBrojGlasovaZa().getValue());
		metadataAktDTO.setStatus(preambula.getStatus().getValue());
		metadataAktDTO.setOblast(preambula.getOblast().getValue());

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Calendar datumObjave = preambula.getDatumObjave().getValue().toGregorianCalendar();
		metadataAktDTO.setDatumObjave(formatter.format(datumObjave.getTime()));

		Calendar datumPredaje = preambula.getDatumPredaje().getValue().toGregorianCalendar();
		metadataAktDTO.setDatumPredaje(formatter.format(datumPredaje.getTime()));

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(metadataAktDTO);

		byte[] jsonFile = jsonString.getBytes(Charset.forName("UTF-8"));

		return new ResponseEntity<byte[]>(jsonFile, HttpStatus.OK);
	}

	@RequestMapping(value = "/rdf/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getRDF(@PathVariable String id) throws IOException {

		Akt akt = aktService.findAktDocId("akti/" + id);

		if (akt == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		String rdfString = AktMetadata.getMetaData(Util.loadProperties(), id);

		byte[] rdf = rdfString.getBytes(Charset.forName("UTF-8"));

		return new ResponseEntity<byte[]>(rdf, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<AktDTO>> getAll() {
		List<AktDTO> retVal = new ArrayList<>();
		List<Akt> sviAkti = aktService.findAll();

		if (sviAkti == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		for (Akt akt : sviAkti) {
			String aktXML = aktService.getOne(akt.getId());
			String text = XPathAkt.generateText(aktXML);

			retVal.add(new AktDTO(akt, text));
		}

		return new ResponseEntity<List<AktDTO>>(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/pretraga/{criteria}", method = RequestMethod.GET)
	public ResponseEntity<List<AktDTO>> searchByText(@PathVariable String criteria) {
		List<AktDTO> retVal = new ArrayList<>();
		Map<String, String> pronadjeniAkti = null;

		try {
			pronadjeniAkti = XQueryAkt.searchAktsByText(Util.loadProperties(), criteria);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (pronadjeniAkti == null)
			return new ResponseEntity<>(HttpStatus.OK);

		for (String docId : pronadjeniAkti.keySet()) {
			Akt akt = aktService.findAktDocId(docId);

			if (!akt.getPreambula().getStatus().getValue().equalsIgnoreCase("odbijen")) {
				String text = pronadjeniAkti.get(docId);
				if (text.length() > 120) {
					text = text.substring(0, 117);
				}
				AktDTO aktDTO = new AktDTO(akt, text);
				retVal.add(aktDTO);
			}
		}

		return new ResponseEntity<List<AktDTO>>(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/filter", method = RequestMethod.POST)
	public ResponseEntity<List<AktDTO>> searchByMetaData(@RequestBody SearchDTO searchDTO) {
		List<AktDTO> retVal = new ArrayList<>();
		List<Akt> nadjeniAkti = null;

		try {
			nadjeniAkti = AktSPARQL.searchMetaData(Util.loadProperties(), searchDTO);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (nadjeniAkti == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		nadjeniAkti.removeAll(Collections.singleton(null));

		for (Akt akt : nadjeniAkti) {
			String aktXML = aktService.getOne(akt.getId());
			String text = XPathAkt.generateText(aktXML);

			retVal.add(new AktDTO(akt, text));
		}

		return new ResponseEntity<List<AktDTO>>(retVal, HttpStatus.OK);
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

	@RequestMapping(value = "/povuci/{id}", method = RequestMethod.GET)
	public ResponseEntity<Void> povuciAkt(@PathVariable String id) throws FileNotFoundException, IOException,
			TransformerFactoryConfigurationError, ParserConfigurationException, TransformerException, SAXException {

		String docId = "akti/" + id;
		Akt akt = aktService.findAktDocId(docId);

		if (akt == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		aktService.delete(id);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/reference/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Akt>> preuzmiReferenceNaAkt(@PathVariable String id)
			throws ParserConfigurationException, SAXException, IOException {

		HashMap<String, Integer> reference = null;

		reference = XQueryAkt.findReferences(Util.loadProperties(), id);

		if (reference == null)
			return new ResponseEntity<>(HttpStatus.OK);

		List<Akt> retVal = new ArrayList<>();

		for (String docId : reference.keySet()) {

			Akt akt = aktService.findAktDocId(docId);
			retVal.add(akt);

		}

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/prihvatiUNacelu/{id}", method = RequestMethod.GET)
	public ResponseEntity<Akt> prihvatiUNacelu(@PathVariable String id) throws SAXException,
			ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {

		String docId = "akti/" + id;
		Akt akt = aktService.findAktDocId(docId);

		try {
			aktService.azurirajStatusAkta(akt);
			aktService.azurirajMetapodatke(akt.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Akt>(akt, HttpStatus.OK);
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
