package tim9.xml.services;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.JAXBHandle;

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

}
