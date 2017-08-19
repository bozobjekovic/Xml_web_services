package tim9.xml.rdf;

import java.util.List;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.semantics.SPARQLQueryManager;

import tim9.xml.DTO.SearchDTO;
import tim9.xml.model.akt.Akt;
import tim9.xml.util.Util.ConnectionProperties;

public class AktSPARQL {
	
	private static DatabaseClient client;
	
	public static List<Akt> searchMetaData(ConnectionProperties props, SearchDTO searchDTO) {
		
		// Initialize the database client
		if (props.database.equals("")) { //using default DB
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password, props.authType);
		} else {
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password, props.authType);
		}
		
		SPARQLQueryManager sparqlQueryManager = client.newSPARQLQueryManager();
		
		return null;
	}

}
