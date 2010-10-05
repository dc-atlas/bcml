/**
 *
 * Copyright (C) 2010 Razvan Popovici <rp@miravtech.com>
 * Copyright (C) 2010 Luca Beltrame <luca.beltrame@unifi.it>
 * Copyright (C) 2010 Enrica Calura <enrica.calura@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
