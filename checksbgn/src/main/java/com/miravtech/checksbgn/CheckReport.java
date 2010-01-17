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
				"Process %s contains the node %s but can only contain clone marker. (2.8 Process nodes page 25)") //

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