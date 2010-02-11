package com.miravtech.sbgn.graphics;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JFrame;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class PaintNode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("FrameDemo");
		frame.add(new Component() {			
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g1 = (Graphics2D)g;
				g1.translate(100, 100);
				DrawSyncSource((Graphics2D)g);
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(500, 500);
		frame.setVisible(true);

	}

	public final static int SYNCSIZE = 40;

	public static void DrawSyncSource(Graphics2D g2d) {
		g2d.drawOval(0, 0, SYNCSIZE , SYNCSIZE);
		g2d.drawLine(SYNCSIZE, 0, 0 , SYNCSIZE);
	}

	public static String DrawSyncSource() {
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);

		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// Ask the test to render into the SVG Graphics2D implementation.
		DrawSyncSource(svgGenerator);

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
