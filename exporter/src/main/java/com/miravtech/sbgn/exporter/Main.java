package com.miravtech.sbgn.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import com.miravtech.SBGNUtils.INIConfiguration;
import com.miravtech.SBGNUtils.SBGNUtils;
import com.miravtech.sbgn.SBGNPDL1Type;
import com.miravtech.sbgn.SBGNPDl1;

public class Main {

	static JAXBContext jaxbContext;
	static Unmarshaller unmarshaller;
	static Marshaller marshaller;

	static OptionSet opts;

	public static void main(String[] args) throws Exception {

		String organism;
		String db;
		String method;
		File srcDir;
		File destDir;
		boolean filtering = true;

		Properties p = INIConfiguration.getConfiguration();

		OptionParser parser = new OptionParser();
		parser.accepts("srcSBGN", "Name of the SBGN file to use.")
				.withRequiredArg().ofType(File.class).describedAs("file path");
		parser.accepts("outFile", "The target file.").withRequiredArg().ofType(
				File.class).describedAs("file path");

		String prop;
		prop = p.getProperty("organism", "HS");
		parser.accepts("organism", "The name of the organism to consider")
				.withOptionalArg().ofType(String.class).describedAs("Organism")
				.defaultsTo(prop);

		prop = p.getProperty("database", "EntrezGeneID");
		parser.accepts("db", "The database to consider.").withOptionalArg()
				.ofType(String.class).describedAs("Database").defaultsTo(prop);

		prop = p.getProperty("exporter.method", "GeneList or SPIA");
		parser.accepts("method", "The method to use.").withOptionalArg()
				.ofType(String.class).describedAs("GeneList").defaultsTo(prop);

		parser.accepts("var",
				"The variable to export the value together with the symbol.")
				.withOptionalArg().ofType(String.class).describedAs("Variable")
				.defaultsTo(prop);

		parser.accepts("disableFilter", "Disable filtering.");

		try {
			opts = parser.parse(args);

			srcDir = (File) opts.valueOf("srcSBGN");
			destDir = (File) opts.valueOf("outFile");

			method = (String) opts.valueOf("method");
			organism = (String) opts.valueOf("organism");
			db = (String) opts.valueOf("db");

			if (opts.has("disableFilter"))
				filtering = false;

			if (srcDir.getAbsolutePath().compareToIgnoreCase(
					destDir.getAbsolutePath()) == 0)
				throw new RuntimeException(
						"Source and destination directories or files cannot be identical!");

			jaxbContext = JAXBContext
					.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");
			unmarshaller = jaxbContext.createUnmarshaller();
			marshaller = jaxbContext.createMarshaller();

			// run the function, we have the arguments;
			if (method.equalsIgnoreCase("GeneList"))
				exportSBGN(srcDir, destDir, organism, db, filtering);
			else if (method.equalsIgnoreCase("SPIA")) {
				exportSPIA(srcDir, destDir,organism, db, filtering);
			} else
			throw new Exception(
					"Method not supported, currently supported: GeneList SPIA");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception occured: " + e.toString()
					+ "\nPossible commands:\n");			
			parser.printHelpOn(System.out);
			return;
		}
	}

	/**
	 * Exports an R file for SPIA.
	 * 
	 * @param sourceSBGN
	 * @param destFile
	 * @param usefilter
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void exportSPIA(File sourceSBGN, File destFile, String organism, String db,
			final boolean usefilter) throws JAXBException, IOException {

		// for each file, get the graph and put it in a map
		Map<SBGNPDL1Type, SPIAGeneGraph> data = new HashMap<SBGNPDL1Type, SPIAGeneGraph>();
		for (File f : getFiles(sourceSBGN)) {
			SBGNPDl1 sbgnpath = (SBGNPDl1) unmarshaller.unmarshal(f);
			SBGNPDL1Type root = sbgnpath.getValue();
			data.put(root, SBGNFileAsSPIAGraph(root,organism,db,usefilter));
		}
		
		// list of all existing reactions
		Set<String> reacttypes = new HashSet<String>();
		reacttypes.add("activation"); // required !!		
		for (Entry<SBGNPDL1Type, SPIAGeneGraph> e : data.entrySet()) 
			for (GeneGeneRel r : e.getValue().getEdges())
				reacttypes.add(r.getReaction());
		
		// output file
		PrintStream ps = new PrintStream(destFile);
		ps.println("path.info=list()");
		int count = 1; // pathway number, if not specified		
		for (Entry<SBGNPDL1Type, SPIAGeneGraph> e : data.entrySet()) {
			SPIAGeneGraph k = e.getValue();
			int sz = k.getNodes().size();
			StringBuffer strNodes = new StringBuffer();
			for (String s : k.getNodes()) {
				strNodes.append("\"");
				strNodes.append(s); // id of the node
				strNodes.append("\",");
			}
			strNodes.deleteCharAt(strNodes.length() - 1);
			ps.println("nodes=c(" + strNodes + ")");
			ps.println("listnodes=as.list(nodes)");
			String name = e.getKey().getPathwayName();
			if (name == null)
				name = "Unknown";
			String ID = e.getKey().getPathwayID();
			if (ID == null)
				ID = "Unknown" + count++;
			ps.println("crtpath=list(\"" + name + "\",listnodes,"
					+ k.getEdges().size() + ")");
			ps
					.println("names(crtpath)=c(\"title\",\"nodes\",\"NumberOfReactions\")");



			GeneGeneRel rels[][] = new GeneGeneRel[sz][sz];

			for (String relation : reacttypes) {
				ps.println("m = matrix(data=0,nrow=" + sz + ",ncol=" + sz
						+ ", byrow=TRUE)");
				k.getEdgeMatrix(rels);
				for (int i = 0; i != sz; i++)
					for (int j = 0; j != sz; j++)
						if (rels[i][j] != null)
							if (rels[i][j].getReaction().equals(relation))
								ps.println("m[" + (j + 1) + "," + (i + 1)
										+ "]=1");

				ps.println("rownames(m)=nodes");
				ps.println("colnames(m)=nodes");
				ps.println("crtpath=c(crtpath,list(m))");
				ps.println("names(crtpath)[length(crtpath)]=\"" + relation
						+ "\"");

			}

			// add the object to the R list.
			ps.println("path.info=c(path.info,list(crtpath))");
			ps.println("names(path.info)[length(path.info)]=\"" + ID + "\"");

		}

		// create an object with relation types
		StringBuffer strNodes = new StringBuffer();
		for (String s : reacttypes) {
			strNodes.append("\"");
			strNodes.append(s);
			strNodes.append("\",");
		}
		if (strNodes.length() > 0)
			strNodes.deleteCharAt(strNodes.length() - 1);
		ps.println("rel <- c(" + strNodes.toString() + ")");
		strNodes = new StringBuffer();
		for (String s : reacttypes) {
			strNodes.append(getDefaultBeta(s));
			strNodes.append(",");
		}
		if (strNodes.length() > 0)
			strNodes.deleteCharAt(strNodes.length() - 1);
		ps.println("beta <- c(" + strNodes.toString() + ")");
		ps.println("names(beta) <- rel");

		ps.println("name = paste(system.file(\"extdata\", package = \"SPIA\"), paste(\"/SBGN_SPIA\", sep = \"\"), \".RData\", sep = \"\")");
		ps.println("save (path.info, file=name)");
		ps.close();

	}

	public static double getDefaultBeta(String s) {
		if (s.equalsIgnoreCase("activation"))
			return 1;
		if (s.equalsIgnoreCase("compound"))
			return 0;
		if (s.equalsIgnoreCase("inhibition"))
			return -1;
		if (s.equalsIgnoreCase("methilation"))
			return -1;
		if (s.equalsIgnoreCase("demethilation"))
			return 1;
		if (s.equalsIgnoreCase("phosphorylation"))
			return 1;
		if (s.equalsIgnoreCase("dephosphorylation"))
			return -1;

		// default
		return 1;
	}

	public static SPIAGeneGraph SBGNFileAsSPIAGraph(SBGNPDL1Type src, String organism, String db,
			final boolean usefilter) {
		SBGNGraph g = new SBGNGraph(src);
		if (usefilter)
			g.applyFilter();
		g.adaptCatalystReactions(); 
		g.deleteProdCons();
		return g.toSPIAGeneGraph(organism, db, usefilter);
	}

	public static List<File> getFiles(File s) {
		List<File> ret = new LinkedList<File>();
		if (!s.exists()) {
			System.err.println("No source file has beeen found");
			return ret;
		}
		if (s.isFile()) {
			ret.add(s);
			return ret;
		}
		// directory
		File f1[] = s.listFiles(new XMLFiles());
		for (File f : f1) {
			ret.add(f);
		}
		return ret;

	}

	public static void exportSBGN(File sourceSBGN, File destFile,
			final String organism, final String db, final boolean usefilter)
			throws JAXBException, IOException {

		SBGNPDl1 sbgnpath = (SBGNPDl1) unmarshaller.unmarshal(sourceSBGN);
		SBGNUtils utils = new SBGNUtils(sbgnpath.getValue());

		utils.fillRedundantData();
		FileOutputStream fos = new FileOutputStream(destFile);
		PrintWriter pr = new PrintWriter(fos);

		if (opts.has("var")) { // gene + variable
			Map<String, String> genes = utils.getVariable(organism, db,
					usefilter, (String) opts.valueOf("var"));
			for (Entry<String, String> g : genes.entrySet())
				pr.println(g.getKey() + "\t" + g.getValue());
			pr.close();

		} else { // gene list
			Set<String> genes = utils.getSymbols(organism, db, usefilter);
			for (String g : genes)
				pr.println(g);
			pr.close();
		}

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
