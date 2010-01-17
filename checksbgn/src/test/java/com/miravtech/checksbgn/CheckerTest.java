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
		// for (CheckReport r: ret)
		// System.out.println(r.toString());

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

	// TODO test ERROR_INVALID_CLONE_REF

	// TODO ERROR_EQUIVALENCE_NODE_MUST_LINK_A_TAG

	// TODO SUBMAP_CAN_ONLY_CONTAIN_LABELS

	// TODO NODE_CANNOT_CONTAIN_OTHER_GLYPHS

	// TODO COMPARTMENTS_CANNOT_BE_CONTAINED

	// TODO PROCESSES_CANNOT_CONTAIN_OTHER_NODES

}
