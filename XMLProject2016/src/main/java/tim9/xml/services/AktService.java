package tim9.xml.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

import tim9.xml.model.akt.Akt;
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
	
	public Akt save(Document akt, int id) throws TransformerFactoryConfigurationError, TransformerException, FileNotFoundException, JAXBException {
		
		String collId = "akti";
		String docId = "akti/" + id;
		
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/output.xml"));
		Source input = new DOMSource(akt);
		
		transformer.transform(input, output);

		InputStreamHandle handle = new InputStreamHandle(new FileInputStream("gen/output.xml"));
		xmlManager.write(docId, metadata, handle);
		
		// TODO: SAČUVATI METAPODATKE
		
		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance("tim9.xml.model.akt");

		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
		// model
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// Unmarshalling generiše objektni model na osnovu XML fajla
		Akt newAkt = (Akt) unmarshaller.unmarshal(new File("./gen/output.xml"));
		
		return newAkt;
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

}
