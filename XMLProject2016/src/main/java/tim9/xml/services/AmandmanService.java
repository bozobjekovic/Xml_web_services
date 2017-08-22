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

import tim9.xml.model.amandman.Amandman;
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
	
	public Amandman save(Document amandman, int id) throws TransformerFactoryConfigurationError, TransformerException, FileNotFoundException, JAXBException {
		
		String collId = "amandmani";
		String docId = "amandmani/" + id;
		
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("gen/outputAmandman.xml"));
		Source input = new DOMSource(amandman);
		
		transformer.transform(input, output);

		InputStreamHandle handle = new InputStreamHandle(new FileInputStream("gen/outputAmandman.xml"));
		xmlManager.write(docId, metadata, handle);
		
		// TODO: SAČUVATI METAPODATKE
		
		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance("tim9.xml.model.amandman");

		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
		// model
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// Unmarshalling generiše objektni model na osnovu XML fajla
		Amandman newAmandman = (Amandman) unmarshaller.unmarshal(new File("./gen/outputAmandman.xml"));
		
		return newAmandman;
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

	public void delete(String id) {
		try {
			xmlManager.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
