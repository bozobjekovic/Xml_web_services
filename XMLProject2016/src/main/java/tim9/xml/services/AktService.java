package tim9.xml.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.eval.ServerEvaluationCall;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;

import tim9.xml.model.akt.Akt;
import tim9.xml.model.korisnik.Korisnik;
import tim9.xml.rdf.MetadataExtractor;
import tim9.xml.util.Util;

@Service
public class AktService {

	DatabaseClient client;
	XMLDocumentManager xmlManager;

	Util.ConnectionProperties properties;

	public AktService() throws IOException {
		try {
			DatabaseClientFactory.getHandleRegistry().register(JAXBHandle.newFactory(Akt.class));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		properties = Util.loadProperties();
		client = DatabaseClientFactory.newClient(properties.host, properties.port, properties.database, properties.user,
				properties.password, properties.authType);
		xmlManager = client.newXMLDocumentManager();
	}

	public void release() {
		client.release();
	}

	public List<Akt> findAll() {
		List<Akt> akti = new ArrayList<>();

		QueryManager queryMgr = client.newQueryManager();

		StringQueryDefinition stringQry = queryMgr.newStringDefinition();
		stringQry.setCollections("akti");

		SearchHandle searchHandle = queryMgr.search(stringQry, new SearchHandle());
		for (MatchDocumentSummary docSum : searchHandle.getMatchResults()) {
			Akt a = xmlManager.readAs(docSum.getUri(), Akt.class);
			akti.add(a);
		}

		return akti;
	}

	public Akt save(Document akt, int id, Korisnik korisnik) throws TransformerFactoryConfigurationError,
			TransformerException, JAXBException, SAXException, IOException {

		String collId = "akti";
		String docId = "akti/" + id;

		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/output.xml"));
		Source input = new DOMSource(akt);

		transformer.transform(input, output);

		InputStreamHandle handle = new InputStreamHandle(new FileInputStream("gen/output.xml"));

		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance("tim9.xml.model.akt");

		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
		// model

		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			// XML schema validacija
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new File("data/akt_schema.xsd"));

			// Podešavanje unmarshaller-a za XML schema validaciju
			unmarshaller.setSchema(schema);

			// Unmarshalling generiše objektni model na osnovu XML fajla
			Akt newAkt = (Akt) unmarshaller.unmarshal(new File("./gen/output.xml"));

			xmlManager.write(docId, metadata, handle);

			saveMD(Integer.toString(id));

			return newAkt;
		} catch (SAXException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private void saveMD(String id) throws SAXException, IOException, TransformerException {

		// Create a document manager to work with XML files.
		GraphManager graphManager = client.newGraphManager();

		// Set the default media type (RDF/XML)
		graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);

		// Referencing XML file with RDF data in attributes
		String xmlFilePath = "./gen/output.xml";

		String rdfFilePath = "gen/rdf/akt.rdf";

		// Automatic extraction of RDF triples from XML file
		MetadataExtractor metadataExtractor = new MetadataExtractor();

		metadataExtractor.extractMetadata(new FileInputStream(new File(xmlFilePath)),
				new FileOutputStream(new File(rdfFilePath)));

		// A handle to hold the RDF content.
		FileHandle rdfFileHandle = new FileHandle(new File(rdfFilePath)).withMimetype(RDFMimeTypes.RDFXML);

		// Writing the named graph
		System.out.println("[INFO] Tripleti su uspesno dodati u bazu. Id tripleta: " + "akti/metadata/" + id + ".");
		graphManager.write("akti/metadata/" + id, rdfFileHandle);
	}

	public String getOne(String id) {
		String akt;
		String docId = "akti/" + id;
		try {
			akt = xmlManager.readAs(docId, String.class);
		} catch (Exception e) {
			akt = null;
		}

		return akt;
	}

	public Akt findAktDocId(String docId) {
		Akt akt;
		try {
			akt = xmlManager.readAs(docId, Akt.class);
		} catch (Exception e) {
			akt = null;
		}

		return akt;
	}

	public void azurirajStatusAkta(Akt akt) throws IOException, SAXException, ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		String docId = "akti/" + akt.getId();
		akt.getPreambula().getStatus().setValue("U nacelu");

		// Initialize XQuery invoker object
		ServerEvaluationCall invoker = client.newServerEval();

