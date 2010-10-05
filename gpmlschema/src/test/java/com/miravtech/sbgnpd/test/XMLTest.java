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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.genmapp.gpml._2008a.Pathway;
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

		JAXBContext jaxbContext = JAXBContext
				.newInstance("org.genmapp.gpml._2008a");   //
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		InputStream f = XMLTest.class.getResourceAsStream("/DECTIN-1.gpml");
		Pathway root = (Pathway) unmarshaller.unmarshal(f);

		System.out.println("Data nodes: " + root.getDataNode().size());

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(root, new FileOutputStream(new File(
				"target/test.xml")));

	}

}
