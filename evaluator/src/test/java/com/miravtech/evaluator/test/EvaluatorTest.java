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

package com.miravtech.evaluator.test;

import java.io.ByteArrayInputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.miravtech.sbgn.CellTypeEnum;
import com.miravtech.sbgn.FindingType;
import com.miravtech.sbgn.OrganismEnum;
import com.miravtech.sbgn.OrganismPartEnum;
import com.miravtech.sbgn.filter.evaluator.FilteringLexer;
import com.miravtech.sbgn.filter.evaluator.FilteringParser;

public class EvaluatorTest {

	@BeforeClass
	public void setup() {

	}

	
	private boolean evaluate(String expr, FindingType f) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(expr.getBytes());
		ANTLRInputStream input = new ANTLRInputStream(bais);
		FilteringLexer lexer = new FilteringLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FilteringParser parser = new FilteringParser(tokens);
		parser.toEval = f;
		try {
			boolean ret = parser.expr();
			return ret;
		} catch (RuntimeException e) {
			throw new Exception("Error parsing the condition: "+e.getMessage());
		}
		//System.out.println("Return is: "+ret);
	}
	
	@Test
	public void evaluateTest() throws Exception {
		
		FindingType f = new FindingType();
		f.getOrganism().add(OrganismEnum.HOMO_SAPIENS);
		f.getOrganism().add(OrganismEnum.MUS_MUSCULUS);
		f.getOrganismPart().add(OrganismPartEnum.BONE_MARROW);
		f.getCellType().add(CellTypeEnum.DENDRITIC_CELLS_DC);
		
		assert(evaluate("organism='Homo sapiens'",f));
		assert(!evaluate("cellType='Test'",f));
		assert(evaluate("cellType='Dendritic cells (DC)'",f));
		assert(!evaluate("organism='Homo sapienss'",f));
		assert(!evaluate("organism='Homo sapienss' and organism='Homo sapiens'",f));
		assert(evaluate("organism='Homo sapienss' or organism='Homo sapiens'",f));
		assert(evaluate("organism='Homo sapiens' and organismPart='Bone Marrow'",f));
		assert(evaluate("( organism='Homo sapiens' or organism='Homo sapiensss')  and organismPart='Bone Marrow' ",f));
		assert(!evaluate("( organism='Homo sapiens' or organism='Homo sapiensss')  and not organismPart='Bone Marrow' ",f));

		assert(!evaluate(" organism='Homo sapiens' == organismPart='Bone MarrowA' ",f));
		assert(evaluate(" organism='Homo sapiens' == organismPart='Bone Marrow' ",f));
		assert(evaluate(" organism='Homo sapiensAD' == organismPart='Bone MarrowBB' ",f));

		try {
			evaluate("organism='Homo sapiens' and organismPart='Bone Marrow",f);
			assert false; //Must have been failed the test!
		} catch (Exception e) {
			
		}
		try {
			evaluate("organism='Homo sapiens' and ",f);
			assert false; //Must have been failed the test!
		} catch (Exception e) {
			
		}

		try {
			evaluate("organism='Homo sapiens' and not ",f);
			assert false; //Must have been failed the test!
		} catch (Exception e) {
			
		}

	}

}
