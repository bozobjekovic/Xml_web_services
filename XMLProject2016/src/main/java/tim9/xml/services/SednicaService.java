package tim9.xml.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.eval.ServerEvaluationCall;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

import tim9.xml.model.sednica.Sednica;
import tim9.xml.util.Util;

@Service
public class SednicaService {

	DatabaseClient client;
	XMLDocumentManager xmlManager;
	Util.ConnectionProperties properties;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");

	public SednicaService() throws IOException {
		try {
			DatabaseClientFactory.getHandleRegistry().register(JAXBHandle.newFactory(Sednica.class));
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

	public void save(Document sednica, String id) throws JAXBException, TransformerFactoryConfigurationError,
			TransformerException, SAXException, IOException {
		String collId = "sednice";
		String docId = "sednice/" + id;

		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/outputSednica.xml"));
		Source input = new DOMSource(sednica);

		transformer.transform(input, output);

		InputStreamHandle handle = new InputStreamHandle(new FileInputStream("gen/outputSednica.xml"));
		xmlManager.writeAs(docId, metadata, handle);

		System.out.println("[INFO] Sednica je uspesno upisana u bazu kao dokument " + docId);
	}

	public List<Sednica> vratiSveSednice() {
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition stringQry = queryMgr.newStringDefinition();
		stringQry.setCollections("sednice");

		List<Sednica> sednice = new ArrayList<>();
		SearchHandle searchHandle = queryMgr.search(stringQry, new SearchHandle());
		
		for (MatchDocumentSummary docSum : searchHandle.getMatchResults()) {
			Sednica s = xmlManager.readAs(docSum.getUri(), Sednica.class);
			sednice.add(s);
		}
		return sednice;
	}

	public Sednica nadjiSednicu(String id) {
		String docId = "sednice/" + id;
		Sednica sednica;
		try {
			sednica = xmlManager.readAs(docId, Sednica.class);
		} catch (Exception e) {
			sednica = null;
		}
		return sednica;
	}
	
	public void azurirajStatusSednice(Sednica sednica) throws IOException {
		String docId = "sednice/" + sednica.getId();
		sednica.setStatus("Zavrsena");
		
		// Initialize XQuery invoker object
		ServerEvaluationCall invoker = client.newServerEval();

		// Read the file contents into a string object
		String query = "xquery version \"1.0-ml\";"
				+ " declare namespace sednica = \"http://www.tim9.com/sednica\";" + " xdmp:node-replace(doc(\""
				+ docId + "\")//sednica:Sednica/sednica:Status," + " <sednica:Status>"
				+ sednica.getStatus() + "</sednica:Status>);";

		// Invoke the query
		invoker.xquery(query);

		// Interpret the results
		invoker.eval();
	}
}
