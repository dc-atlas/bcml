package com.miravtech.checksbgn;

import org.xml.sax.SAXParseException;

class CheckReport {
	String message;
	Integer line, col;
	ERRORCODES code;

	public enum ERRORCODES {
		ERROR_GENERIC("%s"), // generic
		ERROR_SAX_PARSING("%s"), // sax error
		ERROR_DUPLICATE_ID("The id %s occurs multiple times."), //
		ERROR_INVALID_REF("The id %s specified as arc refNode is invalid."), //
		ERROR_INVALID_CLONE_REF(
				"The id %s specified as clone template was not found."), //
		ERROR_CYCLIC_CLONE_DEF(
				"The id %s specified as clone template is a cyclic definition."), //
		ERROR_DIFFERENT_CLONE_TYPES(
				"The id %s is a clone of %s, but they have different types."), //
		ERROR_ARCHS_MUST_CONNECT_NODES("An arch is connecting the non-node %s."), //
		ERROR_EQUIVALENCE_ARC_MUST_LINK_A_TAG(
				"The id %s and %s are connected with an Equivalence arc, but none of them is a Tag (page 40, 3.4.1. Node Connectivity)"), //
		ERROR_LOGIC_ARC_MUST_LINK_A_LOGIC_OPERATOR(
				"The id %s and %s are connected with an Logic arc, but none of them is a Logical operator (page 40, 3.4.1. Node Connectivity)"), // 
		ERROR_ARC_MUST_LINK_AN_EPN_TO_PROCESS(
				"The connection between %s and %s must contain exacly one process and one EPN (page 40, 3.4.1. Node Connectivity)"), //
		SUBMAP_CAN_ONLY_CONTAIN_LABELS(
				"The submap id %s can only contain labels, but contains %s. (2.7.1 Glyph: Submap page 21)"), //
		NODE_CANNOT_CONTAIN_OTHER_GLYPHS(
				"The node id %s can only contain labels, but contains %s. "), //
		COMPARTMENTS_CANNOT_BE_CONTAINED(
				"Compartiments cannot be contained. Node %s contains %s which is a container. (Figure2.20 page 23)"), //
		PROCESSES_CANNOT_CONTAIN_OTHER_NODES(
				"Process %s contains the node %s but can only contain clone marker. (2.8 Process nodes page 25)"), //
		INVALID_NODE_CONTAINED_IN_EPN(
				"EPN %s illegaly contains the node %s. (3.4.2 Containment definition)"), //
		NON_COMPLEX_EPN_CANNOT_CONTAIN_EPN(
				"non-complex EPN %s illegaly contains the EPN %s. (3.4.2 Containment definition)"), ERROR_SINK_HAS_ONLY_PRODUCTION(
				"The sink %s must only contain production arcs."), //
		ERROR_SOURCE_HAS_ONLY_CONSUMPTION(
				"The source %s must only contain consumption arcs."), //
		ERROR_NUCLEIC_ACID_CANNOT_BE_CATALYST(
				"The nucleic acid feature %s cannot have a catalist arch."), //
		ERROR_PROCESS_CANNOT_HAVE_LOGIC_OR_EQUIVALENCE_ARCS(
				"Process %s cannot be connected by logic or equivalence arcs"), //
		ERROR_NOT_NODES_SHOULDHAVE2ARCS_AND_ONE_LOGIC(
				"\"NOT\" Node %s must be connected by two arcs, one of them logic"), //
		INVALID_LOGICAL_OPERATOR_NODE_CONNECTIONS(
				"Logical node %s must be connected to at least three arcs, two of them logical."), //
		INVALID_CONNECTIONS_ON_TAG_NODE(
				"Tag node %s may only have one equivalence arc."), //
		THIS_PROCESS_MUST_HAVE_AT_LEAST_ONE_CONSUMPTION_AND_ONE_PRODUCTION(""), //

		;

		private final String error;

		public String getError() {
			return error;
		}

		ERRORCODES(String e) {
			error = e;
		}
	};

	public CheckReport(ERRORCODES c, Object... data) {
		code = c;
		message = String.format(c.getError(), data);
	}

	public CheckReport(SAXParseException e) {
		line = e.getLineNumber();
		col = e.getColumnNumber();
		message = e.getMessage();
		code = ERRORCODES.ERROR_SAX_PARSING;
	}

	public CheckReport(Exception e) {
		message = e.toString();
		code = ERRORCODES.ERROR_GENERIC;

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(code);
		sb.append(" ");
		sb.append(message);
		if (line != null) {
			sb.append(" Line:");
			sb.append(line);
		}
		if (col != null) {
			sb.append(" Column:");
			sb.append(col);
		}
		return sb.toString();
	}

}