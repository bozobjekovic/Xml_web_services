package tim9.xml.parser;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import tim9.xml.model.akt.Akt;
import tim9.xml.model.akt.Deo;
import tim9.xml.model.akt.Glava;
import tim9.xml.model.akt.Odeljak;
import tim9.xml.model.akt.Pododeljak;

public class JAXBParser {
	
	public JAXBContext context;
	public Unmarshaller unmarshaller;
	public Marshaller marshaller;
	
	public void prepare() throws JAXBException{
		
		context = JAXBContext.newInstance("tim9.xml.model");

	}
	
	public void unmarhsal(Akt akt, String fileName) throws SAXException {
		try {
			
			unmarshaller = context.createUnmarshaller();
			
			// Validacija
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new File("./data/akt.xsd"));
			
			unmarshaller.setSchema(schema);
            unmarshaller.setEventHandler(new ValidationHandler());
			
			akt = (Akt) unmarshaller.unmarshal(new File(fileName));
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	
	public void marshal(Akt akt, JAXBContext context, String fileName) throws JAXBException {
		
		akt = (Akt) unmarshaller.unmarshal(new File(fileName));
		
		marshaller = context.createMarshaller();
		
		Deo deo = new Deo();
		deo.setId("aa");
		deo.setNaslov("deo2");
		
		deo.getGlava().add(new Glava());
		
		akt.getDeo().add(deo);

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		marshaller.marshal(akt, System.out);
		
	}

	@SuppressWarnings("unused")
	private void printAkt(Akt akt) {
		
		System.out.println("---- AKT ----");
		System.out.println("- Id akta : " + akt.getId() + "\n");
		System.out.println("- Naslov akta : " + akt.getNaslov() + "\n");
		
		System.out.println("\t---- DEO ----");
		for(Deo deo : akt.getDeo())
			printDeo(deo);
		
	}

	private void printDeo(Deo deo) {
		System.out.println("\t- Id dela : " + deo.getId() + "\n");
		System.out.println("\t- Naslov dela : " + deo.getNaslov() + "\n");
		
		System.out.println("\t\t---- GLAVA ----");
		for(Glava glava : deo.getGlava())
			printGlava(glava);
		
	}

	private void printGlava(Glava glava) {
		System.out.println("\t\t- Id glave : " + glava.getId() + "\n");
		System.out.println("\t\t- Naslov glave : " + glava.getNaslov() + "\n");
		
		System.out.println("\t\t\t---- ODELJAK ----");
		for(Odeljak odeljak : glava.getOdeljak())
			printOdaljak(odeljak);
		
	}

	private void printOdaljak(Odeljak odeljak) {
		System.out.println("\t\t\t- Id odeljka : " + odeljak.getId() + "\n");
		System.out.println("\t\t\t- Naslov odeljka : " + odeljak.getNaslov() + "\n");
		
		System.out.println("\t\t\t\t---- PODODELJAK ----");
		for(Pododeljak pododeljak : odeljak.getPododeljak())
			printPododaljak(pododeljak);
		
	}


	private void printPododaljak(Pododeljak pododeljak) {
		System.out.println("\t\t\t\t- Id pododeljka : " + pododeljak.getId() + "\n");
		System.out.println("\t\t\t\t- Naslov pododeljka : " + pododeljak.getNaslov() + "\n");
		
		System.out.println("\t\t\t\t\t---- CLAN ----");
		System.out.println("\t\t\t\t\t- Clan : " + pododeljak.getClan() + "\n");
		
	}
	
/*	public static void main(String[] args) throws JAXBException, SAXException {
		
		JAXBParser parser = new JAXBParser();
		
		parser.prepare();
		
		Akt akt = new Akt();
		
		parser.unmarhsal(akt, "./data/akt1.xml");
		
		parser.marshal(akt, parser.context, "./data/akt1.xml");
	}*/

}
