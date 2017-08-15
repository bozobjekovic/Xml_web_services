package tim9.xml.transformation;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

import net.sf.saxon.TransformerFactoryImpl;

/**
 * 
 * Primer demonstrira koriscenje Apache FOP programskog API-a za 
 * renderovanje PDF-a primenom XSL-FO transformacije na XML dokumentu.
 *
 */
public class TransformationAkt {
	
	private FopFactory fopFactory;
	
	private TransformerFactory transformerFactory;
	
	public static final String XSL_FILE = "data/xslt/akt_xsl.xsl";
	//public static final String XML_FILE = "data/akt.xml";
	public static final String FO_FILE = "data/xslt/akt_fo.xsl";
	
	public static final String INPUT_FILE = "data/tt/bookstore.xml";
	//public static final String XSL_FILE = "data/tt/bookstore.xsl";
	
	public TransformationAkt() throws SAXException, IOException {
		
		// Initialize FOP factory object
		fopFactory = FopFactory.newInstance(new File("data/fop.xconf"));
		
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
	
	private void generatePDF(String inputXml) throws Exception {

		// Point to the XSL-FO file
		File xslFile = new File(FO_FILE);
		
		XMLStreamReader xmlSR = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(inputXml));

		// Create transformation source
		StreamSource transformSource = new StreamSource(xslFile);
		
		// Initialize the transformation subject
		//StreamSource source = new StreamSource(new File(INPUT_FILE));
		StAXSource source = new StAXSource(xmlSR);

		// Initialize user agent needed for the transformation
		FOUserAgent userAgent = fopFactory.newFOUserAgent();
		
		// Create the output stream to store the results
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		// Initialize the XSL-FO transformer object
		Transformer xslFoTransformer = transformerFactory.newTransformer(transformSource);
		
		// Construct FOP instance with desired output format
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outStream);

		// Resulting SAX events 
		Result res = new SAXResult(fop.getDefaultHandler());

		// Start XSLT transformation and FOP processing
		xslFoTransformer.transform(source, res);

		// Generate PDF file
		File pdfFile = new File("data/pdf/akt.pdf");
		if (!pdfFile.getParentFile().exists()) {
			System.out.println("[INFO] A new directory is created: " + pdfFile.getParentFile().getAbsolutePath() + ".");
			pdfFile.getParentFile().mkdir();
		}
		
		OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
		out.write(outStream.toByteArray());

		System.out.println("[INFO] File \"" + pdfFile.getCanonicalPath() + "\" generated successfully.");
		out.close();
		
		System.out.println("[INFO] End.");

	}
	
	public static void main(String[] args) throws Exception {
    	
		TransformationAkt aktTransformer = new TransformationAkt();
		
		System.out.println(aktTransformer.generateHTML(INPUT_FILE));
		aktTransformer.generatePDF("");
		
		System.out.println("[INFO] End.");
    }

}
