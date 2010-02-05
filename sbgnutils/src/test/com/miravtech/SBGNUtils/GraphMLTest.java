package com.miravtech.checksbgn;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.graphdrawing.graphml.xmlns.graphml.Graphml;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.miravtech.sbgn.SBGNPDl1;

public class GraphMLTest {
	@BeforeClass
	public void setup() {

	}

	@Test
	public void TestGraph() throws Exception {

		JAXBContext jaxbContext = JAXBContext
				.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics"); //

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		InputStream f = GraphMLTest.class.getResourceAsStream("/dectin1.xml");
		SBGNPDl1 root = (SBGNPDl1) unmarshaller.unmarshal(f);

		SBGNUtils.setIDs(root);
		Graphml out = SBGNUtils.asGraphML(root);

		JAXBContext jaxbContext2 = JAXBContext
				.newInstance("com.yworks.xml.graphml:org.graphdrawing.graphml.xmlns.graphml"); //

		Marshaller marshaller = jaxbContext2.createMarshaller();
		/*
		 * XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance()
		 * .createXMLStreamWriter(new FileWriter(new File("out.graphml")));
		 * xmlStreamWriter
		 * .setDefaultNamespace("http://graphml.graphdrawing.org/xmlns/graphml"
		 * ); xmlStreamWriter.setPrefix("y",
		 * "http://www.yworks.com/xml/graphml");
		 */
		// TODO - failing to add in the XML file:

		// xmlns="http://graphml.graphdrawing.org/xmlns"
		// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		// xmlns:y="http://www.yworks.com/xml/graphml"
		// xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
		// http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd

		// TODO add @XmlRootElement(name="graphml") for graphml
		// TODO add @XmlRootElement(name="graphml") for graphml

		// xmlStreamWriter.writeDefaultNamespace("http://graphml.graphdrawing.org/xmlns/graphml");
		// xmlStreamWriter.writeNamespace("y",
		// "http://www.yworks.com/xml/graphml");

		// marshaller.marshal(out, xmlStreamWriter);
		// marshaller.marshal(new JAXBElement<GraphmlType>(new QName(
		// "http://www.yworks.com/xml/graphml"), GraphmlType.class, out),
		// new File("out.graphml"));

		marshaller.marshal(out, new File("dectin1.graphml"));

		f = GraphMLTest.class.getResourceAsStream("/TLR3.xml");
		root = (SBGNPDl1) unmarshaller.unmarshal(f);

		SBGNUtils.setIDs(root);
		out = SBGNUtils.asGraphML(root);
		marshaller.marshal(out, new File("TLR3.graphml"));

	}
}
