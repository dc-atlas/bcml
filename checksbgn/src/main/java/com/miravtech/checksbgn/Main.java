package com.miravtech.checksbgn;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

public class Main {

	/**
	 * @param args
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws Exception {

		String source;
		if (args.length >= 1) {
			source = args[0];
		} else {
			throw new Exception(
					"Please provide the directory or the file to check!");
		}
		File srcDir = new File(source);

		if (srcDir.isDirectory()) {
			for (File f : srcDir.listFiles()) {
				sbgnCheck(f);
			}
		} else {
			sbgnCheck(srcDir);
		}
	}

	public static void sbgnCheck(File srcSBGN) throws JAXBException,
			SAXException {
		Checker c = new Checker();
		List<CheckReport> r = c.check(srcSBGN);
		if (r.size() != 0) {
			System.out.println("File: " + srcSBGN.getAbsolutePath());
			for (CheckReport cr : r) {
				System.out.println(cr);

			}
		}

	}

}
