package com.miravtech.sbgn.exporter;

public class GeneGeneRel {
	private String from;
	private String to;
	private String reaction;

	public GeneGeneRel(String from, String to, String reaction) {
		super();
		this.from = from;
		this.to = to;
		this.reaction = reaction;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

}
