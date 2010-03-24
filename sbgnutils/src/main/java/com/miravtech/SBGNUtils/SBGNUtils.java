package com.miravtech.SBGNUtils;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.beanutils.BeanUtils;
import org.graphdrawing.graphml.xmlns.graphml.Data;
import org.graphdrawing.graphml.xmlns.graphml.Edge;
import org.graphdrawing.graphml.xmlns.graphml.Graph;
import org.graphdrawing.graphml.xmlns.graphml.GraphEdgedefaultType;
import org.graphdrawing.graphml.xmlns.graphml.Graphml;
import org.graphdrawing.graphml.xmlns.graphml.Key;
import org.graphdrawing.graphml.xmlns.graphml.KeyForType;
import org.graphdrawing.graphml.xmlns.graphml.Node;

import com.miravtech.sbgn.AndNodeType;
import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.AssociationType;
import com.miravtech.sbgn.AuxiliaryUnitType;
import com.miravtech.sbgn.CatalysisArcType;
import com.miravtech.sbgn.CompartmentType;
import com.miravtech.sbgn.ComplexType;
import com.miravtech.sbgn.ConsumptionArcType;
import com.miravtech.sbgn.DissociationType;
import com.miravtech.sbgn.EntityPoolNodeType;
import com.miravtech.sbgn.InhibitionArcType;
import com.miravtech.sbgn.LogicArcType;
import com.miravtech.sbgn.LogicalOperatorNodeType;
import com.miravtech.sbgn.ModulationArcType;
import com.miravtech.sbgn.NotNodeType;
import com.miravtech.sbgn.OmittedProcessType;
import com.miravtech.sbgn.OrNodeType;
import com.miravtech.sbgn.PhenotypeType;
import com.miravtech.sbgn.ProcessType;
import com.miravtech.sbgn.ProductionArcType;
import com.miravtech.sbgn.SBGNGlyphType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDL1Type;
import com.miravtech.sbgn.SelectType;
import com.miravtech.sbgn.SinkType;
import com.miravtech.sbgn.SourceType;
import com.miravtech.sbgn.StateVariableType;
import com.miravtech.sbgn.StatefulEntiyPoolNodeType;
import com.miravtech.sbgn.UncertainProcessType;
import com.miravtech.sbgn.StatefulEntiyPoolNodeType.Organism;
import com.miravtech.sbgn.StatefulEntiyPoolNodeType.Organism.Annotation;
import com.miravtech.sbgn.graphics.PaintNode;
import com.miravtech.sbgn_graphics.GraphicType;
import com.yworks.xml.graphml.ArrowTypeType;
import com.yworks.xml.graphml.GeometryType;
import com.yworks.xml.graphml.GroupNode;
import com.yworks.xml.graphml.LineStyleType;
import com.yworks.xml.graphml.NodeLabelModelType;
import com.yworks.xml.graphml.NodeLabelPositionType;
import com.yworks.xml.graphml.NodeLabelType;
import com.yworks.xml.graphml.NodeType;
import com.yworks.xml.graphml.PolyLineEdge;
import com.yworks.xml.graphml.ProxyShapeNode;
import com.yworks.xml.graphml.ResourceType;
import com.yworks.xml.graphml.Resources;
import com.yworks.xml.graphml.SVGNode;
import com.yworks.xml.graphml.ShapeNode;
import com.yworks.xml.graphml.ShapeTypeType;
import com.yworks.xml.graphml.EdgeType.Arrows;
import com.yworks.xml.graphml.GroupNode.State;
import com.yworks.xml.graphml.NodeType.Fill;
import com.yworks.xml.graphml.ProxyShapeNodeType.Realizers;
import com.yworks.xml.graphml.SVGNode.SVGModel;
import com.yworks.xml.graphml.SVGNode.SVGModel.SVGContent;
import com.yworks.xml.graphml.ShapeNode.Shape;

public class SBGNUtils {

	private Map<String, SBGNNodeType> nodes = new HashMap<String, SBGNNodeType>();

	private Set<String> idSet = new HashSet<String>();
	private Set<String> assignedIDs = new HashSet<String>();

