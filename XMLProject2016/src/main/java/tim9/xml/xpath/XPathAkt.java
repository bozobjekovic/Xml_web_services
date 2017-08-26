package tim9.xml.xpath;

import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class XPathAkt {
	
	private static DocumentBuilderFactory documentFactory;
	private static TransformerFactory transformerFactory;
	private static XPathFactory xPathFactory;
	private static Map<String, String> namespaceMappings;
	
	private static String text;
	
	private Document document;
	
	static {

		documentFactory = DocumentBuilderFactory.newInstance();
		documentFactory.setNamespaceAware(true);
		documentFactory.setIgnoringComments(true);
		documentFactory.setIgnoringElementContentWhitespace(true);
		
		transformerFactory = TransformerFactory.newInstance();

		xPathFactory = XPathFactory.newInstance();
		
		namespaceMappings = new HashMap<String, String>();
		
	}
	
	public void evaluateXPath(String expression) {
		
		XPath xPath = xPathFactory.newXPath();
		XPathExpression xPathExpression;
		namespaceMappings.put("akt", "http://www.tim9.com/akt");
		xPath.setNamespaceContext(new NamespaceContext(namespaceMappings));
		
		try {
			
			xPathExpression = xPath.compile(expression);

			NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
			Node node;
			
			boolean done = false;
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (done) {
					break;
				}
			
				node = nodeList.item(i);
				
				if (node.getNodeType() == Node.TEXT_NODE) {
					String nodeValue = node.getNodeValue();
					
					if (nodeValue.trim().length() != 0) {
						nodeValue.replace("\n", " ");
						String[] words = nodeValue.split(" ");
						
						for (String word : words) {
							if (text.length() + word.length() > 150) {
								done = true;
								break;
							}
							text += " " + word;
						}
					}
				}
				else {
					transform(node, System.out);
				}
			}
			
		} catch (XPathExpressionException e) {
			System.out.println("[ERROR] Error evaluationg \"" + expression + "\" expression, line: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void transform(Node node, OutputStream out) {
		try {

			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(node);

			StreamResult result = new StreamResult(out);

			transformer.transform(source, result);
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public void transform(OutputStream out) {
		transform(document, out);
	}
	
	public void buildDocument(String filePath) {

		try {
			
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(filePath));
			document = builder.parse(is); 

			if (document != null)
				System.out.println("[INFO] File parsed with no errors.");
			else
				System.out.println("[WARN] Document is null.");

		} catch (SAXParseException e) {
			
			System.out.println("[ERROR] Parsing error, line: " + e.getLineNumber() + ", uri: " + e.getSystemId());
			System.out.println("[ERROR] " + e.getMessage() );
			System.out.print("[ERROR] Embedded exception: ");
			
			Exception embeddedException = e;
			if (e.getException() != null)
				embeddedException = e.getException();

			embeddedException.printStackTrace();
			
			System.exit(0);
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateText(String aktXML) {
		text = "";
		XPathAkt xpAkt = new XPathAkt();
		
		xpAkt.buildDocument(aktXML);
		
		String expression = "/akt:Akt/akt:Clan/descendant::*/text()";
		xpAkt.evaluateXPath(expression);
		
		if (text.trim().equals("")) {
			expression = "/akt:Akt/akt:Deo/descendant::*/text()";
			xpAkt.evaluateXPath(expression);
		}
		
		text += "...";
		return text;
	}
	
	/*public static void main(String[] args) {
		
		XPathAkt handler = new XPathAkt();

		String filePath = "./data/akt.xml";
		
		handler.buildDocument(filePath);

		handler.transform(System.out);
		
		String expression = "//akt:Tacka";
		
		System.out.println("*********************");
		handler.evaluateXPath(expression);
		handler.transform(System.out);
	}*/
	
}
