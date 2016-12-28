package tim9.xml.xquery;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.marklogic.client.DatabaseClientFactory.Authentication;

public class Util {
	
	static public class ConnectionProperties {

		public String host;
		public int port = -1;
		public String user;
		public String password;
		public String database;
		public String proxyHost;
		public String proxyPort;
		public boolean usingProxy;
		public Authentication authType;

		public ConnectionProperties(Properties props) {
			super();
			host = props.getProperty("conn.host").trim();
			port = Integer.parseInt(props.getProperty("conn.port"));
			user = props.getProperty("conn.user").trim();
			password = props.getProperty("conn.password").trim();
			database = props.getProperty("conn.database").trim();
			proxyHost = props.getProperty("conn.proxy_host").trim();
			proxyPort = props.getProperty("conn.proxy_port").trim();
			usingProxy = Boolean.parseBoolean(props.getProperty("conn.using_proxy").trim());
			authType = Authentication.valueOf(props.getProperty("conn.authentication_type").toUpperCase().trim());
			
			// Proxy settings
			if (usingProxy) {
				setProxy(proxyHost, proxyPort);
			}
		}
	}

	/**
	 * Read the configuration properties for the example.
	 * 
	 * @return the configuration object
	 */
	public static ConnectionProperties loadProperties() throws IOException {
		String propsName = "connection.properties";

		InputStream propsStream = openStream(propsName);
		if (propsStream == null)
			throw new IOException("Could not read properties " + propsName);

		Properties props = new Properties();
		props.load(propsStream);

		return new ConnectionProperties(props);
	}

	/**
	 * Read a resource for an example.
	 * 
	 * @param fileName
	 *            the name of the resource
	 * @return an input stream for the resource
	 * @throws IOException
	 */
	public static InputStream openStream(String fileName) throws IOException {
		return Util.class.getClassLoader().getResourceAsStream(fileName);
	}
	
	/**
	 * Configures proxy settings within the JVM. 
	 * 
	 * @param props connection configuration object
	 */
	public static void setProxy(String proxyHost, String proxyPort) {
		
		System.out.println("[INFO] Using proxy: " + proxyHost + ":" + proxyPort + ".");
		
		System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);
        
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        
	}

}