	public String getPossibleID(String begin) {
		String root = begin;
		// remove xml unfriendly chars
		root = root.replace('\"', '_');
		root = root.replace('>', '_');
		root = root.replace('\'', '_');
		root = root.replace('<', '_');
		if (!idSet.contains(root)) {
			idSet.add(root);
			return root;
		}
		int i = 1;
		while (true) {
			String id = root + "_" + i++;
			if (!idSet.contains(id)) {
				idSet.add(id);
				return id;
			}

		}
	}

	SBGNPDL1Type in;

	public SBGNUtils(SBGNPDL1Type in) {
		this.in = in;
	}

	public void removeAssignedIDs() {

		new SBGNIterator() {
			@Override
			public void iterateGlyph(SBGNGlyphType n) {
				if (assignedIDs.contains(n.getID())) {
					n.setID(null);
				}
			}
		}.run(in);

	}

	public void fillRedundantData() throws JAXBException {
		setEmptyLabels();
		setEmptyIDs();
		expandClones();
	}

	public void setEmptyLabels() {
		// set empty labels, if ID is provided
		new SBGNIterator() {
			@Override
			public void iterateNode(SBGNNodeType n) {
				if (n.getLabel() == null && n.getID() != null)
					n.setLabel(n.getID());
			}
		}.run(in);
	}

	public void setEmptyIDs() throws JAXBException {
		// save the ids in a map
		new SBGNIterator() {
			public void iterateNode(SBGNNodeType n) {
				if (n.getID() != null) // save the entry in the map
					nodes.put(n.getID(), n);
				if (n.getID() != null)
					idSet.add(n.getID());
			};
		}.run(in);

		// set the IDs
		new SBGNIterator() {
			@Override
			public void iterateGlyph(SBGNGlyphType n) {

				if (n.getID() != null)
					return;

				// determine the beginning of the possible id
				String root = null;
				if (n instanceof SBGNNodeType) {
					SBGNNodeType node = (SBGNNodeType) n;
					if (node.getLabel() != null)
						root = node.getLabel();
				}

				if (root == null)
					root = n.getClass().getSimpleName();
				String id = getPossibleID(root);
				n.setID(id);
				assignedIDs.add(id);
			}
		}.run(in);

	}

	public void expandClones() {
		/*
		 * // dump JAXBContext jaxbContext = JAXBContext
		 * .newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics"); //
		 * Marshaller marshaller = jaxbContext.createMarshaller();
		 * marshaller.marshal(in, new File("SBGNDEBUG.xml"));
		 */
		// solve the clones

		while (true) {
			final boolean[] found = new boolean[1];
			new SBGNIterator() {
				public void iterateNode(SBGNNodeType n) {
					if (n.getCloneref() != null) {
						SBGNNodeType cloned = nodes.get(n.getCloneref());
						if (cloned.getCloneref() == null) {
							Clone(n, cloned);
							found[0] = true;
						}
					}
				};
			}.run(in);
			if (!found[0])
				break; // no further clone resolved
		}
	}

	// private static int count = 0;

