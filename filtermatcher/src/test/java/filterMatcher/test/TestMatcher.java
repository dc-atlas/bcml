package filterMatcher.test;

import org.testng.annotations.Test;

import com.miravtech.filterMatcher.Matcher;
import com.miravtech.sbgn.FindingType;
import com.miravtech.sbgn.OrganismEnum;
import com.miravtech.sbgn.OrganismPartEnum;

public class TestMatcher	 {
	
	@Test
	public void testFinding()
	{
		FindingType f = new FindingType();
		f.getOrganism().add(OrganismEnum.HOMO_SAPIENS);
		f.getOrganism().add(OrganismEnum.MUS_MUSCULUS);
		f.getOrganismPart().add(OrganismPartEnum.BONE_MARROW);
		assert(Matcher.contains(null,f,"organism", "'Homo sapiens'"));
		assert(!Matcher.contains(null,f,"organism", "'Homo sapiens1'"));
		assert(Matcher.contains(null, f,"cellType", "'test'"));

	}
	
}
