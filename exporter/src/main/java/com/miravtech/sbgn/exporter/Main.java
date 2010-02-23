package com.miravtech.sbgn.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.miravtech.SBGNUtils.SBGNUtils;
import com.miravtech.sbgn.SBGNPDl1;

public class Main {

	static JAXBContext jaxbContext;
	static Unmarshaller unmarshaller;
	static Marshaller marshaller;

	/**
	 * @param args
	 * @throws JAXBException
	 */
	public static void main(String[] args) throws Exception {

		
		String source;
		String destination;
		String organism = "HS";
		String db = "EntrezGeneID";
		String method = "GeneList";
		if (args.length >= 5) {
			source = args[0];
			destination = args[1];
			organism = args[2];
			db = args[3];
			method = args[4];
		} else {
			throw new Exception("Please provide the source file, the source destination, the organism, the database and the method.");
		}
		File srcDir = new File(source);
		File destDir = new File(destination);

		if (srcDir.getAbsolutePath().compareToIgnoreCase(
				destDir.getAbsolutePath()) == 0)
			throw new RuntimeException(
					"Source and destination directories or files cannot be identical!");

		
		jaxbContext = JAXBContext
				.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");
		unmarshaller = jaxbContext.createUnmarshaller();
		marshaller = jaxbContext.createMarshaller();

		if (method.equalsIgnoreCase("GeneList"))
			exportSBGN(srcDir, destDir, organism,db,true);
		else
			throw new Exception("Method not supported, currently supported: GeneList");
	}

	public static void exportSBGN(File sourceSBGN, File destFile,
			final String organism, final String db, final boolean usefilter) throws JAXBException, IOException {

		SBGNPDl1 sbgnpath = (SBGNPDl1) unmarshaller.unmarshal(sourceSBGN);
		SBGNUtils utils = new SBGNUtils(sbgnpath.getValue());

		utils.fillRedundantData();
		
		Set<String> genes =  utils.getSymbols(organism, db, usefilter);
		FileOutputStream fos = new FileOutputStream(destFile);
		PrintWriter pr = new PrintWriter(fos);
		for (String g: genes)
			pr.println(g);
		pr.close();

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
