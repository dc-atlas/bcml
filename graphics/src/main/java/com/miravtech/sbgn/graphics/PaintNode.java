package com.miravtech.sbgn.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.miravtech.sbgn.AuxiliaryUnitType;
import com.miravtech.sbgn.ComplexType;
import com.miravtech.sbgn.EntityPoolNodeType;
import com.miravtech.sbgn.MacromoleculeType;
import com.miravtech.sbgn.NucleicAcidFeatureType;
import com.miravtech.sbgn.PerturbingAgentType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SimpleChemicalType;
import com.miravtech.sbgn.SinkType;
import com.miravtech.sbgn.SourceType;
import com.miravtech.sbgn.StateVariableType;
import com.miravtech.sbgn.TagType;
import com.miravtech.sbgn.UnitOfInformationType;
import com.miravtech.sbgn.UnspecifiedEntityType;
import com.miravtech.sbgn_graphics.GraphicType;


public class PaintNode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("FrameDemo");
		frame.add(new Component() {

			private static final long serialVersionUID = -1896912267511312627L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g1 = (Graphics2D) g;
				g1.translate(100, 100);
				// DrawSyncSource((Graphics2D) g);

//				ComplexType nft = new ComplexType();
//				MacromoleculeType m1;
//				for (int i = 0; i != 10; i++) {
//					m1 = new MacromoleculeType();
//					m1.setLabel("m"+i);
//					nft.getInnerNodes().add(m1);
//				}
				//nft.setLabel("test");
//				DrawNode(g1, nft);

				TagType t = new TagType();
				t.setLabel("TheTag");
				DrawNode(g1, t);
				
				
//				ComplexType nft = new ComplexType();
//				nft.setLabel("Test");
//				nft.setGraphic(new GraphicType());
//				nft.getGraphic().setBorderColor("green");
//				nft.getGraphic().setColor("blue");
//				nft.getGraphic().setBgColor("white");
//
//				nft.setCardinality(new BigInteger("5"));
//				// g1.translate(90, -90);
//				// DrawNode(g1, nft);
//				UnitOfInformationType u1 = new UnitOfInformationType();
//				u1.setLabel("Info1");
//				nft.getInnerNodes().add(u1);
//				u1 = new UnitOfInformationType();
//				u1.setLabel("Info2");
//				u1.setPrefix("pre");
//				nft.getInnerNodes().add(u1);
//
//				StateVariableType sv = new StateVariableType();
//				sv.setLabel("val");
//				sv.setVariable("variable");
//				nft.getInnerNodes().add(sv);
//
//				
//				MacromoleculeType mac1 = new MacromoleculeType();
//				mac1.setLabel("testLabel1");
//				mac1.setCardinality(new BigInteger("1000000"));
//				mac1.getInnerNodes().add(u1);
//
//				sv = new StateVariableType();
//				sv.setLabel("val");
//				sv.setVariable("variable11111111111111111111");
//				mac1.getInnerNodes().add(sv);
//
//				
//				MacromoleculeType mac2 = new MacromoleculeType();
//				mac2.setLabel("testLabel2");
//				mac2.getInnerNodes().add(u1);
//				
//				nft.getInnerNodes().add(mac1);
//				nft.getInnerNodes().add(mac2);
		
				
//				JAXBContext jaxbContext;
//				try {
//					jaxbContext = JAXBContext
//					.newInstance("com.miravtech.sbgn:com.miravtech.sbgn_graphics");
//		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//		
//		SBGNPDl1 root1 = (SBGNPDl1) unmarshaller.unmarshal(new File("../pathways/TLR3expandedForPaper1.xml"));
//		SBGNPDL1Type root = root1.getValue();
//		SBGNNodeType n1 = root.getGlyphs().get(0);
//		
//				 
//		DrawNode(g1, n1);
//				} catch (JAXBException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}   //

			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(500, 500);
		frame.setVisible(true);

	}

	public final static int SYNCSIZE = 40;

	public static Point lastPoint = new Point();

	
	public static String toColorString(Color c) {
		return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
	}
	

	public static final Font labelFont = new Font("Serif", Font.PLAIN, 14);
	public static final Font decoFont = new Font("Serif", Font.PLAIN, 10);

	public static final int INSET_X_DECO = 8, INSET_Y_DECO = 4;
	public static final int INSET_X_NODE = 20, INSET_Y_NODE = 10; // minimal
	// distance
	// between
	// texts

	public static final int DIST_X_DECO = 5; // distance between decorators
	public static final int MULTIMER_REPEAT_DIST = 4; // distance between the
	// two multimer edges


	public static final int INSET_X_COMPLEX = 5;
	public static final int START_Y_COMPLEX = 25;
	public static final int DISTANCE_Y_COMPLEX = 20;
	public static final int DISTANCE_X_COMPLEX = 10;
	
	public static final int DECOSTART_X = 20; // where to start the decorators

	public static String getPaintingLabel(SBGNNodeType node) {
		if (node instanceof StateVariableType) {
			StateVariableType sv = (StateVariableType) node;
			if (sv.getVariable() != null)
				return sv.getVariable() + "@" + sv.getLabel();
		}
		if (node instanceof UnitOfInformationType) {
			UnitOfInformationType ui = (UnitOfInformationType) node;
			if (ui.getPrefix() != null)
				return ui.getPrefix() + ":" + ui.getLabel();
		}
		return node.getLabel();
	}

	public static Color getNodeColor(GraphicType g) {
		if (g == null)
			return getColor(null);
		return getColor(g.getColor());
	}
	
	public static Color getNodeBgColor(GraphicType g) {
		if (g == null || g.getBgColor() == null)
			return Color.WHITE;
		return getColor(g.getBgColor());
	}
	

	public static Color getBorderColor(GraphicType g) {
		if (g == null)
			return getColor(null);
		return getColor(g.getBorderColor());
	}
	

	
	
	public static Color getColor(String color) {
		if (color == null)
			return Color.BLACK;
		if (color.compareToIgnoreCase("black") == 0)
			return Color.BLACK;
		if (color.compareToIgnoreCase("blue") == 0)
			return Color.blue;
		if (color.compareToIgnoreCase("cyan") == 0)
			return Color.cyan ;
		if (color.compareToIgnoreCase("darkGray") == 0)
			return Color.darkGray;
		if (color.compareToIgnoreCase("gray") == 0)
			return Color.gray;
		if (color.compareToIgnoreCase("green") == 0)
			return Color.green;
		if (color.compareToIgnoreCase("lightGray") == 0)
			return Color.lightGray;
		if (color.compareToIgnoreCase("magenta") == 0)
			return Color.magenta;
		if (color.compareToIgnoreCase("orange") == 0)
			return Color.orange;
		if (color.compareToIgnoreCase("pink") == 0)
			return Color.pink;
		if (color.compareToIgnoreCase("red") == 0)
			return Color.red;
		if (color.compareToIgnoreCase("white") == 0)
			return Color.white;
		if (color.compareToIgnoreCase("yellow") == 0)
			return Color.yellow;
		
		//color code
		int r = Integer.parseInt(color.substring(1, 3), 16) ;
		int g = Integer.parseInt(color.substring(3, 5), 16) ;
		int b = Integer.parseInt(color.substring(5, 7), 16) ;
		return new Color(r,g,b);
	}
	
	public static void DrawNode(Graphics2D g2d, SBGNNodeType n) {

		// draw shape
		int from = 0;
		Point decoSz = getNodeDecoratorSize(g2d, n);
		from += decoSz.getY() / 2;
		Point shSize = getNodeShapeSize(g2d, n);
		Shape s = getNodeShape(n, shSize);
		g2d.translate(0, from);
		String card = getCardinality(n);
		Color line = getBorderColor(n.getGraphic());
		Color bg = getNodeBgColor(n.getGraphic());
		if (card != null) {
			g2d.translate(MULTIMER_REPEAT_DIST, MULTIMER_REPEAT_DIST);
			g2d.setColor(bg);
			g2d.fill(s);
			g2d.setColor(line);
			g2d.draw(s);
			g2d.translate(-MULTIMER_REPEAT_DIST, -MULTIMER_REPEAT_DIST);
		}
		g2d.setColor(bg);
		g2d.fill(s);
		g2d.setColor(line);
		g2d.draw(s);
		g2d.translate(0, -from);

		int y = 0;
		if (n instanceof ComplexType) {
//			y = START_Y_COMPLEX;
			ArrangeComplex ac = new ArrangeComplex((ComplexType)n, g2d);
			for (SBGNNodeType nt: ac.arrangement.keySet()) {
				Point loc = ac.arrangement.get(nt);
				g2d.translate(INSET_X_COMPLEX+loc.getX(), START_Y_COMPLEX + loc.getY());
				DrawNode(g2d, nt);
				g2d.translate(-(INSET_X_COMPLEX+loc.getX()), -(START_Y_COMPLEX + loc.getY()));
			}
		} else {
			// draw text
			Rectangle r = new Rectangle(y, from, shSize.x, from + shSize.y);
			OutText(r, getPaintingLabel(n), g2d, n);
		}

		// draw units of info
		int startDeco = DECOSTART_X;
		for (StateVariableType v : getStateVars(n)) {
			Point p = getNodeShapeSize(g2d, v);
			g2d.translate(startDeco, 0);
			DrawNode(g2d, v);
			g2d.translate(-startDeco, 0);
			startDeco += p.getX() + DIST_X_DECO;
		}
		startDeco += DIST_X_DECO;

		for (UnitOfInformationType u : getUnitsOfInformation(n)) {
			Point p = getNodeShapeSize(g2d, u);
			g2d.translate(startDeco, 0);
			DrawNode(g2d, u);
			g2d.translate(-startDeco, 0);
			startDeco += p.getX() + DIST_X_DECO;
		}
		// TODO draw clone, if any
		
		
		// compute the size of the image
		lastPoint.x = shSize.x;
		lastPoint.y = from + shSize.y;
		if (card != null) {
			lastPoint.x += MULTIMER_REPEAT_DIST; 
			lastPoint.y += MULTIMER_REPEAT_DIST; 
		}

	}

	public static String getCardinality(SBGNNodeType node) {
		if (node instanceof EntityPoolNodeType) {
			EntityPoolNodeType epn = (EntityPoolNodeType) node;
			if (epn.getCardinality() != null) {
				return epn.getCardinality().toString();
			}
		}
		return null;
	}

	final static int ROUNDEDCORNER = 10;
	final static int COMPLEXCORNER = 15;

	public static Shape getNodeShape(SBGNNodeType node, Point size) {
		if (node instanceof StateVariableType
				|| node instanceof UnspecifiedEntityType)
			return new Ellipse2D.Double(0, 0, size.getX(), size.getY());
		if (node instanceof UnitOfInformationType)
			return new Rectangle2D.Double(0, 0, size.getX(), size.getY());
		if (node instanceof MacromoleculeType)
			return new RoundRectangle2D.Double(0, 0, size.getX(), size.getY(),
					ROUNDEDCORNER, ROUNDEDCORNER);
		if (node instanceof NucleicAcidFeatureType) {
			// special path, rourd rectacle with edges at the top

			Path2D p = new Path2D.Double();

			p.moveTo(0, 0);
			p.lineTo(size.getX(), 0);
			p.lineTo(size.getX(), size.getY() - ROUNDEDCORNER);
			p.quadTo(size.getX() - ROUNDEDCORNER / 3, size.getY()
					- ROUNDEDCORNER / 3, 
					size.getX() - ROUNDEDCORNER , size.getY() );
			p.lineTo(ROUNDEDCORNER, size.getY() );
			p.quadTo(ROUNDEDCORNER / 3, size.getY() - ROUNDEDCORNER / 3,
					0, size.getY() - ROUNDEDCORNER);

			p.lineTo(0,0);
			return p;
		}
		if (node instanceof PerturbingAgentType) {
			Path2D p = new Path2D.Double();
			p.moveTo(0, 0);
			p.lineTo(size.getY() / 3, size.getY() / 2);
			p.lineTo(0, size.getY());
			p.lineTo(size.getX(), size.getY());
			p.lineTo(size.getX() - size.getY() / 3, size.getY() / 2);
			p.lineTo(size.getX(), 0);
			p.lineTo(0, 0);
			return p;
		}

		if (node instanceof TagType) {
			Path2D p = new Path2D.Double();
			p.moveTo(0, 0);
			p.lineTo(0, size.getY());
			p.lineTo(size.getX() - size.getY() / 2, size.getY());
			p.lineTo(size.getX(), size.getY() / 2);
			p.lineTo(size.getX() - size.getY() / 2, 0);
			p.lineTo(0, 0);
			return p;

		}

		// complex?
		if (node instanceof ComplexType) {
			Path2D p = new Path2D.Double();
			p.moveTo(0, COMPLEXCORNER);
			p.lineTo(0, size.getY() - COMPLEXCORNER);
			p.lineTo(COMPLEXCORNER, size.getY() );
			p.lineTo(size.getX()- COMPLEXCORNER, size.getY() );
			p.lineTo(size.getX(), size.getY() - COMPLEXCORNER );
			p.lineTo(size.getX(), COMPLEXCORNER );
			p.lineTo(size.getX() - COMPLEXCORNER, 0 );
			p.lineTo(COMPLEXCORNER , 0 );
			p.lineTo(0, COMPLEXCORNER );
			return p;
		}
		
		// throw new RuntimeException("Not implemented!");
		// default is ellipse 
		return new Ellipse2D.Double(0, 0, size.getX(), size.getY());
	}

	public static List<UnitOfInformationType> getUnitsOfInformation(
			SBGNNodeType node) {
		List<UnitOfInformationType> ret = new LinkedList<UnitOfInformationType>();
		for (SBGNNodeType n : node.getInnerNodes()) {
			if (n instanceof UnitOfInformationType) {
				UnitOfInformationType ui = (UnitOfInformationType) n;
				ret.add(ui);
			}
		}
		// additional cardinality node
		if (getCardinality(node) != null) {
			UnitOfInformationType card = new UnitOfInformationType();
			card.setLabel(getCardinality(node));
			card.setPrefix("N");
			ret.add(card);

		}
		return ret;
	}

	public static List<StateVariableType> getStateVars(SBGNNodeType node) {
		List<StateVariableType> ret = new LinkedList<StateVariableType>();
		for (SBGNNodeType n : node.getInnerNodes()) {
			if (n instanceof StateVariableType) {
				StateVariableType sv = (StateVariableType) n;
				ret.add(sv);
			}
		}
		return ret;
	}

	public static Point getNodeDecoratorSize(Graphics2D g2d, SBGNNodeType node) {
		int varLen = 0;
		int maxDecoH = 0;
		for (StateVariableType v : getStateVars(node)) {
			Point p = getNodeShapeSize(g2d, v);
			varLen += p.getX() + DIST_X_DECO;
			if (p.getY() > maxDecoH)
				maxDecoH = (int) p.getY();
		}
		for (UnitOfInformationType u : getUnitsOfInformation(node)) {
			Point p = getNodeShapeSize(g2d, u);
			varLen += p.getX() + DIST_X_DECO;
			if (p.getY() > maxDecoH)
				maxDecoH = (int) p.getY();
		}
		if (varLen != 0) {
			varLen += DIST_X_DECO + 2 * DECOSTART_X;
		}
		return new Point(varLen, maxDecoH);
	}

	public static Point getNodeShapeSize(Graphics2D g2d, SBGNNodeType node) {
		if (node instanceof AuxiliaryUnitType) {
			Point p = getTextLabelSize(g2d, node);
			Point ret = new Point((int) p.getX() + INSET_X_DECO, (int) p.getY()
					+ INSET_Y_DECO);
			return ret;
		}
		Point pLabel = new Point (0,0);
		if (! (node instanceof ComplexType) ) {
			pLabel = getTextLabelSize(g2d, node);
			pLabel.x += INSET_X_NODE;
		}

		Point pAux = getNodeDecoratorSize(g2d, node);
		if (pAux.getX() > pLabel.getX()) {
			pLabel.x = (int) pAux.getX();
		}

		if (getStateVars(node).size() != 0
				|| getUnitsOfInformation(node).size() != 0) {
			pLabel.x += DIST_X_DECO;
		}

		if (node instanceof ComplexType) {
			
			ArrangeComplex ac = new ArrangeComplex((ComplexType)node, g2d);
			pLabel.y+=DISTANCE_Y_COMPLEX + ac.height;
			pLabel.x+=INSET_X_COMPLEX * 2 + ac.width;
		}

		pLabel.y += INSET_Y_NODE + pAux.getY() / 2;

		if (node instanceof SimpleChemicalType) {  // force round Simple chemical
			int m = Math.max(pLabel.x, pLabel.y);
			pLabel.x = pLabel.y= m;
		}
		
		return pLabel;
	}

	public static void setFont(Graphics2D g2d, SBGNNodeType node) {
		if (node instanceof AuxiliaryUnitType) {
			g2d.setFont(decoFont);
		} else {
			g2d.setFont(labelFont);
		}

	}

	public static void OutText(Rectangle r, String s, Graphics2D g2d,
			SBGNNodeType node) {
		Font f = g2d.getFont();
		setFont(g2d, node);
		Color exist = g2d.getColor();
		g2d.setColor(getNodeColor(node.getGraphic()));

		Rectangle2D r1 = g2d.getFont().getStringBounds(getPaintingLabel(node),
				g2d.getFontRenderContext());

		int y = (int) (r.getY() + (r.getHeight() + r1.getHeight()) / 2);
		int x = (int) (r.getX() + (r.getWidth() - r1.getWidth()) / 2);

		g2d.drawString(s, x, y);

		g2d.setFont(f);
		g2d.setColor(exist);

	}

	public static Point getTextLabelSize(Graphics2D g2d, SBGNNodeType node) {
		Font f = g2d.getFont();
		setFont(g2d, node);
		Rectangle2D r = labelFont.getStringBounds(getPaintingLabel(node), g2d
				.getFontRenderContext());

		g2d.setFont(f);
		Point p = new Point((int) r.getWidth(), (int) r.getHeight());
		return p;
	}

	public static void DrawSyncSource(Graphics2D g2d, EntityPoolNodeType epn) {
		Color line = getBorderColor(epn.getGraphic());
		Color bg = getNodeBgColor(epn.getGraphic());
		g2d.setBackground(bg);
		g2d.setColor(line);

		g2d.setFont(labelFont);
		g2d.drawOval(0, 0, SYNCSIZE, SYNCSIZE);
		g2d.drawLine(SYNCSIZE, 0, 0, SYNCSIZE);
		
		lastPoint.x = SYNCSIZE; 
		lastPoint.y = SYNCSIZE; 

	}

	public static String DrawNode(SBGNNodeType n) {
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);

		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// Ask the test to render into the SVG Graphics2D implementation.
		if (n instanceof SinkType || n instanceof SourceType) {
			DrawSyncSource(svgGenerator,  (EntityPoolNodeType)n);
		} else if ((n instanceof EntityPoolNodeType)
				|| (n instanceof AuxiliaryUnitType || n instanceof TagType) ) {
			DrawNode(svgGenerator,  n);
		} else {
			throw new RuntimeException(
					"Unimplemented: Cannot render the given type");
		}

		// Finally, stream out SVG to the standard output using
		// UTF-8 encoding.
		boolean useCSS = true; // we want to use CSS style attributes
		try {
			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			Writer out = new OutputStreamWriter(boas, "UTF-8");
			svgGenerator.stream(out, useCSS);
			boas.close();
			return boas.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

}
