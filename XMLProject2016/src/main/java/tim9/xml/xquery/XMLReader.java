package tim9.xml.xquery;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JAXBHandle;

import tim9.xml.model.akt.Akt;
import tim9.xml.xquery.Util.ConnectionProperties;

@Component
public class XMLReader {

	private static DatabaseClient client;

	public static void run(ConnectionProperties props, String docId) throws FileNotFoundException, JAXBException {

		client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
				props.authType);

		XMLDocumentManager xmlManager = client.newXMLDocumentManager();

		// A JAXB handle to receive the document's content.
		JAXBContext context = JAXBContext.newInstance("tim9.xml.model");
		JAXBHandle<Akt> handle = new JAXBHandle<Akt>(context);

		DocumentMetadataHandle metadata = new DocumentMetadataHandle();

		xmlManager.read(docId, metadata, handle);

		// Retrieving a Bookstore instance
		Akt akt = handle.get();

		// Serializing DOM tree to standard output.
		System.out.println("[INFO] Retrieved content:");
		System.out.println(akt);

		// Release the client
		client.release();

		System.out.println("[INFO] End.");
	}

}
