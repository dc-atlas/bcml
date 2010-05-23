package com.miravtech.sbgn.graphics;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ArrangeAlgorithm<T> {

	static final public double MAXSIZE = 480; // yed has problems in rendering
												// larger items

	/**
	 * The list of rectangles to be arranged
	 */
	Map<T, Point> rectSize = null;

	// intermediary data
	List<Map.Entry<T, Point>> sortedByHeight = null;
	double maxElemX = 0;
	double increment;

	// results
	Map<T, Point> arrangement = null;
	double width;
	double height;

	double getRatio() {
		if (rectSize.size() == 0)
			return 1d;
		double ret = width / height;
		if (ret > 1d)
			return 1d / ret;
		else
			return ret;
	}

	public void compute(Map<T, Point> p) {
		rectSize = p;
		sortedByHeight = new LinkedList<Map.Entry<T, Point>>();
		sortedByHeight.addAll(rectSize.entrySet());
		Collections.sort(sortedByHeight, new Comparator<Map.Entry<T, Point>>() {
			@Override
			public int compare(Map.Entry<T, Point> a, Map.Entry<T, Point> b) {
				return new Double(a.getValue().getY()).compareTo(b.getValue()
						.getY());
			}
		});
		// determine the widest object
		for (Point p1 : p.values())
			if (p1.getX() > maxElemX)
				maxElemX = p1.getX();

		double bestW = maxElemX, crtW = maxElemX;
		arrangeByWidth(crtW);
		double bestR = getRatio();
		while (increment != Double.POSITIVE_INFINITY) {
			// attempt a better solution
			crtW+=increment;
			arrangeByWidth(crtW);
			if (getRatio() > bestR) {
				bestR = getRatio();
				bestW = crtW;
			}
			if (getRatio() < bestR) {
				// things become worse, just quit
				break;
			}
		}

		arrangeByWidth(bestW);
	}

	private void arrangeByWidth(double maxw) {
		width = maxElemX;
		height = 0;
		increment = Double.POSITIVE_INFINITY;
		double crtWidth = 0, crtHeight = 0;
		arrangement = new HashMap<T, Point>();
		for (Map.Entry<T, Point> e : sortedByHeight) {
			if (crtWidth + e.getValue().getX() > maxw) {
				double inc = crtWidth + e.getValue().getX() - maxw; // growth
				if (increment > inc )
					increment = inc;
				// new row
				if (crtWidth > width)
					width = crtWidth;
				crtWidth = 0;
				height += crtHeight;
			}
			Point crt = new Point();
			crt.setLocation(crtWidth, height);
			arrangement.put(e.getKey(), crt);
			crtWidth += e.getValue().getX();
			if (crtHeight < e.getValue().getY())
				crtHeight = e.getValue().getY();
		}

		// last row, update dimensions
		height += crtHeight;
		if (crtWidth > width)
			width = crtWidth;

	}
}
