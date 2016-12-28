package tim9.xml.contoller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tim9.xml.xquery.Util;
import tim9.xml.xquery.XMLReader;
import tim9.xml.xquery.XMLWriter;

@Controller
@RequestMapping(value="xml/akt")
public class AktController {
	
	@RequestMapping(method = RequestMethod.POST)
	public void saveAkt() throws FileNotFoundException, IOException{
		XMLWriter.run(Util.loadProperties(), ".asa", "./data/akt1.xml");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public void getAkt() throws FileNotFoundException, IOException, JAXBException{
		XMLReader.run(Util.loadProperties(), ".asa");
	}

}
