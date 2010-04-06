package com.miravtech.sbgn.exporter;

import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.SBGNNodeType;

/**
 * 
 * The relation between two SBGN Nodes
 * 
 */
public class SBGNGraphRelation {
	private SBGNNodeType from;
	private SBGNNodeType to;
	private ArcType reaction;

	public SBGNGraphRelation(SBGNNodeType from, SBGNNodeType to, ArcType reaction) {
		super();
		this.from = from;
		this.to = to;
		this.reaction = reaction;
	}

	public SBGNNodeType getFrom() {
		return from;
	}

	public void setFrom(SBGNNodeType from) {
		this.from = from;
	}

	public SBGNNodeType getTo() {
		return to;
	}

	public void setTo(SBGNNodeType to) {
		this.to = to;
	}

	public ArcType getReaction() {
		return reaction;
	}

	public void setReaction(ArcType reaction) {
		this.reaction = reaction;
	}

}
