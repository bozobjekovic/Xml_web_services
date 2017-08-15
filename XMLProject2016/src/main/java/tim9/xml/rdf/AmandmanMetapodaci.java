package tim9.xml.rdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;

import tim9.xml.util.Util.ConnectionProperties;

public class AmandmanMetapodaci {
	private static DatabaseClient client;

	public static String getMetaData(ConnectionProperties props, String id) throws IOException {

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

		String graphName = "amandmani/metadata/" + id;  //PROVERITI

		GraphManager graphManager = client.newGraphManager();
		graphManager.setDefaultMimetype(RDFMimeTypes.NTRIPLES);

		DOMHandle domHandle = new DOMHandle();

		graphManager.read(graphName, domHandle).withMimetype(RDFMimeTypes.RDFXML);

		OutputStream out = new ByteArrayOutputStream();

		DOMUtil.transform(domHandle.get(), out);
		return out.toString();

	}
}
