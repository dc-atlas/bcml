package com.miravtech.sbgn.graphics;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import com.miravtech.sbgn.ComplexType;
import com.miravtech.sbgn.EntityPoolNodeType;
import com.miravtech.sbgn.SBGNNodeType;

public class ArrangeComplex extends ArrangeAlgorithm<SBGNNodeType> {

	ArrangeComplex(ComplexType c, Graphics2D g2d) {
		Map<SBGNNodeType, Point> init = new HashMap<SBGNNodeType, Point>();
		
		for (SBGNNodeType nt : c.getInnerNodes())
			if (nt instanceof EntityPoolNodeType) {
				Point p = PaintNode.getNodeShapeSize(g2d, nt);
				p.setLocation(p.getX() + PaintNode.DISTANCE_X_COMPLEX, p.getY() + PaintNode.DISTANCE_Y_COMPLEX);
				init.put(nt, p);
			}

		compute(init);
	}
}
