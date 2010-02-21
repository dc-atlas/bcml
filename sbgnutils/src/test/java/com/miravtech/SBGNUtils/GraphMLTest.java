package com.miravtech.SBGNUtils;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.graphdrawing.graphml.xmlns.graphml.Graphml;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.miravtech.sbgn.SBGNPDL1Type;
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

		// InputStream f =
		// GraphMLTest.class.getResourceAsStream("/dectin1.xml");
		File f = new File("c:\\temp\\filter.xml");
		SBGNPDl1 r = ((SBGNPDl1) unmarshaller.unmarshal(f));
		SBGNPDL1Type root = r.getValue();

		SBGNUtils sbgn = new SBGNUtils(root);
		sbgn.fillRedundantData();

		Marshaller marshaller1 = jaxbContext.createMarshaller();
		// marshaller1.marshal(r, new File("c:\\temp\\dectin_exp_1.xml"));

		Graphml out = sbgn.asGraphML();

		JAXBContext jaxbContext2 = JAXBContext
				.newInstance("com.yworks.xml.graphml:org.graphdrawing.graphml.xmlns.graphml"); //

		Marshaller marshaller = jaxbContext2.createMarshaller();

		marshaller.marshal(out, new File("l:\\temp\\dectin1.graphml"));

	}
}
