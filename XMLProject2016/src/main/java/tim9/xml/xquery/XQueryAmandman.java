package tim9.xml.xquery;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

import tim9.xml.model.amandman.Amandman;
import tim9.xml.util.Util.ConnectionProperties;

public class XQueryAmandman {

	private static final String COLLECTION = "amandmani";
	static XMLDocumentManager xmlManager;
	private static DatabaseClient client;

	public static ArrayList<String> amandmaniAkta(ConnectionProperties props, String idAkta)
			throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {

		ArrayList<String> retVal = new ArrayList<>();

		// Initialize the database client
		if (props.database.equals("")) {
			System.out.println("[INFO] Using default database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password,
					props.authType);
			xmlManager = client.newXMLDocumentManager();
		} else {
			System.out.println("[INFO] Using \"" + props.database + "\" database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
			xmlManager = client.newXMLDocumentManager();
		}

		// Initialize query manager
		QueryManager queryManager = client.newQueryManager();

		// Query definition is used to specify Google-style query string
		StringQueryDefinition queryDefinition = queryManager.newStringDefinition();

		// Search within a specific collection
		queryDefinition.setCollections(COLLECTION);

		// Perform search
		SearchHandle results = queryManager.search(queryDefinition, new SearchHandle());

		// Serialize search results to the standard output
		MatchDocumentSummary matches[] = results.getMatchResults();

		idAkta = "akti/" + idAkta;
		for (int i = 0; i < matches.length; i++) {

			String xml = xmlManager.readAs(matches[i].getUri(), String.class);

			// Parse the input document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));

			String urlAkta = ((Element) doc.getDocumentElement()).getAttributeNode("aktURL").getValue();
			String idAmandmana = ((Element) doc.getDocumentElement()).getAttributeNode("id").getValue();
			if (urlAkta.equals(idAkta)) {
				String docId = "amandmani/" + idAmandmana;
				retVal.add(docId);
			}
		}
		client.release();
		
		return retVal;
	}

	public static List<Amandman> searchByUserEmail(ConnectionProperties props, String korisnikURL) {

		List<Amandman> retVal = new ArrayList<Amandman>();

		// Initialize the database client
		if (props.database.equals("")) {
			System.out.println("[INFO] Using default database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password,
					props.authType);
			xmlManager = client.newXMLDocumentManager();
		} else {
			System.out.println("[INFO] Using \"" + props.database + "\" database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
			xmlManager = client.newXMLDocumentManager();
		}

		// Initialize query manager
		QueryManager queryManager = client.newQueryManager();

		// Query definition is used to specify Google-style query string

		StringQueryDefinition queryDefinition = queryManager.newStringDefinition();

		// Search within a specific collection
		queryDefinition.setCollections(COLLECTION);

		// Perform search
		SearchHandle results = queryManager.search(queryDefinition, new SearchHandle());

		// Serialize search results to the standard output
		MatchDocumentSummary matches[] = results.getMatchResults();

		for (int i = 0; i < matches.length; i++) {
			Amandman a = xmlManager.readAs(matches[i].getUri(), Amandman.class);
			if (a.getKorisnikURL().equals(korisnikURL)) {
				retVal.add(a);
			}

		}

		client.release();

		return retVal;
	}
}
