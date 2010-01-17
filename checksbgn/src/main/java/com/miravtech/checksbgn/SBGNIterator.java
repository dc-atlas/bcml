package com.miravtech.checksbgn;

import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.SBGNGlyphType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDl1;

abstract class SBGNIterator {

	public void run(SBGNPDl1 n ) {
		for(SBGNNodeType nt: n.getGlyphs())
			run(nt);
	}
	
	public void run(SBGNGlyphType g) {
		iterateGlyph(g);
		if (g instanceof SBGNNodeType) {
			SBGNNodeType n = (SBGNNodeType) g;
			iterateNode(n);
			for (SBGNNodeType in : n.getInnerNodes()) {
				run(in);
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
