package com.miravtech.annotator;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

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
	String sbgnFileSource;
	String textFileSource;
	String sbgnFileDest;
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
					if (nodeFCs.containsKey(inner.getID()))
					{
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

	private void run(String args[]) throws Exception {

		jaxbContext = JAXBContext
				.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");
		unmarshaller = jaxbContext.createUnmarshaller();
		marshaller = jaxbContext.createMarshaller();


		// Blue Green Darkgray   Orange   Red %.2f

        OptionParser parser = new OptionParser();
        parser.accepts("srcSBGN", "Name of the SBGN file to use.").withRequiredArg().ofType(File.class).describedAs("file path");
        parser.accepts("varFile", "The two column file to annotate in the pathway.").withRequiredArg().ofType(File.class).describedAs("file path");
        parser.accepts("outFile", "The target SBGN file.").withRequiredArg().ofType(File.class).describedAs("file path");
        parser.accepts("alg", "The algorithm to use, can be MIN, MAX or AVG.").withOptionalArg().ofType(String.class).describedAs("MIN|MAX|AVG").defaultsTo("AVG");
        parser.accepts("varName", "The name of the variable to annotate.").withOptionalArg().ofType(String.class).describedAs("VAR").defaultsTo("FC");
        parser.accepts("organism", "The name of the organism to consider").withOptionalArg().ofType(String.class).describedAs("Organism").defaultsTo("HS");
        parser.accepts("db", "The database to consider.").withOptionalArg().ofType(String.class).describedAs("Database").defaultsTo("EntrezGeneID");

        parser.accepts("minNegCol", "The color of the most extreme negative value.").withOptionalArg().ofType(String.class).describedAs("Color").defaultsTo("Blue");
        parser.accepts("maxNegCol", "The color of the less extreme negative value.").withOptionalArg().ofType(String.class).describedAs("Color").defaultsTo("Green");
        parser.accepts("zeroCol", "The color of the zero value.").withOptionalArg().ofType(String.class).describedAs("Color").defaultsTo("White");
        parser.accepts("minPosCol", "The color of the less extreme positive value.").withOptionalArg().ofType(String.class).describedAs("Color").defaultsTo("Orange");
        parser.accepts("maxPosCol", "The color of the most extreme positive value.").withOptionalArg().ofType(String.class).describedAs("Color").defaultsTo("Red");

        parser.printHelpOn(System.out);
        
        OptionSet opts = parser.parse(args);
        
        File sourceSBGN = (File) opts.valueOf("srcSBGN");
        File textFileSource = (File) opts.valueOf("varFile");
        File outFile = (File) opts.valueOf("outFile");
        
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
						String dispVal = String.format(format, val);
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
//		node.getGraphic().setBorderColor(color);
		Color crtCol = PaintNode.getColor(color);
		String textcolor = PaintNode.toColorString(ColorManager.getMostContrastantColor(crtCol, Color.WHITE, Color.BLACK));
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
