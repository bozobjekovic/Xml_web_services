package tim9.xml.services;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JAXBHandle;

import tim9.xml.model.korisnik.Korisnik;
import tim9.xml.util.Util;

@Service
public class UserService {
	
	DatabaseClient client;
	XMLDocumentManager xmlManager;

	Util.ConnectionProperties properties;
	
	public UserService() throws IOException {
		try {
			DatabaseClientFactory.getHandleRegistry().register(JAXBHandle.newFactory(Korisnik.class));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		properties = Util.loadProperties();
		client = DatabaseClientFactory.newClient(properties.host, properties.port, properties.database, properties.user,
				properties.password, properties.authType);
		xmlManager = client.newXMLDocumentManager();
	}
	
	public void release(){
        client.release();
    }
	
	public Korisnik findByEmail(String email) {
		String docId = "users/" + email + ".xml";
		Korisnik korisnik;
		
		try {
			korisnik = xmlManager.readAs(docId, Korisnik.class);
		} catch (Exception e) {
			korisnik = null;
		}
		
		return korisnik;
	}
	
	public Korisnik save(Korisnik korisnik) {
		String collId = "users";
		String docId = "users/" + korisnik.getEmail() + ".xml";
		
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);
		
		xmlManager.writeAs(docId, metadata, korisnik);
		
		return korisnik;
	}

}
