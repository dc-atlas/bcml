package com.miravtech.annotator;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import com.miravtech.SBGNUtils.INIConfiguration;
import com.miravtech.SBGNUtils.SBGNIterator;
import com.miravtech.SBGNUtils.SBGNUtils;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDl1;
import com.miravtech.sbgn.SelectType;
import com.miravtech.sbgn.StateVariableType;
import com.miravtech.sbgn.StatefulEntiyPoolNodeType;
import com.miravtech.sbgn.graphics.PaintNode;
import com.miravtech.sbgn_graphics.GraphicType;

public class Main {

	JAXBContext jaxbContext;
	Unmarshaller unmarshaller;
	Marshaller marshaller;

	/*
	 * String sbgnFileSource; String textFileSource; String sbgnFileDest;
	 */
	boolean filterExcludedSelection = true;
	String organism;
	String db;
	String varName;
	String algorithm;
	String format = "%.3f"; // can be changed in future, if desided

	SBGNPDl1 sbgnpath;
	SBGNUtils utils;

	SymbolList sl;

	ColorManager colorMan = new ColorManager();

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Main m = new Main();

		m.run(args);
	}

	private Map<String, Double> nodeFCs = new HashMap<String, Double>();

	Double getNodeFc(StatefulEntiyPoolNodeType epn) {
		if (!(filterExcludedSelection && epn.getSelected() == SelectType.EXCLUDE)) {
			Collection<String> symbols = SBGNUtils
					.getSymbols(organism, db, epn);
			Collection<Double> vals = sl.getList(symbols);

			// add the values of the inner elements to the value list
			for (SBGNNodeType n : epn.getInnerNodes()) {
				if (n instanceof StatefulEntiyPoolNodeType) {
					StatefulEntiyPoolNodeType inner = (StatefulEntiyPoolNodeType) n;
					double val;
					if (nodeFCs.containsKey(inner.getID())) {
						val = nodeFCs.get(inner.getID());
						vals.add(val);
					}
				}
			}

			Double val = computeValue(vals, algorithm);
			if (val != null)
				nodeFCs.put(epn.getID(), val);
			return val;
		} else
			return null;
	}

	File sourceSBGN;
	File textFileSource;
	File outFile;

	private void run(String args[]) throws Exception {

		jaxbContext = JAXBContext
				.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");
		unmarshaller = jaxbContext.createUnmarshaller();
		marshaller = jaxbContext.createMarshaller();

		Properties p = INIConfiguration.getConfiguration();

		OptionParser parser = new OptionParser();
//		ArgumentAcceptingOptionSpec<File> aaos_file;
		String prop;
		parser.accepts("srcSBGN", "Name of the SBGN file to use.")
				.withRequiredArg().ofType(File.class).describedAs("file path");
//		prop = p.getProperty("annotator.input");
//		if (prop != null)
//			aaos_file.defaultsTo(new File(prop));

		parser.accepts("varFile",
				"The two column file to annotate in the pathway.")
				.withRequiredArg().ofType(File.class).describedAs("file path");
//		prop = p.getProperty("annotator.varFile");
//		if (prop != null)
//			aaos_file.defaultsTo(new File(prop));

		parser.accepts("outFile", "The target SBGN file.").withRequiredArg()
				.ofType(File.class).describedAs("file path");
//		prop = p.getProperty("annotator.outFile");
//		if (prop != null)
//			aaos_file.defaultsTo(new File(prop));
		
		
		prop = p.getProperty("annotator.alg","AVG");
		parser.accepts("alg", "The algorithm to use, can be MIN, MAX or AVG.")
				.withOptionalArg().ofType(String.class).describedAs(
						"MIN|MAX|AVG").defaultsTo(prop);

		prop = p.getProperty("varname","FC");
		parser.accepts("varName", "The name of the variable to annotate.")
				.withOptionalArg().ofType(String.class).describedAs("VAR")
				.defaultsTo(prop);
		
		prop = p.getProperty("organism","HS");
		parser.accepts("organism", "The name of the organism to consider")
				.withOptionalArg().ofType(String.class).describedAs("Organism")
				.defaultsTo(prop);
		
		prop = p.getProperty("database","EntrezGeneID");
		parser.accepts("db", "The database to consider.").withOptionalArg()
				.ofType(String.class).describedAs("Database").defaultsTo(prop);

		prop = p.getProperty("minNegCol","Blue");
		parser.accepts("minNegCol",
				"The color of the most extreme negative value.")
				.withOptionalArg().ofType(String.class).describedAs("Color")
				.defaultsTo(prop);
		
		prop = p.getProperty("maxNegCol","Green");
		parser.accepts("maxNegCol",
				"The color of the less extreme negative value.")
				.withOptionalArg().ofType(String.class).describedAs("Color")
				.defaultsTo(prop);
		
		prop = p.getProperty("zeroCol","White");
		parser.accepts("zeroCol", "The color of the zero value.")
				.withOptionalArg().ofType(String.class).describedAs("Color")
				.defaultsTo(prop);
		
		prop = p.getProperty("minPosCol","Orange");
		parser.accepts("minPosCol",
				"The color of the less extreme positive value.")
				.withOptionalArg().ofType(String.class).describedAs("Color")
				.defaultsTo(prop);
		
		prop = p.getProperty("maxPosCol","Red");
		parser.accepts("maxPosCol",
				"The color of the most extreme positive value.")
				.withOptionalArg().ofType(String.class).describedAs("Color")
				.defaultsTo(prop);

		try {
			OptionSet opts = parser.parse(args);

			sourceSBGN = (File) opts.valueOf("srcSBGN");
			textFileSource = (File) opts.valueOf("varFile");
			outFile = (File) opts.valueOf("outFile");

			if (sourceSBGN == null)
				throw new Exception("srcSBGN argument not present!");
			if (textFileSource == null)
				throw new Exception("varFile file argument not present!");
			if (outFile == null)
				throw new Exception("outFile file argument not present!");

			varName = (String) opts.valueOf("varName");
			algorithm = (String) opts.valueOf("alg");
			organism = (String) opts.valueOf("organism");
			db = (String) opts.valueOf("db");

			Color c;
			String col;

			col = (String) opts.valueOf("minNegCol");
			c = PaintNode.getColor(col);
			if (c == null)
				throw new Exception("Cannot build color from: " + col);
			colorMan.setColMinFC(c);

			col = (String) opts.valueOf("maxNegCol");
			c = PaintNode.getColor(col);
			if (c == null)
				throw new Exception("Cannot build color from: " + col);
			colorMan.setColZeroNegFC(c);

			col = (String) opts.valueOf("zeroCol");
			c = PaintNode.getColor(col);
			if (c == null)
				throw new Exception("Cannot build color from: " + col);
			colorMan.setColZero(c);

			col = (String) opts.valueOf("minPosCol");
			c = PaintNode.getColor(col);
			if (c == null)
				throw new Exception("Cannot build color from: " + col);
			colorMan.setColZeroPosFC(c);

			col = (String) opts.valueOf("maxPosCol");
			c = PaintNode.getColor(col);
			if (c == null)
				throw new Exception("Cannot build color from: " + col);
			colorMan.setColMaxFC(c);

			sl = new SymbolList(new FileInputStream(textFileSource));
			colorMan.setValues(sl.values());
		} catch (Exception e) {
			System.out.println("Exception occured: " + e.toString()
					+ "\nPossible commands:\n");
			parser.printHelpOn(System.out);
			return;

		}

		// load the SBGN pathway
		sbgnpath = (SBGNPDl1) unmarshaller.unmarshal(sourceSBGN);
		utils = new SBGNUtils(sbgnpath.getValue());

		// expand the data
		utils.setEmptyIDs();
		utils.expandClones();

		// iterate the EPNs and determine the value of the variable
		new SBGNIterator() {
			@Override
			public void iterateNode(SBGNNodeType n) {
				if (n instanceof StatefulEntiyPoolNodeType) {
					getNodeFc((StatefulEntiyPoolNodeType) n);
				}
			}
		}.runBottomUp(sbgnpath.getValue());
		
		if (nodeFCs.size() == 0) { // no node was computed
			System.out.println("No output file will be written, since no entry could be assigned to the pathway");
			return;
		}

		// reload the SBGN pathway
		SBGNPDl1 sbgnpath2 = (SBGNPDl1) unmarshaller.unmarshal(sourceSBGN);
		final SBGNUtils utils2 = new SBGNUtils(sbgnpath2.getValue());

		// fill the IDs that are not specified
		utils2.setEmptyIDs();

		// iterate the EPNs and set the values of the variables; create the
		// variable if missing
		new SBGNIterator() {
			@Override
			public void iterateNode(SBGNNodeType n) {
				if (n instanceof StatefulEntiyPoolNodeType) {
					Double val = nodeFCs.get(n.getID());
					if (val != null) {
						String dispVal = String.format(Locale.US, format, val);
						StateVariableType sv = null;
						for (SBGNNodeType v1 : SBGNUtils.getInnerNodesOfType(n,
								StateVariableType.class).values()) {
							StateVariableType svt = (StateVariableType) v1;
							if (varName.equalsIgnoreCase(svt.getVariable()))
								sv = svt;
						}
						if (sv == null) {
							sv = new StateVariableType();
							sv.setVariable(varName);
							n.getInnerNodes().add(sv);
						}

						// set the variable
						sv.setLabel(dispVal);

						// set the color of the node and variable
						Color c = colorMan.getColor(val);
						String col = PaintNode.toColorString(c);
						setColor(n, col);
						setColor(sv, col);
					}
				}
			}
		}.run(sbgnpath2.getValue());

		// remove the assigned IDs
		utils2.removeAssignedIDs();

		// save the file to the destination
		marshaller.marshal(sbgnpath2, outFile);

	}

	static void setColor(SBGNNodeType node, String color) {
		if (node.getGraphic() == null)
			node.setGraphic(new GraphicType());
		node.getGraphic().setBgColor(color);
		// node.getGraphic().setBorderColor(color);
		Color crtCol = PaintNode.getColor(color);
		String textcolor = PaintNode.toColorString(ColorManager
				.getMostContrastantColor(crtCol, Color.WHITE, Color.BLACK));
		node.getGraphic().setColor(textcolor);
	}

	static public Double computeValue(Collection<Double> vals, String algorithm) {
		if (vals.size() == 0)
			return null;
		if (algorithm.compareToIgnoreCase("max") == 0) {
			double ret = Double.MIN_VALUE;
			for (double d : vals)
				if (Math.abs(d) > Math.abs(ret))
					ret = d;
			return ret;
		} else if (algorithm.compareToIgnoreCase("min") == 0) {
			double ret = Double.MAX_VALUE;
			for (double d : vals)
				if (Math.abs(d) < Math.abs(ret))
					ret = d;
			return ret;

		} else if (algorithm.compareToIgnoreCase("avg") == 0) {
			double ret = 0;
			for (double d : vals)
				ret += d;
			return ret / vals.size();
		} else {
			throw new RuntimeException(
					"Unable to indenfity the algorithm; max, min and avg supported!");
		}
	}
}
