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

import tim9.xml.model.amandman.Amandman;
import tim9.xml.rdf.MetadataExtractor;
import tim9.xml.util.Util;

@Service
public class AmandmanService {

	DatabaseClient client;
	XMLDocumentManager xmlManager;

	Util.ConnectionProperties properties;

	public AmandmanService() throws IOException {
		try {
			DatabaseClientFactory.getHandleRegistry().register(JAXBHandle.newFactory(Amandman.class));
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

	public List<Amandman> findAll() {
		List<Amandman> amandmani = new ArrayList<>();

		QueryManager queryMgr = client.newQueryManager();

		StringQueryDefinition stringQry = queryMgr.newStringDefinition();
		stringQry.setCollections("amandmani");

		SearchHandle searchHandle = queryMgr.search(stringQry, new SearchHandle());
		for (MatchDocumentSummary docSum : searchHandle.getMatchResults()) {
			Amandman a = xmlManager.readAs(docSum.getUri(), Amandman.class);
			amandmani.add(a);
		}

		return amandmani;
	}

	public Amandman save(Document amandman, int id) throws TransformerFactoryConfigurationError, TransformerException,
			JAXBException, SAXException, IOException {

		String collId = "amandmani";
		String docId = "amandmani/" + id;

		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/outputAmandman.xml"));
		Source input = new DOMSource(amandman);

		transformer.transform(input, output);

		InputStreamHandle handle = new InputStreamHandle(new FileInputStream("gen/outputAmandman.xml"));

		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance("tim9.xml.model.amandman");

		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
		// model

		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			// XML schema validacija
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new File("C:/Users/Ana/Desktop/amandman_schema.xsd"));

			// Podešavanje unmarshaller-a za XML schema validaciju
			unmarshaller.setSchema(schema);

			// Unmarshalling generiše objektni model na osnovu XML fajla
			Amandman newAmandman = (Amandman) unmarshaller.unmarshal(new File("./gen/outputAmandman.xml"));
			
			xmlManager.write(docId, metadata, handle);

			saveMD(id);
			
			return newAmandman;
		} catch (SAXException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private void saveMD(int id) throws SAXException, IOException, TransformerException {

		// Create a document manager to work with XML files.
		GraphManager graphManager = client.newGraphManager();

		// Set the default media type (RDF/XML)
		graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);

		// Referencing XML file with RDF data in attributes
		String xmlFilePath = "gen/outputAmandman.xml";

		String rdfFilePath = "gen/rdf/amandman.rdf";

		// Automatic extraction of RDF triples from XML file
		MetadataExtractor metadataExtractor = new MetadataExtractor();

		metadataExtractor.extractMetadata(new FileInputStream(new File(xmlFilePath)),
				new FileOutputStream(new File(rdfFilePath)));

		// A handle to hold the RDF content.
		FileHandle rdfFileHandle = new FileHandle(new File(rdfFilePath)).withMimetype(RDFMimeTypes.RDFXML);

		// Writing the named graph
		System.out
				.println("[INFO] Tripleti su uspesno dodati u bazu. Id trupleta: " + "amandmani/metadata/" + id + ".");
		graphManager.write("amandmani/metadata/" + id, rdfFileHandle);
	}

	public String getOne(String id) {
		String amandman;
		String docId = "amandmani/" + id;
		try {
			amandman = xmlManager.readAs(docId, String.class);
		} catch (Exception e) {
			amandman = null;
		}

		return amandman;
	}

	public Amandman getAmandmanDocID(String docId) {
		Amandman amandman;
		try {
			amandman = xmlManager.readAs(docId, Amandman.class);
		} catch (Exception e) {
			amandman = null;
		}

		return amandman;
	}

	public void azurirajStatus(Amandman amandman, String status) throws IOException, ParserConfigurationException,
			SAXException, TransformerFactoryConfigurationError, TransformerException {
		String docId = "amandmani/" + amandman.getId();

		// Initialize XQuery invoker object
		ServerEvaluationCall invoker = client.newServerEval();

		// Read the file contents into a string object
		String query = "xquery version \"1.0-ml\"; " + " declare namespace amd = \"http://www.tim9.com/amandman\";"
				+ "xdmp:node-replace(doc(\"" + docId + "\")//amd:Amandman/amd:Preambula, "
				+ "<amd:Preambula><amd:Status datatype=\"xs:string\" property=\"pred:status\">"
				+ amandman.getPreambula().getStatus().getValue() + "</amd:Status>"
				+ "<amd:BrojGlasovaZa datatype=\"xs:int\" property=\"pred:za\" xmlns=\"\">"
				+ amandman.getPreambula().getBrojGlasovaZa().getValue() + "</amd:BrojGlasovaZa>"
				+ "<amd:BrojGlasovaProtiv datatype=\"xs:int\" property=\"pred:protiv\" xmlns=\"\">"
				+ amandman.getPreambula().getBrojGlasovaProtiv().getValue() + "</amd:BrojGlasovaProtiv>"
				+ "<amd:BrojGlasovaUzdrzano datatype=\"xs:int\" property=\"pred:uzdrzano\" xmlns=\"\">"
				+ amandman.getPreambula().getBrojGlasovaUzdrzano().getValue()
				+ "</amd:BrojGlasovaUzdrzano></amd:Preambula>" + ");";

		// Invoke the query
		invoker.xquery(query);

		// Interpret the results
		invoker.eval();

		String amandmanXML = getOne(amandman.getId());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(amandmanXML)));

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/outputAmandman.xml"));
		Source input = new DOMSource(doc);

		transformer.transform(input, output);
	}

	public void azurirajMetapodatke(String id) throws TransformerException, SAXException, IOException {
		// Create a document manager to work with XML files.
		GraphManager graphManager = client.newGraphManager();

		// Set the default media type (RDF/XML)
		graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);

		String xmlFilePath = "gen/outputAmandman.xml";
		String rdfFilePath = "gen/rdf/amandman.rdf";

		MetadataExtractor metadataExtractor = new MetadataExtractor();

		metadataExtractor.extractMetadata(new FileInputStream(new File(xmlFilePath)),
				new FileOutputStream(new File(rdfFilePath)));

		// A handle to hold the RDF content.
		FileHandle rdfFileHandle = new FileHandle(new File(rdfFilePath)).withMimetype(RDFMimeTypes.RDFXML);

		// Writing the named graph
		System.out.println("[INFO] Tripleti su uspesno azurrani i dodati u bazu. Id trupleta: " + "amandmani/metadata/"
				+ id + ".");
		graphManager.write("amandmani/metadata/" + id, rdfFileHandle);
	}

	public void delete(String id) {
		try {
			xmlManager.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
