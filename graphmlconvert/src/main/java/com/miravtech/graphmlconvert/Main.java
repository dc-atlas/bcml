package com.miravtech.graphmlconvert;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.graphdrawing.graphml.xmlns.graphml.Graphml;

import com.miravtech.SBGNUtils.SBGNUtils;
import com.miravtech.sbgn.SBGNPDL1Type;
import com.miravtech.sbgn.SBGNPDl1;

public class Main {

	/**
	 * @param args
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws JAXBException, IOException {

		OptionParser parser = new OptionParser();
		parser.accepts("srcSBGN", "Name of the SBGN file to transform to GraphML.")
				.withRequiredArg().ofType(File.class).describedAs("file path");
		parser.accepts("targetGraphML", "Name of the target GraphML file to output.")
		.withRequiredArg().ofType(File.class).describedAs("file path");
		
//		String source, destination;
//		if (args.length >= 2) {
//			source = args[0];
//			destination = args[1];
//		} else {
//			throw new IllegalArgumentException(
//					"Must provide source and destination directory");
//		}

		OptionSet opts = parser.parse(args);
		
		File srcDir = (File) opts.valueOf("srcSBGN");
		File destDir = (File) opts.valueOf("targetGraphML");
		if (srcDir == null || destDir == null) {
			parser.printHelpOn(System.out);
			throw new RuntimeException("both srcSBGN and targetGraphML arguments are mandatory");			
		}
		
		//File srcDir = new File(source);
		//File destDir = new File(destination);
		int convert = 0;
		if (srcDir.isDirectory()) {
		for (File f : srcDir.listFiles(new XMLFiles())) {
			File destGraphML = new File(destDir, XMLFiles.getName(f.getName())
					+ ".graphml");
			graphmlconvert(f, destGraphML);
			convert++;
		}
		} else {
			graphmlconvert(srcDir, destDir);
			convert++;
		}
		System.out.println("Converted: " + convert + " files.");
	}

	private static JAXBContext jaxbContext;
	private static JAXBContext jaxbContext2;
	private static Unmarshaller unmarshaller;
	private static Marshaller marshaller;
	/**
	 * 
	 * Converts the gpml to SBGN files.
	 * 
	 * @param sourceGPML
	 *            the source file (must exist)
	 * @param destSBGN
	 *            the destination file (will be overwritten)
	 * @throws JAXBException
	 *             if any issue occurs with IO or with XML parsing
	 */
	public static void graphmlconvert(File sourceSBGN, File destGraphML)
			throws JAXBException {

		if (jaxbContext == null) {
			jaxbContext = JAXBContext
					.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics"); //
			unmarshaller = jaxbContext.createUnmarshaller();
		}


		SBGNPDL1Type root = ((SBGNPDl1) unmarshaller.unmarshal(sourceSBGN)).getValue();
		SBGNUtils sbgn = new SBGNUtils(root);
		sbgn.fillRedundantData();
		Graphml out = sbgn.asGraphML();

		if (jaxbContext2 == null) {
			jaxbContext2 = JAXBContext
					.newInstance("com.yworks.xml.graphml:org.graphdrawing.graphml.xmlns.graphml"); //
			marshaller = jaxbContext2.createMarshaller();
		}


		marshaller.marshal(out, destGraphML);

	}

}

class XMLFiles implements FilenameFilter {
	public final static String XML = "xml";
	private static Pattern file = Pattern.compile("(.*)\\." + XML);

	@Override
	public boolean accept(File dir, String name) {
		return file.matcher(name.toLowerCase()).matches();
	}

	public static String getName(String name) {
		Matcher m = file.matcher(name.toLowerCase());
		if (!m.matches())
			throw new RuntimeException("The name does not match the pattern");
		return m.group(0);
	}
}
