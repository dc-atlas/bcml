package com.miravtech.sbgnpd.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.SBGNGlyphType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDl1;
import com.miravtech.sbgn.StimulationArcType;

public class XMLTest {

	@BeforeClass
	public void setup() {

	}

	@Test
	public void TestXMLCreate() {

	}

	private static Logger log = Logger.getLogger(XMLTest.class);

	class NodeIDCheck {
		int autoID = 0;
		Set<String> names = new HashSet<String>();
		public void check(SBGNGlyphType g) throws Exception {
			if (g.getID() == null) {
				g.setID("AutomaticID##" + autoID++);
			}
			if (names.contains(g.getID())) {
				throw new Exception("Error: " + g.getID()
						+ " is duplicated in the XML file");
			}
			names.add(g.getID());
		
			if (g instanceof SBGNNodeType) {
				SBGNNodeType n = (SBGNNodeType)g;
				for (SBGNNodeType in : n.getInnerNodes()) check(in);
				for (ArcType a : n.getArcs()) check(a);
			}

		}
	}
	
	
	
	@Test
	public void TestXMLLoad() throws ParserConfigurationException,
			SAXException, IOException, JAXBException, Exception {

		JAXBContext jaxbContext = JAXBContext
				.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		InputStream f = XMLTest.class.getResourceAsStream("/dectin1.xml");
		// InputStream f = XMLTest.class.getResourceAsStream("/sampleSBGN.xml");
		SBGNPDl1 root = (SBGNPDl1) unmarshaller.unmarshal(f);
		log.debug("Elements in the root: " + root.getGlyphs().size());

		// checking uniqueness of the ids
		NodeIDCheck n = new NodeIDCheck();
		for (SBGNGlyphType g : root.getGlyphs()) {
			n.check(g);
		}

		// solving clones
		Map<String, SBGNGlyphType> ids = new HashMap<String, SBGNGlyphType>();
		Set<SBGNGlyphType> clones = new HashSet<SBGNGlyphType>();
		for (SBGNGlyphType c : root.getGlyphs()) {
			if (c instanceof SBGNNodeType) {
				SBGNNodeType node = (SBGNNodeType) c;
				if (node.getCloneref() == null) {

				}
			}
		}

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(root, new File("target/test.xml"));

	}

}
