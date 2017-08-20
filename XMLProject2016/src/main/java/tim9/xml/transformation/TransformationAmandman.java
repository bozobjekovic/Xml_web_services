package tim9.xml.transformation;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import net.sf.saxon.TransformerFactoryImpl;

public class TransformationAmandman {

	private TransformerFactory transformerFactory;
	
	public static final String XSL_FILE = "data/xslt/amandman_xsl.xsl";
	//public static final String XML_FILE = "data/amandman.xml";
	
	public TransformationAmandman() throws SAXException, IOException {
		
		// Setup the XSLT transformer factory
		transformerFactory = new TransformerFactoryImpl();
	}
	
	public String generateHTML(String inputXml) throws TransformerException {
    	
		// Initialize Transformer instance
		StreamSource transformSource = new StreamSource(XSL_FILE);
		Transformer transformer = transformerFactory.newTransformer(transformSource);
		
		StreamSource xml = new StreamSource(new StringReader(inputXml));
		
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		
		transformer.transform(xml, result);
		
		return writer.toString();
    }
}
