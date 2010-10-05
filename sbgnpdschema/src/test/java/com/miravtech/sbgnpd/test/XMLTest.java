/**
 *
 * Copyright (C) 2010 Razvan Popovici <rp@miravtech.com>
 * Copyright (C) 2010 Luca Beltrame <luca.beltrame@unifi.it>
 * Copyright (C) 2010 Enrica Calura <enrica.calura@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.miravtech.sbgnpd.test;

import java.io.File;
import java.io.FileOutputStream;
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
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.SBGNGlyphType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDL1Type;
import com.miravtech.sbgn.SBGNPDl1;

abstract class RecursiveSBGNIterator {

	public void run(SBGNGlyphType g) {
		if (g instanceof SBGNNodeType) {
			SBGNNodeType n = (SBGNNodeType) g;
			for (SBGNNodeType in : n.getInnerNodes()) {
				interateNode(in);
				interateGlyph(in);
				run(in);
			}
			for (ArcType a : n.getArcs()) {
				interateArc(a);
				interateGlyph(a);
				run(a);
			}
		}
	}

	public void interateGlyph(SBGNGlyphType n) {
	}

	public void interateNode(SBGNNodeType n) {
	}

	public void interateArc(ArcType n) {
	}

}

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
				SBGNNodeType n = (SBGNNodeType) g;
				for (SBGNNodeType in : n.getInnerNodes())
					check(in);
				for (ArcType a : n.getArcs())
					check(a);
			}

		}
	}

	@Test
	public void TestXMLLoad() throws ParserConfigurationException,
			SAXException, IOException, JAXBException, Exception {

		JAXBContext jaxbContext = JAXBContext
				.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");   //
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		spf.setValidating(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		InputStream f = XMLTest.class.getResourceAsStream("/dectin1.xml");
		SAXSource source = new SAXSource(xmlReader, new InputSource(f));

		// InputStream f = XMLTest.class.getResourceAsStream("/sampleSBGN.xml");
		SBGNPDl1 root1 = (SBGNPDl1) unmarshaller.unmarshal(source);
		SBGNPDL1Type root = root1.getValue();
		log.debug("Elements in the root: " + root.getGlyphs().size());
		SBGNNodeType n1 = root.getGlyphs().get(0);

		//		log.debug("Location of the second glyph: line: "
//				+ n1.sourceLocation().getLineNumber() + " column "
//				+ n1.sourceLocation().getColumnNumber());

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
		marshaller.marshal(root1, new FileOutputStream(new File(
				"target/test.xml")));

	}

}
