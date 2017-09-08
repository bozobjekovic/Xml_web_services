package tim9.xml.xquery;

import java.io.BufferedReader;
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
import com.marklogic.client.eval.ServerEvaluationCall;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

import tim9.xml.model.amandman.Amandman;
import tim9.xml.util.PrimeniAmandman;
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
	
	public static void primeniAmandman(ConnectionProperties props, PrimeniAmandman pAmandman) {
		
		// Initialize the database client
		if (props.database.equals("")) {
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password,
					props.authType);
		} else {
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
		}
		
		// Initialize XQuery invoker object
		ServerEvaluationCall invoker = client.newServerEval();
		
		String query = "";
		if (pAmandman.getPredlozenoResenje().equalsIgnoreCase("Izmena")) {
			pAmandman.setPatch(fomatPatch(pAmandman.getPatch()));
			query = "xquery version \"1.0-ml\"; declare namespace akt = \"http://www.tim9.com/akt\";"
					+ " xdmp:node-replace(doc(\"" + pAmandman.getDocIDAkt() + "\")//*[@id=\"" + pAmandman.getOdredba() + "\"]," + pAmandman.getPatch() + ");";
		} else if (pAmandman.getPredlozenoResenje().equalsIgnoreCase("Dodavanje")) {
			pAmandman.setPatch(fomatPatch(pAmandman.getPatch()));
			query = "xquery version \"1.0-ml\"; declare namespace akt = \"http://www.tim9.com/akt\";"
					+ " xdmp:node-insert-after(doc(\"" + pAmandman.getDocIDAkt() + "\")//*[@id=\"" + pAmandman.getOdredba() + "\"]," + pAmandman.getPatch() + ");";
		} else if (pAmandman.getPredlozenoResenje().equalsIgnoreCase("Brisanje")) {
			query = "xquery version \"1.0-ml\"; declare namespace akt = \"http://www.tim9.com/akt\";"
					+ " xdmp:node-delete(doc(\"" + pAmandman.getDocIDAkt() + "\")//*[@id=\"" + pAmandman.getOdredba() + "\"]);";
		}
		
		System.out.println(pAmandman.getPatch());
		
		// Invoke the query
		invoker.xquery(query);
		
		// Interpret the results
		invoker.eval();
		
		System.out.println("GOTOVO");
		
	}
	
	private static String fomatPatch(String patch) {
		
		patch = patch.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		patch = patch.replace("xmlns:akt=\"http://www.tim9.com/akt\"", "");
		patch = patch.replace("xmlns:pred=\"http://www.tim9.com/akt/rdf/predikati/\"", "");
		patch = patch.replace("xmlns:amd=\"http://www.tim9.com/amandman\"", "");
		patch = patch.replace("xmlns:xs=\"http://www.w3.org/2001/XMLSchema#\"", "");
		patch = patch.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
		
		patch = patch.replaceAll("(?m)^[ \t]*\r?\n", "");
		
		BufferedReader bufReader = new BufferedReader(new StringReader(patch));
		patch = "";
		try {
			String line = "";
			while ((line = bufReader.readLine()) != null) {

				if (!line.contains("id") && !line.contains(">"))
					line = line + " id=\"-1\" ";

				if (!line.contains("redniBroj") && !line.contains(">") && trebaRedniBroj(line))
					line = line + " redniBroj=\"-1\" ";

				if (!line.contains(">"))
					line = line + ">";

				line = line.replace(" >", ">");

				patch += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return patch;
	}
	
	private static boolean trebaRedniBroj(String line) {

		if (line.toLowerCase().contains("akt:deo"))
			return true;

		if (line.toLowerCase().contains("akt:glava"))
			return true;

		if (line.toLowerCase().contains("akt:odeljak"))
			return true;

		if (line.toLowerCase().contains("akt:pododeljak"))
			return true;

		if (line.toLowerCase().contains("akt:clan"))
			return true;

		return false;
		
	}
}
