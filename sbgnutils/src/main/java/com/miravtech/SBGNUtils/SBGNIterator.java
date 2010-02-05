package com.miravtech.SBGNUtils;

import java.util.LinkedList;
import java.util.List;

import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.SBGNGlyphType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDl1;

abstract public class SBGNIterator {

	public List<SBGNNodeType> stack = new LinkedList<SBGNNodeType>();

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

	public void run(SBGNPDl1 n) {
		for (SBGNNodeType nt : n.getGlyphs()) {
			stack.add(nt);
			run(nt);
			stack.remove(nt);
		}
	}

	public void run(SBGNGlyphType g) {

		iterateGlyph(g);
		if (g instanceof SBGNNodeType) {
			SBGNNodeType n = (SBGNNodeType) g;
			iterateNode(n);
			for (SBGNNodeType in : n.getInnerNodes()) {
				stack.add(in);
				run(in);
				stack.remove(in);
			}
			for (ArcType a : n.getArcs()) {
				run(a);
			}
		}
		if (g instanceof ArcType) {
			ArcType n = (ArcType) g;
			iterateArc(n);
		}
	}

	public void iterateGlyph(SBGNGlyphType n) {
	}

	public void iterateNode(SBGNNodeType n) {
	}

	public void iterateArc(ArcType n) {
	}

}
