package tim9.xml.xquery;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.MatchLocation;
import com.marklogic.client.query.MatchSnippet;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

import tim9.xml.model.akt.Akt;
import tim9.xml.util.Util.ConnectionProperties;

public class XQueryAkt {

	private static DatabaseClient client;

	private static final String COLLECTION = "akti";

	static XMLDocumentManager xmlManager;

	public static Map<String, String> searchAktsByUserEmail(ConnectionProperties props, String email) {
		Map<String, String> retVal = new HashMap<String, String>();

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

		MatchDocumentSummary result;
		MatchLocation locations[];
		String text;

		String pronadjeno = "";
		String dokument = "";

		for (int i = 0; i < matches.length; i++) {
			result = matches[i];
			Akt a = xmlManager.readAs(matches[i].getUri(), Akt.class);
			if (a.getKorisnik().getEmail().equals(email)) {
				locations = result.getMatchLocations();

				for (MatchLocation location : locations) {
					for (MatchSnippet snippet : location.getSnippets()) {
						text = snippet.getText().trim();
						if (!text.equals("")) {
							pronadjeno += snippet.isHighlighted() ? text.toUpperCase() : text;
							pronadjeno += " ";
						}
					}
					String xQueryPath = location.getPath();
					dokument = xQueryPath.split("\\*")[0];
					dokument = dokument.replace("fn:doc(", "");
					dokument = dokument.replace("\"", "");
					dokument = dokument.replace(")", "");
					dokument = dokument.substring(0, dokument.length() - 1);

					if (retVal.containsKey(dokument)) {
						String p = retVal.get(dokument);
						p += pronadjeno;
						retVal.replace(dokument, p);
					} else {
						retVal.put(dokument, pronadjeno);
					}
				}
			}
		}

		client.release();

		return retVal;
	}

	public static Map<String, String> searchAktsByText(ConnectionProperties props, String criteria) {
		Map<String, String> retVal = new HashMap<String, String>();

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

		// Initialize query manager
		QueryManager queryManager = client.newQueryManager();

		// Query definition is used to specify Google-style query string
		StringQueryDefinition queryDefinition = queryManager.newStringDefinition();

		queryDefinition.setCriteria(criteria);

		// Search within a specific collection
		queryDefinition.setCollections(COLLECTION);

		// Perform search
		SearchHandle results = queryManager.search(queryDefinition, new SearchHandle());

		// Serialize search results to the standard output
		MatchDocumentSummary matches[] = results.getMatchResults();
		// System.out.println("[INFO] Showing the results for: " + criteria +
		// "\n");

		MatchDocumentSummary result;
		MatchLocation locations[];
		String text;

		String pronadjeno = "";
		String dokument = "";

		for (int i = 0; i < matches.length; i++) {
			result = matches[i];

			locations = result.getMatchLocations();

			for (MatchLocation location : locations) {
				for (MatchSnippet snippet : location.getSnippets()) {
					text = snippet.getText().trim();
					if (!text.equals("")) {
						pronadjeno += snippet.isHighlighted() ? text.toUpperCase() : text;
						pronadjeno += " ";
					}
				}
				String xQueryPath = location.getPath();
				dokument = xQueryPath.split("\\*")[0];
				dokument = dokument.replace("fn:doc(", "");
				dokument = dokument.replace("\"", "");
				dokument = dokument.replace(")", "");
				dokument = dokument.substring(0, dokument.length() - 1);

				if (retVal.containsKey(dokument)) {
					String p = retVal.get(dokument);
					p += pronadjeno;
					retVal.replace(dokument, p);
				} else {
					retVal.put(dokument, pronadjeno);
				}
			}
		}

		client.release();

		return retVal;
	}

	public static HashMap<String, Integer> findReferences(ConnectionProperties props, String id) throws ParserConfigurationException, SAXException, IOException {

		HashMap<String, Integer> retVal = new HashMap<>();

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

			String xml = xmlManager.readAs(matches[i].getUri(), String.class);

			// Parse the input document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));

			String referenca = ((Element) doc.getDocumentElement()).getAttributeNode("id").getValue();
			
			if (!referenca.equals(id)) {
				ArrayList<String> foundRefs = new ArrayList<>();
				findReferencesRecursive(doc.getDocumentElement(), "referenca", id, foundRefs);
				if (foundRefs.size() != 0) {
					retVal.put("akti/" + referenca, foundRefs.size());
				}
			}
		}

		client.release();

		return retVal;
	}

	private static void findReferencesRecursive(Node documentElement, String name, String id,
			ArrayList<String> foundRefs) {
		
		if (documentElement.getNodeType() != Node.ELEMENT_NODE)
			return;

		if (!documentElement.hasChildNodes())
			return;

		NodeList children = documentElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);

			if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().toLowerCase().contains(name))
			{
				Element reference = (Element) child;
				if (reference.getAttributeNode("URL").getValue().contains(id)) {
					foundRefs.add(child.getNodeValue());
				}
			}

			findReferencesRecursive(child, name, id, foundRefs);
		}
		
	}
}
