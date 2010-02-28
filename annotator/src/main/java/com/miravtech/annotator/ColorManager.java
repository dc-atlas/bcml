package com.miravtech.annotator;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Provides utilities to convert numbers to a color, given
 * a predefined positive and negative number color assignment.
 * 
 * Eliminates the n% extremes from both sides.
 * 
 */
public class ColorManager {

	public ColorManager() {
		
	}

	public ColorManager(Collection<Double> values) {
		setValues(values);
	}
	
	private double eliminationPercentile  = 5d;
	
	public double getEliminationPercentile() {
		return eliminationPercentile;
	}
	
	public void setEliminationPercentile(double eliminationPercentile) {
		this.eliminationPercentile = eliminationPercentile;
	}
	
	public void setValues(Collection<Double> values) {
		Vector<Double> positive = new Vector<Double>(); 
		Vector<Double> negative = new Vector<Double>();
		for (Double d: values) {
			if (d > 0)
				positive.add(d);
			else
				negative.add(d);
		}
		
		Collections.sort(positive);
		Collections.sort(negative);
		
		// remove 5% of all
		int removeat = (int)Math.ceil((double)positive.size() * (100 - eliminationPercentile) / 100);
		List<Double> newpos = positive.subList(0, removeat);
		removeat = (int)Math.floor((double)negative.size() * eliminationPercentile / 100);
		List<Double> newneg = negative.subList(removeat, negative.size());
		if (newpos.size() == 0)
			newpos.add(0.0);
		if (newneg.size() == 0)
			newneg.add(0.0);
		
		posMaxFC = Collections.max(newpos);
		posMinFC = Collections.min(newpos);
		negMaxFC = Collections.max(newneg);
		negMinFC = Collections.min(newneg);
	}

	private double negMinFC, negMaxFC;
	private double posMinFC, posMaxFC;

	private Color colMaxFC , colMinFC,
			colZeroNegFC  , colZeroPosFC;
	private Color colZero;
	
	public void loadColors(ColorManager source) {
		colZeroPosFC = source.colZeroPosFC;
		colMaxFC = source.colMaxFC;
		colMinFC = source.colMinFC;
		colZeroNegFC = source.colZeroNegFC;
		colZero = source.colZero;
	}
	
	public Color getColor(double fc) {
		if (fc == 0)
			return colZero;
		if (fc > 0)
			return getColor(fc, posMinFC, posMaxFC, colZeroPosFC, colMaxFC);
		else
			return getColor(fc, negMinFC, negMaxFC, colMinFC, colZeroNegFC);
	}

	public static Color getMostContrastantColor(Color background, Color choice1, Color choice2) {
		
		double db = ColorManager.getDistance(background, choice1);
		double dw = ColorManager.getDistance(background, choice2);
		if (db > dw)
			return choice1;
		else
			return choice2;
	}
	
	
	public static double getDistance(Color c1, Color c2) {
		return Math.sqrt(pow2(c1.getRed() - c2.getRed()) + pow2(c1.getGreen() - c2.getGreen()) + pow2(c1.getBlue() - c2.getBlue()) );
	}
	
	public static double pow2(double d) {
		return d*d;
	}
	
	public static Color getColor(double v, double from, double to, Color cfrom,
			Color cto) {
		int r = (int) rescale(v, from, to, cfrom.getRed(), cto.getRed());
		int g = (int) rescale(v, from, to, cfrom.getGreen(), cto.getGreen());
		int b = (int) rescale(v, from, to, cfrom.getBlue(), cto.getBlue());
		return new Color(r, g, b);
	}

	public static double rescale(double v, double from, double to, double cfrom,
			double cto) {
		if (v < from)
			v = from;
		if (v > to)
			v = to;
		return cfrom + (cto - cfrom) * (v - from) / (to - from);
	}



	public Color getColMaxFC() {
		return colMaxFC;
	}

	public void setColMaxFC(Color colMaxFC) {
		this.colMaxFC = colMaxFC;
	}

	public Color getColMinFC() {
		return colMinFC;
	}

	public void setColMinFC(Color colMinFC) {
		this.colMinFC = colMinFC;
	}



	public Color getColZeroNegFC() {
		return colZeroNegFC;
	}

	public void setColZeroNegFC(Color colZeroNegFC) {
		this.colZeroNegFC = colZeroNegFC;
	}

	public Color getColZeroPosFC() {
		return colZeroPosFC;
	}

	public void setColZeroPosFC(Color colZeroPosFC) {
		this.colZeroPosFC = colZeroPosFC;
	}

	public double getNegMinFC() {
		return negMinFC;
	}

	public double getNegMaxFC() {
		return negMaxFC;
	}

	public double getPosMinFC() {
		return posMinFC;
	}

	public double getPosMaxFC() {
		return posMaxFC;
	}

	public Color getColZero() {
		return colZero;
	}

	public void setColZero(Color colZero) {
		this.colZero = colZero;
	}
	
}