	/**
	 * @return
	 */
	public Graphml asGraphML() {
		final Graphml graphml = new Graphml();
		Key k;

		k = new Key();
		k.setFor(KeyForType.NODE);
		k.setId("d3");
		k.setYfilesType("nodegraphics");
		graphml.getKeies().add(k);

		k = new Key();
		k.setFor(KeyForType.EDGE);
		k.setId("d6");
		k.setYfilesType("edgegraphics");
		graphml.getKeies().add(k);

		k = new Key();
		k.setFor(KeyForType.GRAPHML);
		k.setId("d0");
		k.setYfilesType("resources");
		graphml.getKeies().add(k);

		final Resources res = new Resources();
		Data d0 = new Data();
		d0.getContent().add(res);
		d0.setKey("d0");

		final Graph main = new Graph();
		graphml.getGraphsAndDatas().add(main);
		main.setEdgedefault(GraphEdgedefaultType.DIRECTED);
		main.setId("MainGraph");

		// the graphical representation of each node
		final Map<SBGNNodeType, Graph> mapping = new HashMap<SBGNNodeType, Graph>();
		mapping.put(null, main);

		new SBGNIterator() {
			public void iterateNode(SBGNNodeType n) {
				GraphicType g = n.getGraphic();

				String textColor = PaintNode.toColorString(PaintNode
						.getNodeColor(g));
				String bgColor = PaintNode.toColorString(PaintNode
						.getNodeBgColor(g));
				String borderColor = PaintNode.toColorString(PaintNode
						.getBorderColor(g));

				Graph loc;
				if (stack.size() == 0) // base
					loc = main;
				else {
					loc = mapping.get(getLastNode());
					if ((getLastNode() instanceof ComplexType))
						return; // don't paint the inside of the complex
					if ((n instanceof AuxiliaryUnitType))
						return; // don't render them
				}

				Node theNode = new Node();
				Data dt = new Data();
				dt.setKey("d3");
				theNode.getDatasAndPorts().add(dt);

				if (n instanceof SinkType || n instanceof AuxiliaryUnitType
						|| n instanceof SourceType
						|| (n instanceof EntityPoolNodeType)) {
					int sz = res.getResources().size();
					String ID = "" + (sz + 1);
					String xml = PaintNode.DrawNode(n);
					ResourceType r = new ResourceType();
					r.setId(ID);
					r.setType("java.lang.String");
					r.getContent().add(xml);
					res.getResources().add(r);

					SVGNode node = new SVGNode();
					GeometryType gmt = new GeometryType();
					gmt.setWidth(PaintNode.lastPoint.getX());
					gmt.setHeight(PaintNode.lastPoint.getY());
					node.setGeometry(gmt);
					node.setSVGModel(new SVGModel());
					node.getSVGModel().setSvgBoundsPolicy("0");
					node.getSVGModel().setSVGContent(new SVGContent());
					node.getSVGModel().getSVGContent().setRefid(ID);
					dt.getContent().add(node);
				}

				NodeLabelType nlt = new NodeLabelType();
				String label = n.getLabel();
				if (n instanceof OmittedProcessType)
					label = "\\\\";
				if (n instanceof UncertainProcessType)
					label = "?";
				if (n instanceof DissociationType)
					label = "O";
				if (n instanceof AndNodeType)
					label = "AND";
				if (n instanceof OrNodeType)
					label = "OR";
				if (n instanceof NotNodeType)
					label = "NOT";
				if (n instanceof AssociationType)
					label = "";
				if (n instanceof ComplexType) // no label for the complexes
					label = "";

				nlt.setValue(label);
				nlt.setTextColor(textColor);

				if (n instanceof CompartmentType) {
					nlt.setModelName(NodeLabelModelType.INTERNAL);
					nlt.setModelPosition(NodeLabelPositionType.TR);
					ProxyShapeNode p = new ProxyShapeNode();
					p.setRealizers(new Realizers());
					p.getRealizers().setActive(new BigInteger("0"));

					// -------
					GroupNode gnt = new GroupNode();
					gnt.setShape(new GroupNode.Shape());
					if (n instanceof CompartmentType)
						gnt.getShape().setType(ShapeTypeType.ROUNDRECTANGLE);
					else
						gnt.getShape().setType(ShapeTypeType.RECTANGLE); // complex
					gnt.setFill(new Fill());
					gnt.getFill().setHasColor(true);
					gnt.getFill().setColor(bgColor);

					LineStyleType lst = new LineStyleType();
					gnt.setBorderStyle(lst);
					lst.setHasColor(true);
					lst.setColor(borderColor);
					lst.setWidth(6.0); // compartiment is shown with ticker ink

					nlt.setTextColor(textColor);
					gnt.getNodeLabels().add(nlt);

					State s = new State();
					s.setClosed(false);
					s.setInnerGraphDisplayEnabled(true);
					gnt.setState(s);

					p.getRealizers().getShapeNodesAndImageNodesAndGroupNodes()
							.add(gnt);

					try {
						GroupNode n1 = (GroupNode) BeanUtils.cloneBean(gnt);
						n1.getState().setClosed(true);
						p.getRealizers()
								.getShapeNodesAndImageNodesAndGroupNodes().add(
										n1);

					} catch (Exception e) {
						throw new RuntimeException(e);
					}

					dt.getContent().add(p);
					Graph inner = new Graph();
					inner.setEdgedefault(GraphEdgedefaultType.DIRECTED);

					theNode.setGraph(inner);
					mapping.put(n, inner);
				} else {
					if (n instanceof ProcessType
							|| n instanceof LogicalOperatorNodeType) {
						// simple node
						ShapeNode s = new ShapeNode();
						s.setFill(new NodeType.Fill());
						s.getFill().setHasColor(false);

						Shape sh = new Shape();
						sh.setType(ShapeTypeType.RECTANGLE);
						if (n instanceof PhenotypeType)
							sh.setType(ShapeTypeType.DIAMOND);
						if (n instanceof AssociationType) {
							sh.setType(ShapeTypeType.ELLIPSE);
							s.getFill().setHasColor(true);
							s.getFill().setColor(borderColor);
						}
						if (n instanceof DissociationType)
							sh.setType(ShapeTypeType.ELLIPSE);
						if (n instanceof LogicalOperatorNodeType)
							sh.setType(ShapeTypeType.ELLIPSE);
						s.setShape(sh);

						if (n instanceof ProcessType) {
							// processes are shown smaller
							s.setGeometry(new GeometryType());
							s.getGeometry().setHeight(15);
							s.getGeometry().setWidth(15);
						}

						LineStyleType lst = new LineStyleType();
						lst.setHasColor(true);
						lst.setColor(borderColor);
						s.setBorderStyle(lst);
						s.getNodeLabels().add(nlt);
						dt.getContent().add(s);
					}
				}
				theNode.setId(n.getID());
				loc.getDatasAndNodesAndEdges().add(theNode);
			};
		}.run(in);

		new SBGNIterator() {
			public void iterateArc(ArcType n) {
				String borderColor = PaintNode.toColorString(PaintNode
						.getBorderColor(n.getGraphic()));

				SBGNNodeType n1 = nodes.get(n.getRefNode());
				SBGNNodeType n2 = getCurrentNode();

				if (n1 instanceof ProcessType) { // any arc goes to a process,
					// except production
					if (!(n instanceof ProductionArcType)) {
						SBGNNodeType tmp = n1;
						n1 = n2;
						n2 = tmp;
					}
				}
				if (n2 instanceof ProcessType) { // production - process to EPN
					if (n instanceof ProductionArcType) {
						SBGNNodeType tmp = n1;
						n1 = n2;
						n2 = tmp;
					}
				}

				if (n2 instanceof EntityPoolNodeType) { // any arc goes from EPN
					// to process
					if (!(n instanceof ProductionArcType)) { // except
						// production
						SBGNNodeType tmp = n1;
						n1 = n2;
						n2 = tmp;
					}
				}
				if (n1 instanceof EntityPoolNodeType) { // production goes from
					// process to epn
					if (n instanceof ProductionArcType) {
						SBGNNodeType tmp = n1;
						n1 = n2;
						n2 = tmp;
					}
				}
				if (n1 instanceof SinkType) { // Sink is always the target
					if (n instanceof ConsumptionArcType) {
						SBGNNodeType tmp = n1;
						n1 = n2;
						n2 = tmp;
					}
				}

				if (n2 instanceof SourceType) { // Source is always the source
					if (n instanceof ConsumptionArcType) {
						SBGNNodeType tmp = n1;
						n1 = n2;
						n2 = tmp;
					}
				}

				Edge e = new Edge();

				e.setSource(n1.getID());
				e.setTarget(n2.getID());
				Data dt = new Data();
				dt.setKey("d6");
				e.getDatas().add(dt);
				PolyLineEdge plet = new PolyLineEdge();
				dt.getContent().add(plet);
				Arrows a = new Arrows();
				a.setSource(ArrowTypeType.NONE);
				a.setTarget(ArrowTypeType.NONE);
				// determine the arrow ending type / start
				if (n instanceof ProductionArcType)
					a.setTarget(ArrowTypeType.DELTA);
				if (n instanceof ModulationArcType)
					a.setTarget(ArrowTypeType.WHITE_DIAMOND);
				if (n instanceof CatalysisArcType)
					a.setTarget(ArrowTypeType.TRANSPARENT_CIRCLE);
				if (n instanceof InhibitionArcType)
					a.setTarget(ArrowTypeType.T_SHAPE);
				plet.setArrows(a);
				plet.setLineStyle(new LineStyleType());
				plet.getLineStyle().setColor(borderColor);

				main.getDatasAndNodesAndEdges().add(e);
			};
		}.run(in);

		graphml.getGraphsAndDatas().add(d0);

		return graphml;

	}

