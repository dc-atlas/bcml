package com.miravtech.checksbgn;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.xml.sax.SAXException;

public class Main {

	static int files = 0;

	/**
	 * @param args
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws Exception {

		OptionParser parser = new OptionParser();
		parser.accepts("srcSBGN", "Name of the SBGN file to use.")
				.withRequiredArg().ofType(File.class).describedAs("file path");

		File srcDir = null;
		try {
			OptionSet opts = parser.parse(args);

			srcDir = (File) opts.valueOf("srcSBGN");
			if (srcDir.isDirectory()) {
				for (File f : srcDir.listFiles()) {
					sbgnCheck(f);
				}
			} else {
				sbgnCheck(srcDir);
			}
			System.out.println("Number of files checked: " + files);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception occured: " + e.toString()
					+ "\nPossible commands:\n");
			parser.printHelpOn(System.out);
			return;
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
		files++;
	}

}
