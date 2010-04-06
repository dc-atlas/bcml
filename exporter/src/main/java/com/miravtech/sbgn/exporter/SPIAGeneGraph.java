package com.miravtech.sbgn.exporter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.miravtech.sbgn.SBGNNodeType;

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