		// Read the file contents into a string object
		String query = "xquery version \"1.0-ml\";" + " declare namespace akt = \"http://www.tim9.com/akt\";"
				+ " xdmp:node-replace(doc(\"" + docId + "\")//akt:Akt/akt:Preambula/akt:Status,"
				+ " <akt:Status datatype=\"xs:string\" property=\"pred:status\">"
				+ akt.getPreambula().getStatus().getValue() + "</akt:Status>);";

		// Invoke the query
		invoker.xquery(query);

		// Interpret the results
		invoker.eval();

		String aktXML = getOne(akt.getId());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(aktXML)));

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/output.xml"));
		Source input = new DOMSource(doc);

		transformer.transform(input, output);
	}

	public void azurirajStatusUCelosti(Akt akt, String status) throws ParserConfigurationException, SAXException,
			IOException, TransformerFactoryConfigurationError, TransformerException {

		String docId = "akti/" + akt.getId();

		// Initialize XQuery invoker object
		ServerEvaluationCall invoker = client.newServerEval();

		String query = "xquery version \"1.0-ml\"; " + " declare namespace akt = \"http://www.tim9.com/akt\";"
				+ "xdmp:node-replace(doc(\"" + docId + "\")//akt:Akt/akt:Preambula, "
				+ "<akt:Preambula>"
				+ "<akt:PravniOsnov>" + akt.getPreambula().getPravniOsnov() + "</akt:PravniOsnov>"
				+ "<akt:NazivOrgana>" + akt.getPreambula().getNazivOrgana() + "</akt:NazivOrgana>"
				+ "<akt:Oblast datatype=\"xs:string\" property=\"pred:status\">"
				+ akt.getPreambula().getOblast() + "</akt:Oblast>"
				+ "<akt:Status datatype=\"xs:string\" property=\"pred:status\">"
				+ status + "</akt:Status>"
				+ "<akt:DatumPredaje datatype=\"xs:date\" property=\"pred:datumPredaje\">"
				+ akt.getPreambula().getDatumPredaje().getValue() + "</akt:DatumPredaje>"
				+ "<akt:DatumObjave datatype=\"xs:date\" property=\"pred:datumObjave\">"
				+ akt.getPreambula().getDatumObjave().getValue() + "</akt:DatumObjave>"
				+ "<akt:BrojGlasovaZa datatype=\"xs:int\" property=\"pred:za\" xmlns=\"\">"
				+ akt.getPreambula().getBrojGlasovaZa().getValue() + "</akt:BrojGlasovaZa>"
				+ "<akt:BrojGlasovaProtiv datatype=\"xs:int\" property=\"pred:protiv\" xmlns=\"\">"
				+ akt.getPreambula().getBrojGlasovaProtiv().getValue() + "</akt:BrojGlasovaProtiv>"
				+ "<akt:BrojGlasovaUzdrzano datatype=\"xs:int\" property=\"pred:uzdrzano\" xmlns=\"\">"
				+ akt.getPreambula().getBrojGlasovaUzdrzano().getValue() + "</akt:BrojGlasovaUzdrzano></akt:Preambula>"
				+ ");";

		// Invoke the query
		invoker.xquery(query);

		// Interpret the results
		invoker.eval();

		String aktXML = getOne(akt.getId());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(aktXML)));

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/output.xml"));
		Source input = new DOMSource(doc);

		transformer.transform(input, output);
	}

	public void azurirajMetapodatke(String id) {
		// Create a document manager to work with XML files.
		GraphManager graphManager = client.newGraphManager();

		// Set the default media type (RDF/XML)
		graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);

		String xmlFilePath = "gen/output.xml";
		String rdfFilePath = "gen/rdf/akt.rdf";

		MetadataExtractor metadataExtractor;
		try {
			metadataExtractor = new MetadataExtractor();
			metadataExtractor.extractMetadata(new FileInputStream(new File(xmlFilePath)),
					new FileOutputStream(new File(rdfFilePath)));
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}

		// A handle to hold the RDF content.
		FileHandle rdfFileHandle = new FileHandle(new File(rdfFilePath)).withMimetype(RDFMimeTypes.RDFXML);

		// Writing the named graph
		System.out.println(
				"[INFO] Tripleti su uspesno azurirani i dodati u bazu. Id tripleta: " + "akti/metadata/" + id + ".");
		graphManager.write("akti/metadata/" + id, rdfFileHandle);
	}

	public void delete(String id) {
		try {
			String docId = "akti/" + id;
			xmlManager.delete(docId);
			docId = "akti/metadata/" + id;
			xmlManager.delete(docId);
			// TRIPLETSTORE
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
