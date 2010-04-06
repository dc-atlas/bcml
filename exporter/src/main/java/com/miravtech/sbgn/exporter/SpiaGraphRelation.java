package com.miravtech.sbgn.exporter;

import com.miravtech.sbgn.SBGNNodeType;

/**
 * 
 * The relation between two SBGN Nodes
 * 
 */
public class SpiaGraphRelation {
	private SBGNNodeType from;
	private SBGNNodeType to;
	private String reaction;

	public SpiaGraphRelation(SBGNNodeType from, SBGNNodeType to, String reaction) {
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

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

}
