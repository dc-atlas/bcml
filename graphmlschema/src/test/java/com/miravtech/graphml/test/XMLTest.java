package com.miravtech.graphml.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;


public class XMLTest {

	@BeforeClass
	public void setup() {

	}



	@Test
	public void TestXMLLoad() throws ParserConfigurationException,
			SAXException, IOException, JAXBException, Exception {

	/*	
		JAXBContext jaxbContext = JAXBContext
				.newInstance("com.yworks.xml.graphml:org.graphdrawing.graphml.xmlns.graphml");   //
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		InputStream f = XMLTest.class.getResourceAsStream("/DECTIN-1.gpml");
	*/
//		Pathway root = (Pathway) unmarshaller.unmarshal(f);

//		System.out.println("Data nodes: " + root.getDataNode().size());

//		Marshaller marshaller = jaxbContext.createMarshaller();
		
		
	//	marshaller.marshal(root, new FileOutputStream(new File(
	//			"target/test.xml")));

	}

}
