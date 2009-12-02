package com.miravtech.sbgnpd.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.miravtech.sbgn.SBGNPDL1;

public class XMLTest {

	@BeforeClass
	public void setup() {

	}

	@Test
	public void TestXMLCreate() {

	}
	
	private static Logger log = Logger.getLogger(XMLTest.class);


	@Test
	public void TestXMLLoad() throws ParserConfigurationException, SAXException, IOException, JAXBException {

		/*
			
		
		NamespacePrefixMapper m = new NamespacePrefixMapper () {
			@Override
			public String getPreferredPrefix(String arg0, String arg1,
					boolean arg2) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String[] getPreDeclaredNamespaceUris() {
				// TODO Auto-generated method stub
				return super.getPreDeclaredNamespaceUris();
			}
		};
		
		
		log.debug("starting test");
		
		JAXBContext jaxbContext;
		Unmarshaller unmarshaller;
		InputStream f = XMLTest.class.getResourceAsStream("/sampleSBGN.xml");

		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		db.setEntityResolver(new MyEntityResolver());
		Document d = db.parse(f);
			
		jaxbContext = JAXBContext.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");
		unmarshaller = jaxbContext.createUnmarshaller();
//		NamespacePrefixMapper m1 = (NamespacePrefixMapper)unmarshaller.getProperty("com.sun.xml.bind.namespacePrefixMapper");
//		unmarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", m);

		f.close();
		SBGNPDL1 root = (SBGNPDL1)unmarshaller.unmarshal(d);
		root.getProcessOrOmittedProcessOrUncertainProcess().size();
		*/

		JAXBContext jaxbContext = JAXBContext.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		InputStream f = XMLTest.class.getResourceAsStream("/sampleSBGN.xml");		
		SBGNPDL1 root = (SBGNPDL1)unmarshaller.unmarshal(f);
		log.debug(" " +  root.getProcessOrOmittedProcessOrUncertainProcess().size());

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(root, new File("target/test.xml"));

		
	}

}
