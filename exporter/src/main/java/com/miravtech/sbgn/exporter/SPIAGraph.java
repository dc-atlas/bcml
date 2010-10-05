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

package com.miravtech.sbgn.exporter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.miravtech.SBGNUtils.SBGNIterator;
import com.miravtech.SBGNUtils.SBGNUtils;
import com.miravtech.sbgn.EntityPoolNodeType;
import com.miravtech.sbgn.LogicalOperatorNodeType;
import com.miravtech.sbgn.ModulationArcType;
import com.miravtech.sbgn.ProcessType;
import com.miravtech.sbgn.ProductionArcType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDL1Type;

public class SPIAGraph extends Graph<SBGNNodeType, SpiaGraphRelation> {

	public SPIAGraph(final SBGNPDL1Type s) {
		try {
			final SBGNUtils utils = new SBGNUtils(s);
			utils.fillRedundantData();
			new SBGNIterator() {
				@Override
				public void iterateNode(SBGNNodeType n) {
					if (n instanceof EntityPoolNodeType || n instanceof LogicalOperatorNodeType) {
						// EntityPoolNodeType sepnt = (EntityPoolNodeType) n;
						nodes.add(n);
					}
				}
			}.run(s);

			new SBGNIterator() {
				@Override
				public void iterateNode(SBGNNodeType n) {
					if (n instanceof ProcessType) {
						Set<SBGNNodeType> from =  utils.getRelatedNodes(n, ProductionArcType.class);
						Set<SBGNNodeType> to =  utils.getRelatedNodes(n, ProductionArcType.class);
						Set<SBGNNodeType> cat =  utils.getRelatedNodes(n, ModulationArcType.class);
						
						from.addAll(cat);
						for (SBGNNodeType f: from)
							for (SBGNNodeType t: to) {
								String type = n.getClass().getSimpleName();
								type = type.replaceAll("Type", "");
								edges.add(new SpiaGraphRelation(f,t,type));
							}
					}
					
					/*
					if (n instanceof LogicalOperatorNodeType) {
						LogicalOperatorNodeType l = (LogicalOperatorNodeType) n;
						SBGNNodeType to = utils.getOutNodeOfLogic(l);
						ArcType a =  utils.getOutArcOfLogic(l);
						for (SBGNNodeType f: utils.getInNodesOfLogic(l)) {
							String type = a.getClass().getSimpleName();
							type = type.replaceAll("Type", "");
							edges.add(new SpiaGraphRelation(f,to,type));
							
						}
					}
					*/
					
				}
			}.run(s);
			init();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	Set<SBGNNodeType> nodes = new HashSet<SBGNNodeType>();
	Set<SpiaGraphRelation> edges = new HashSet<SpiaGraphRelation>();


	
	/**
	 * 
	 * Determines if the nodes n1 and n2 are identical except of the default state variable
	 * If they are not identical, returns null.
	 * If they are identical, it returns the reaction type (phosphorilation, etc.) 
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static String getTransformationType(SBGNNodeType n1, SBGNNodeType n2) {
		//TODO
		return null;
	}
	
	@Override
	public SBGNNodeType getEnd(SpiaGraphRelation e) {
		return e.getTo();
	}

	@Override
	public Set<SBGNNodeType> getNodes() {
		return nodes;
	}

	@Override
	public SBGNNodeType getStart(SpiaGraphRelation e) {
		return e.getFrom();
	}

	@Override
	public Set<SpiaGraphRelation> getEdges() {
		return edges; 
	}

	@Override
	public Collection<SpiaGraphRelation> getTo(SBGNNodeType node) {
		Set<SpiaGraphRelation> ret = new HashSet<SpiaGraphRelation>();
		for (SpiaGraphRelation s : edges)
			if (s.getFrom().equals(node))
				ret.add(s);
		return ret;
	}

}
