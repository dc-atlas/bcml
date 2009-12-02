package com.miravtech.sbgnpd.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.miravtech.sbgn.ObjectFactory;

public class MyEntityResolver implements EntityResolver {

	private static Logger log = Logger.getLogger(MyEntityResolver.class);
	

	public final String FILE1 = "http://www.miravtech.com/DoD_Intrusion_Detection_System";
	public final String RES1 = "/IDS.dtd";

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		log.debug("Resolving entity: " + publicId + " systemID: "+ systemId);
		if (systemId.equalsIgnoreCase(FILE1)) {
			InputStream is = ObjectFactory.class.getResourceAsStream(RES1);
			return new InputSource(is);
		}
		return null;
	}
	

}
