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

package com.miravtech.SBGNUtils;

import java.util.LinkedList;
import java.util.List;

import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.SBGNGlyphType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDL1Type;

abstract public class SBGNIterator {

	public List<SBGNNodeType> stack = new LinkedList<SBGNNodeType>();

	boolean iterateBefore = true;
	
	public SBGNNodeType getLastNode() {
		if (stack.size() <= 1)
			return null;
		return stack.get(stack.size() - 2);
	}

	public SBGNNodeType getCurrentNode() {
		if (stack.size() == 0)
			return null;
		return stack.get(stack.size() - 1);
	}

	public void run(SBGNPDL1Type n) {
		for (SBGNNodeType nt : n.getGlyphs()) {
			stack.add(nt);
			run(nt);
			stack.remove(nt);
		}
	}

	public void runBottomUp(SBGNPDL1Type g) {
		iterateBefore = false;
		run(g);
		iterateBefore = true;
	}
	public void run(SBGNGlyphType g) {

		if (iterateBefore)
			iterateGlyph(g);
		if (g instanceof SBGNNodeType) {
			SBGNNodeType n = (SBGNNodeType) g;
			if (iterateBefore)
				iterateNode(n);
			for (SBGNNodeType in : n.getInnerNodes()) {
				stack.add(in);
				run(in);
				stack.remove(in);
			}
			for (ArcType a : n.getArcs()) {
				run(a);
			}
			if (!iterateBefore)
				iterateNode(n);
		}
		if (g instanceof ArcType) {
			ArcType n = (ArcType) g;
			iterateArc(n);
		}
		if (!iterateBefore)
			iterateGlyph(g);
	}

	public void iterateGlyph(SBGNGlyphType n) {
	}

	public void iterateNode(SBGNNodeType n) {
	}

	public void iterateArc(ArcType n) {
	}

}
