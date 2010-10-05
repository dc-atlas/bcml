/**
 *
 * Copyright (C) 2010 Razvan Popovici <rp@miravtech.com>
 * Copyright (C) 2010 Luca Beltrame <luca.beltrame@unifi.it>
 * Copyright (C) 2010 Enrica Calura <enrica.calura@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package filterMatcher.test;

import org.testng.annotations.Test;

import com.miravtech.filterMatcher.Matcher;
import com.miravtech.sbgn.FindingType;
import com.miravtech.sbgn.OrganismEnum;
import com.miravtech.sbgn.OrganismPartEnum;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.StatefulEntiyPoolNodeType;

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
	
	@Test
	public void testMatcherMacromodule() {
		SBGNNodeType n = new StatefulEntiyPoolNodeType();
		n.getMacroModule().add("mm1");
		n.getMacroModule().add("mm2");
		n.getMacroModule().add("mm3");

		assert(!Matcher.contains(n,null,"Macromodule", "'m1'"));
		assert(Matcher.contains(n,null,"Macromodule", "'mm1'"));
		
	}
	
}
