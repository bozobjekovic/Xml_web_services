package tim9.xml.xquery;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.stereotype.Component;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.InputStreamHandle;

import tim9.xml.xquery.Util.ConnectionProperties;

@Component
public class XMLWriter {
	
	private static DatabaseClient client;
	
	public static void run(ConnectionProperties props, String docId, String fileName) throws FileNotFoundException {

		client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password, props.authType);
		
		XMLDocumentManager xmlManager = client.newXMLDocumentManager();
		
		InputStreamHandle handle = new InputStreamHandle(new FileInputStream(fileName));
		
		xmlManager.write(docId, handle);
		
		client.release();
		
		System.out.println("[INFO] End.");
	}

}
