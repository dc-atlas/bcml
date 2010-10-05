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

public class SPIAGeneGraph extends  Graph<String, GeneGeneRel> {
	
	
	Set<String> nodes = new HashSet<String>();
	Set<GeneGeneRel> edges = new HashSet<GeneGeneRel>();

	public SPIAGeneGraph() {
		//init();
	}
	
	public void init() {
		super.init();
	}
	
	@Override
	public String getEnd(GeneGeneRel e) {
		return e.getTo();
	}
	
	@Override
	public Set<String> getNodes() {
		return nodes;
	}
	
	@Override
	public String getStart(GeneGeneRel e) {
		return e.getFrom();
	}
	

	@Override
	public Collection<GeneGeneRel> getTo(String node) {
		Set<GeneGeneRel> ret = new HashSet<GeneGeneRel>();
		for (GeneGeneRel s : edges)
			if (s.getFrom().equals(node))
				ret.add(s);
		return ret;
	}
	
	
	@Override
	public Set<GeneGeneRel> getEdges() {
		return edges;
	}
	
	
}
