package tim9.xml.rdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.semantics.SPARQLMimeTypes;
import com.marklogic.client.semantics.SPARQLQueryDefinition;
import com.marklogic.client.semantics.SPARQLQueryManager;

import tim9.xml.DTO.SearchDTO;
import tim9.xml.model.akt.Akt;
import tim9.xml.services.AktService;
import tim9.xml.util.Util.ConnectionProperties;

public class AktSPARQL {

	private static DatabaseClient client;

	public static List<Akt> searchMetaData(ConnectionProperties props, SearchDTO searchDTO) {

		// Initialize the database client
		if (props.database.equals("")) { // using default DB
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password,
					props.authType);
		} else {
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
		}

		SPARQLQueryManager sparqlQueryManager = client.newSPARQLQueryManager();

		String sparqlQuery = "PREFIX xs:     <http://www.w3.org/2001/XMLSchema#>\n" + "SELECT * WHERE { ";

		// STATUS
		if (searchDTO.getStatus().trim().equals(""))
			sparqlQuery += "?akt <http://www.tim9.com/akt/rdf/predikati/status> ?status .\n";
		else
			sparqlQuery += "?akt <http://www.tim9.com/akt/rdf/predikati/status> " + "\"" + searchDTO.getStatus()
					+ "\" .\n";

		SPARQLQueryDefinition query = sparqlQueryManager.newQueryDefinition(sparqlQuery);

		// Initialize DOM results handle
		DOMHandle domResultsHandle = new DOMHandle();

		domResultsHandle = sparqlQueryManager.executeSelect(query, domResultsHandle);
		DOMUtil.transform(domResultsHandle.get(), System.out);

		// Initialize Jackson results handle
		JacksonHandle resultsHandle = new JacksonHandle();
		resultsHandle.setMimetype(SPARQLMimeTypes.SPARQL_JSON);

		query = sparqlQueryManager.newQueryDefinition(sparqlQuery);

		resultsHandle = sparqlQueryManager.executeSelect(query, resultsHandle);

		client.release();

		return handleResults(resultsHandle);

	}

	private static List<Akt> handleResults(JacksonHandle resultsHandle) {

		List<Akt> retVal = new ArrayList<Akt>();

		JsonNode tuples = resultsHandle.get().path("results").path("bindings");
		Iterator<JsonNode> nodes;

		for (JsonNode row : tuples) {
			nodes = row.iterator();

			while (nodes.hasNext()) {
				String id = nodes.next().path("value").asText();
				if (id.contains("http")) {
					id = id.replace("http://www.tim9.com/akt/", "");
					String docId = "akti/" + id;
					try {
						retVal.add(new AktService().findAktDocId(docId));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return retVal;
	}

}
