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

package com.miravtech.SBGNUtils;

import java.io.File;

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

		SBGNPDl1 r = ((SBGNPDl1) unmarshaller.unmarshal(GraphMLTest.class.getResource("/filter.xml")));
		SBGNPDL1Type root = r.getValue();

		SBGNUtils sbgn = new SBGNUtils(root);
		sbgn.fillRedundantData();

		Graphml out = sbgn.asGraphML();

		JAXBContext jaxbContext2 = JAXBContext
				.newInstance("com.yworks.xml.graphml:org.graphdrawing.graphml.xmlns.graphml"); //

		Marshaller marshaller = jaxbContext2.createMarshaller();

		marshaller.marshal(out, new File("target/filtered.graphml"));

	}
}