	// TODO this function does not work for a hierarchical AND/OR construction
	/**
	 * Returns the list of inputs for this logic operator node
	 * 
	 * @param n
	 *            the logic operator node
	 * @return
	 */
	public Set<SBGNNodeType> getInNodesOfLogic(LogicalOperatorNodeType n) {
		getEdges();
		Set<SBGNNodeType> ret = new HashSet<SBGNNodeType>();
		for (ArcType a : connections.get(n)) {
			if (a instanceof LogicArcType) {
				ret.add(getOtherNode(a, n));
			}
		}
		return ret;
	}

	// TODO this function does not work for a hierarchical AND/OR construction

	/**
	 * Returns the output for this logic operator node
	 * 
	 * @param n
	 *            the logic operator node
	 * @return
	 */
	public SBGNNodeType getOutNodeOfLogic(LogicalOperatorNodeType n) {
		getEdges();
		for (ArcType a : connections.get(n)) {
			if (!(a instanceof LogicArcType)) {
				return getOtherNode(a, n);
			}
		}
		throw new RuntimeException("Hierarchical AND/OR not implemented");
	}

	/**
	 * Copies the content of model to n1.
	 * 
	 * @param n1
	 * @param model
	 */
	static void CloneBeans(SBGNNodeType n1, SBGNNodeType model) {

		// TODO iterate all the getters!!
		if (n1.getLabel() == null)
			n1.setLabel(model.getLabel());

		n1.getFinding().addAll(model.getFinding());

		// if lists, append from model to n1

		// if bean objects, not null call recursively
		try {
			if (n1.getGraphic() == null)
				if (model.getGraphic() != null)
					n1.setGraphic((GraphicType) BeanUtils.cloneBean(model
							.getGraphic()));

			if (n1 instanceof StatefulEntiyPoolNodeType) {
				StatefulEntiyPoolNodeType n1_epn = (StatefulEntiyPoolNodeType) n1;
				StatefulEntiyPoolNodeType model_epn = (StatefulEntiyPoolNodeType) model;
				n1_epn.getOrganism().addAll(model_epn.getOrganism());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// if simple object, clone and copy if the n1 side is null

	}

	private SBGNNodeType cloneNode(SBGNNodeType n1) {
		try {

			SBGNNodeType ret = (SBGNNodeType) BeanUtils.cloneBean(n1);
			ret.getInnerNodes().clear();
			for (SBGNNodeType n2 : n1.getInnerNodes()) {
				ret.getInnerNodes().add(cloneNode(n2));
			}
			if (ret.getID() != null)
				ret.setID(getPossibleID(ret.getID()));
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Copy all node data from model to n1
	 * 
	 * @param target
	 * @param model
	 */
	void Clone(SBGNNodeType target, SBGNNodeType model) {
		// variables of n1

		if (target.getID() == null) {
			target.setID(getPossibleID(model.getID()));
		}

		// IDs have to be copied and changed to unique ones, since on
		// copying they become non-unique
		SBGNNodeType c = cloneNode(model);
		Map<String, StateVariableType> clonedvars = getNodeVariables(c); // skip
		// the
		// variables
		c.getInnerNodes().removeAll(clonedvars.values());
		target.getInnerNodes().addAll(c.getInnerNodes());

		// !! ids of the existing state vars must be kept unchanged !!
		Map<String, StateVariableType> target_vars = getNodeVariables(target);
		for (Map.Entry<String, StateVariableType> e : getNodeVariables(model)
				.entrySet()) {
			StateVariableType sv = target_vars.get(e.getKey());
			if (sv == null) {
				// clone
				sv = new StateVariableType();
				Clone(sv, e.getValue());
				target.getInnerNodes().add(sv);
			} else {
				sv.setLabel(e.getValue().getLabel());
			}
		}

		// copy the data
		CloneBeans(target, model);

		target.setCloneref(null); // remove the clone attribute.
	}

	static Map<String, StateVariableType> getNodeVariables(SBGNNodeType n) {
		Map<String, StateVariableType> ret = new HashMap<String, StateVariableType>();
		for (SBGNNodeType n1 : n.getInnerNodes()) {
			if (n1 instanceof StateVariableType) {
				StateVariableType var = (StateVariableType) n1;
				ret.put(var.getVariable(), var);
			}
		}
		return ret;
	}

	public static Map<String, SBGNNodeType> getInnerNodesOfType(SBGNNodeType n,
			Class<?> c) {
		Map<String, SBGNNodeType> ret = new HashMap<String, SBGNNodeType>();
		for (SBGNNodeType n1 : n.getInnerNodes()) {
			if (c.isInstance(c)) {
				ret.put(n1.getID(), n1);
			}
		}
		return ret;
	}

	public Set<String> getSymbols(final String organism, final String db,
			final boolean filterExcludedSelection) {
		final Set<String> ret = new HashSet<String>();

		new SBGNIterator() {
			@Override
			public void iterateNode(SBGNNodeType n) {
				if (n instanceof StatefulEntiyPoolNodeType) {
					StatefulEntiyPoolNodeType sepnt = (StatefulEntiyPoolNodeType) n;
					if (!(filterExcludedSelection && n.getSelected() == SelectType.EXCLUDE))
						ret.addAll(getSymbols(organism, db, sepnt));
				}
			}
		}.run(in);
		return ret;
	}

	public Map<String, String> getVariable(final String organism,
			final String db, final boolean filterExcludedSelection,
			final String variableName) {
		final Map<String, String> ret = new HashMap<String, String>();
		new SBGNIterator() {
			@Override
			public void iterateNode(SBGNNodeType n) {
				if (n instanceof StatefulEntiyPoolNodeType) {
					StatefulEntiyPoolNodeType sepnt = (StatefulEntiyPoolNodeType) n;
					if (!(filterExcludedSelection && n.getSelected() == SelectType.EXCLUDE)) {
						Set<String> sym = getSymbols(organism, db, sepnt);
						StateVariableType st = getNodeVariables(sepnt).get(
								variableName);
						if (st != null) {
							for (String s : sym)
								ret.put(s, st.getLabel());
						}
					}
				}
			}
		}.run(in);
		return ret;
	}

	static private final String SBGNIDDatabase = "SBGNID";

	public static Set<String> getSymbols(String organism, String db,
			StatefulEntiyPoolNodeType node) {
		Set<String> ret = new HashSet<String>();
		if (db.equalsIgnoreCase(SBGNIDDatabase)) {
			if (node.getID() != null)
				ret.add(node.getID());
			return ret;
		}
		for (Organism o : node.getOrganism())
			if (organism.equalsIgnoreCase(o.getName()))
				for (Annotation a : o.getAnnotation())
					if (db.equalsIgnoreCase(a.getDB()))
						ret.add(a.getID());
		return ret;
	}

	Map<SBGNNodeType, Collection<ArcType>> connections = null;

	/**
	 * Returns all the edges of an arch
	 * 
	 * @param node
	 * @return
	 */
	public Collection<ArcType> getEdges(SBGNNodeType node) {
		getEdges();
		Collection<ArcType> ret = connections.get(node);
		if (ret == null)
			ret = new HashSet<ArcType>();
		return ret;

	}

	public SBGNNodeType getOtherNode(ArcType arc, SBGNNodeType node) {
		getEdges();
		for (Map.Entry<SBGNNodeType, Collection<ArcType>> e : connections
				.entrySet())
			if (e.getValue().contains(arc) && e.getKey() != node)
				return e.getKey();
		throw new RuntimeException("Other side of the arc not found!"); // should
																		// not
																		// happen
	}

	public Map<SBGNNodeType, Collection<ArcType>> getEdges() {
		// build a node-archs map and check the archs
		if (connections != null)
			return connections;
		connections = new HashMap<SBGNNodeType, Collection<ArcType>>();
		SBGNIterator it2 = new SBGNIterator() {

			@Override
			public void iterateNode(SBGNNodeType n) {
				if (!connections.containsKey(n)) {
					connections.put(n, new HashSet<ArcType>());
				}
				for (ArcType a : n.getArcs()) {
					SBGNGlyphType g = nodes.get(a.getRefNode());
					if (g instanceof SBGNNodeType) {
						SBGNNodeType n1 = (SBGNNodeType) g;
						if (!connections.containsKey(n1)) {
							connections.put(n1, new HashSet<ArcType>());
						}
						connections.get(n).add(a);
						connections.get(n1).add(a);
					}
				}
			}
		};
		it2.run(in);
		return connections;
	}

}
