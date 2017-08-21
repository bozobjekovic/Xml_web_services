package tim9.xml.xquery;

import java.util.HashMap;
import java.util.Map;

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
		
		return retVal;
	}

}
