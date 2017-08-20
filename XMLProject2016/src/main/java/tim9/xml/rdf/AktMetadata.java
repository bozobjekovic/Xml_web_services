package tim9.xml.rdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;

import tim9.xml.model.akt.Akt;
import tim9.xml.util.Util;
import tim9.xml.util.Util.ConnectionProperties;

public class AktMetadata {

	private static DatabaseClient client;
	
	static XMLDocumentManager xmlManager;

	private static final String AKT_GRAPH_URI = "akti/metadata/";

	public static String getMetaData(ConnectionProperties props, String aktId) throws IOException {

		// Initialize the database client
		if (props.database.equals("")) {
			System.out.println("[INFO] Using default database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password,
					props.authType);
		} else {
			System.out.println("[INFO] Using \"" + props.database + "\" database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
		}

		String graphName = AKT_GRAPH_URI + aktId;

		GraphManager graphManager = client.newGraphManager();
		graphManager.setDefaultMimetype(RDFMimeTypes.NTRIPLES);

		DOMHandle domHandle = new DOMHandle();

		graphManager.read(graphName, domHandle).withMimetype(RDFMimeTypes.RDFXML);

		OutputStream out = new ByteArrayOutputStream();

		DOMUtil.transform(domHandle.get(), out);
		return out.toString();

	}

	public static void saveMetadata(ConnectionProperties props, String id)
			throws TransformerException, SAXException, IOException {

		// Initialize the database client
		if (props.database.equals("")) {
			System.out.println("[INFO] Using default database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password,
					props.authType);
		} else {
			System.out.println("[INFO] Using \"" + props.database + "\" database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
		}
		
		// Create a document manager to work with XML files.
		GraphManager graphManager = client.newGraphManager();

		// Set the default media type (RDF/XML)
		graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);

		// Referencing XML file with RDF data in attributes
		String xmlFilePath = "./gen/output.xml";

		String rdfFilePath = "gen/rdf/akt1.rdf";

		// Automatic extraction of RDF triples from XML file
		MetadataExtractor metadataExtractor = new MetadataExtractor();

		metadataExtractor.extractMetadata(new FileInputStream(new File(xmlFilePath)),
				new FileOutputStream(new File(rdfFilePath)));

		// A handle to hold the RDF content.
		FileHandle rdfFileHandle = new FileHandle(new File(rdfFilePath)).withMimetype(RDFMimeTypes.RDFXML);

		// Writing the named graph
		System.out.println("[INFO] Tripleti su uspesno dodati u bazu. Id trupleta: " + AKT_GRAPH_URI + id + ".");
		graphManager.write(AKT_GRAPH_URI + id, rdfFileHandle);
	}
	
	public static Akt save(ConnectionProperties properties, Document akt, int id)
			throws IOException, SAXException, ParserConfigurationException, TransformerException, JAXBException {

		try {
			DatabaseClientFactory.getHandleRegistry().register(JAXBHandle.newFactory(Akt.class));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		properties = Util.loadProperties();
		client = DatabaseClientFactory.newClient(properties.host, properties.port, properties.database, properties.user,
				properties.password, properties.authType);
		xmlManager = client.newXMLDocumentManager();
		
		// Define a URI value for a document.
		String collId = "akti";
		String docId = "akti/SkupstinaGradaNovogSada/2016/" + id;

		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/output.xml"));
		Source input = new DOMSource(akt);

		transformer.transform(input, output);

		InputStreamHandle handle = new InputStreamHandle(new FileInputStream("gen/output.xml"));
		xmlManager.write(docId, metadata, handle);

		System.out.println("[INFO] Akt je uspesno dodat u bazu. Id akta: " + docId);

//		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
//		JAXBContext context = JAXBContext.newInstance("com.kmj.model.akt");
//
//		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
//		// model
//		Unmarshaller unmarshaller = context.createUnmarshaller();
//
//		// Unmarshalling generiše objektni model na osnovu XML fajla
//		Akt aktObj = (Akt) unmarshaller.unmarshal(new File("./gen/output.xml"));

		return null;
	}

	public static void main(String[] args) throws IOException, TransformerException, SAXException, ParserConfigurationException, JAXBException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentFactory.setNamespaceAware(true);
		documentFactory.setIgnoringComments(true);
		documentFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = documentFactory.newDocumentBuilder();
		Document document = builder.parse(new File("./data/akt1.xml"));
		save(Util.loadProperties(),document, 12);
		//saveMetadata(Util.loadProperties(), "ana");
		// getMetaData(Util.loadProperties(), ".ana");
	}

}
