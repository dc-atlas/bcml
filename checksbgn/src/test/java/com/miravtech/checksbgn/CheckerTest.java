package com.miravtech.checksbgn;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.miravtech.checksbgn.CheckReport.ERRORCODES;

public class CheckerTest {

	@BeforeClass
	public void setup() {

	}

	@Test
	public void TestInvalidSchema() throws JAXBException, SAXException {
		Checker c = new Checker();
		List<CheckReport> ret = c.check(CheckerTest.class
				.getResource("/invalidschema.xml"));
		// System.out.println(ret.get(0));

		if (ret.size() != 1)
			throw new RuntimeException("One error was expected!");
		if (ret.get(0).code != ERRORCODES.ERROR_SAX_PARSING)
			throw new RuntimeException("Sax parsing error expected!");
	}

	@Test
	public void TestReplicateID() throws JAXBException, SAXException {
		Checker c = new Checker();
		List<CheckReport> ret = c.check(CheckerTest.class
				.getResource("/replicatedID.xml"));
		if (ret.size() != 1)
			throw new RuntimeException("One error was expected!");
		if (ret.get(0).code != ERRORCODES.ERROR_DUPLICATE_ID)
			throw new RuntimeException("ERROR_DUPLICATE_ID error expected!");
	}

	@Test
	public void TestOK() throws JAXBException, SAXException {
		Checker c = new Checker();
		List<CheckReport> ret = c.check(CheckerTest.class
				.getResource("/dectin1.xml"));
		for (CheckReport r : ret)
			System.out.println(r.toString());

		if (ret.size() != 0)
			throw new RuntimeException("No error was expected!");
	}

	@Test
	public void TestInvalidCloneRef() throws JAXBException, SAXException {
		Checker c = new Checker();
		List<CheckReport> ret = c.check(CheckerTest.class
				.getResource("/invalidcloneref.xml"));
		if (ret.size() != 1)
			throw new RuntimeException("One error was expected!");
		if (ret.get(0).code != ERRORCODES.ERROR_INVALID_CLONE_REF)
			throw new RuntimeException(
					"ERROR_INVALID_CLONE_REF error expected!");

	}

	@Test
	public void TestInvalidCloneRefType() throws JAXBException, SAXException {
		Checker c = new Checker();
		List<CheckReport> ret = c.check(CheckerTest.class
				.getResource("/invalidclonereftype.xml"));
		if (ret.size() != 1)
			throw new RuntimeException("One error was expected!");
		if (ret.get(0).code != ERRORCODES.ERROR_DIFFERENT_CLONE_TYPES)
			throw new RuntimeException(
					"ERROR_INVALID_CLONE_REF error expected!");
	}

	/**
	 * Test for a bug found, no checking for nodes with empty ID
	 * 
	 * @throws JAXBException
	 * @throws SAXException
	 */
	@Test
	public void TestInvalidCloneRefType2() throws JAXBException, SAXException {
		Checker c = new Checker();
		List<CheckReport> ret = c.check(CheckerTest.class
				.getResource("/invalidclonereftype2.xml"));
		if (ret.size() != 2)
			throw new RuntimeException("Two errors was expected!");
	}

	// TODO
	@Test
	public void InvalidEquivalenceNode() throws JAXBException, SAXException {
		Checker c = new Checker();
		List<CheckReport> ret = c.check(CheckerTest.class
				.getResource("/InvalidEquivalenceArc.xml"));
		if (ret.size() != 2)
			throw new RuntimeException("One error was expected!");
		if (ret.get(0).code != ERRORCODES.ERROR_EQUIVALENCE_ARC_MUST_LINK_A_TAG)
			throw new RuntimeException(
					"ERROR_EQUIVALENCE_ARC_MUST_LINK_A_TAG error expected!");
		if (ret.get(1).code != ERRORCODES.ERROR_EQUIVALENCE_ARC_MUST_LINK_A_TAG)
			throw new RuntimeException(
					"ERROR_EQUIVALENCE_ARC_MUST_LINK_A_TAG error expected!");

	}
	// TODO SUBMAP_CAN_ONLY_CONTAIN_LABELS
	// TODO NODE_CANNOT_CONTAIN_OTHER_GLYPHS
	// TODO COMPARTMENTS_CANNOT_BE_CONTAINED
	// TODO PROCESSES_CANNOT_CONTAIN_OTHER_NODES
	// TODO INVALID_NODE_CONTAINED_IN_EPN
	// TODO NON_COMPLEX_EPN_CANNOT_CONTAIN_EPN
	// TODO ERRORCODES.ERROR_SINK_HAS_ONLY_PRODUCTION
	// TODO ERRORCODES.ERROR_SOURCE_HAS_ONLY_CONSUMPTION
	// TODO ERRORCODES.ERROR_NUCLEIC_ACID_CANNOT_BE_CATALYST
	// TODO ERRORCODES.ERROR_PROCESS_CANNOT_HAVE_LOGIC_OR_EQUIVALENCE_ARCS
	// TODO ERRORCODES.ERROR_NOT_NODES_SHOULDHAVE2ARCS_AND_ONE_LOGIC
	// TODO ERRORCODES.INVALID_LOGICAL_OPERATOR_NODE_CONNECTIONS
	// TODO ERRORCODES.INVALID_CONNECTIONS_ON_TAG_NODE
	// TODO THIS_PROCESS_MUST_HAVE_AT_LEAST_ONE_CONSUMPTION_AND_ONE_PRODUCTION

}
