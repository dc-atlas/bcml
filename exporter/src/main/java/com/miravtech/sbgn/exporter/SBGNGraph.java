package com.miravtech.sbgn.exporter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.miravtech.SBGNUtils.SBGNIterator;
import com.miravtech.SBGNUtils.SBGNUtils;
import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.AuxiliaryUnitType;
import com.miravtech.sbgn.CompartmentType;
import com.miravtech.sbgn.ConsumptionArcType;
import com.miravtech.sbgn.InhibitionArcType;
import com.miravtech.sbgn.ModulationArcType;
import com.miravtech.sbgn.ProcessType;
import com.miravtech.sbgn.ProductionArcType;
import com.miravtech.sbgn.SBGNNodeType;
import com.miravtech.sbgn.SBGNPDL1Type;
import com.miravtech.sbgn.SelectType;
import com.miravtech.sbgn.StateVariableType;

/**
 * Graph based on nodes and edges of the SBGN diagram
 * 
 * @author Razvan
 * 
 */
public class SBGNGraph extends Graph<SBGNNodeType, SBGNGraphRelation> {
	private SBGNUtils utils;
	
	public Set<ArcType> procArc = new HashSet<ArcType>();
	public SBGNGraph(final SBGNPDL1Type s) {
		try {
			utils = new SBGNUtils(s);
			utils.fillRedundantData();
			new SBGNIterator() {
				@Override
				public void iterateNode(SBGNNodeType n) {
					if (!(n instanceof CompartmentType ) && ! (n instanceof  AuxiliaryUnitType) ) {
						nodes.add(n);
						
						for (ArcType a : utils.getEdges(n)) 
							if (!procArc.contains(a))
							{
								SBGNNodeType target = utils.getOtherNode(a, n);
								edges.add(new SBGNGraphRelation(n,target,a));
								edges.add(new SBGNGraphRelation(target,n,a));
								procArc.add(a);
							}
					}
				}
				
			}.run(s);

			init();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	Set<SBGNNodeType> nodes = new HashSet<SBGNNodeType>();
	Set<SBGNGraphRelation> edges = new HashSet<SBGNGraphRelation>();

	@Override
	public SBGNNodeType getEnd(SBGNGraphRelation e) {
		return e.getTo();
	}

	@Override
	public Set<SBGNNodeType> getNodes() {
		return nodes;
	}

	@Override
	public SBGNNodeType getStart(SBGNGraphRelation e) {
		return e.getFrom();
	}

	@Override
	public Set<SBGNGraphRelation> getEdges() {
		return edges;
	}

	@Override
	public Collection<SBGNGraphRelation> getTo(SBGNNodeType node) {
		Set<SBGNGraphRelation> ret = new HashSet<SBGNGraphRelation>();
		for (SBGNGraphRelation s : edges)
			if (s.getFrom().equals(node))
				ret.add(s);
		return ret;
	}

	/**
	 * Removes the node and it's adjacent connections
	 * @param n the node
	 */
	public void eraseNode(SBGNNodeType n) {
		nodes.remove(n);		
		Set<SBGNGraphRelation> toRemove = new HashSet<SBGNGraphRelation>();
			for (SBGNGraphRelation r: edges) 
				if ( r.getFrom() == n || r.getTo() == n ) 
					toRemove.add(r);
		nodes.removeAll(toRemove);
	}
	
	
	Map<SBGNGraphRelation, Set<String>> inferredRelNames = new HashMap<SBGNGraphRelation, Set<String>>();
	
	
	/**
	 *  Determine catalyzed reactions and their actual type.
	 * Fills inferredRelNames.
	 */
	public void adaptCatalystReactions() {
		for (SBGNNodeType n : nodes)
			if (n instanceof ProcessType) {
				Set< SBGNGraphRelation> prod = new HashSet<SBGNGraphRelation>();
				Set< SBGNGraphRelation> cons = new HashSet<SBGNGraphRelation>();
				Set< SBGNGraphRelation> catalyst = new HashSet<SBGNGraphRelation>();
				for (SBGNGraphRelation r : getFrom(n)) {
					if (r.getReaction() instanceof ModulationArcType) {
						catalyst.add(r);
					}
					if (r.getReaction() instanceof ConsumptionArcType) {
						cons.add(r);
					}
					if (r.getReaction() instanceof ProductionArcType)
						prod.add(r);
				}
				
				if (catalyst.size() != 0) {
					// get differences between Prod and Cons
					Set<String> prodVar = new HashSet<String>();
					Set<String> consVar = new HashSet<String>();
					for (SBGNGraphRelation g : prod) {
						SBGNNodeType nd = g.getFrom();
						for (StateVariableType v :  SBGNUtils.getNodeVariables(nd).values()) {
							if (v.getVariable() == null || v.getVariable().length() == 0) {
								// default var
								prodVar.add(v.getLabel().trim().toUpperCase());
							}
						}
					}
					for (SBGNGraphRelation g : cons) {
						SBGNNodeType nd = g.getFrom();
						for (StateVariableType v :  SBGNUtils.getNodeVariables(nd).values()) {
							if (v.getVariable() == null || v.getVariable().length() == 0) {
								// default var
								consVar.add(v.getLabel().trim().toUpperCase());
							}
						}
					}
					// remove common default variables
					prodVar.remove("");
					consVar.remove("");
					prodVar.removeAll(consVar);
					consVar.removeAll(prodVar);
					// determine the name[s]
					Set<String> rels = new HashSet<String>();
					for (String s: prodVar)
						rels.add(getRelationName(s, 1));
					for (String s: consVar)
						rels.add(getRelationName(s, -1));
							
					if (rels.size() != 0) {
						rels.add("");
					}
					disconnect(n); // remove all p's connections
					// move the cathalysis relation from the process to the nodes
					for (SBGNGraphRelation c : catalyst) 
						for (SBGNGraphRelation p : prod) 
						{
							// "brandish" the cathalysis arcs with the computed relation name
							SBGNGraphRelation r1 = new SBGNGraphRelation(c.getFrom(), p.getFrom(), c.getReaction());
							Set<String> names = new HashSet<String>();
							for (String s1: rels)
								names.add(getRelationNameByActivityType(s1, c.getReaction() instanceof InhibitionArcType?-1:1));
							inferredRelNames.put(r1,names); 
							edges.add(r1);
						}
				}
			}
	}
	
	
	public String organism, db;
	public boolean usefilter;
	
	public SPIAGeneGraph toSPIAGeneGraph(String organism, String db,
			final boolean usefilter) {
	
		this.organism = organism;
		this.db = db;
		this.usefilter = usefilter;
		
		CheckSymbolsMatcher matcher = new CheckSymbolsMatcher();
		
		SPIAGeneGraph g = new SPIAGeneGraph();
		Set<SBGNNodeType> processed = new HashSet<SBGNNodeType>();
		for (SBGNNodeType n : nodes)
			if (! processed.contains(n) && matcher.matches(n)) {
				Set<String> symbols =  SBGNUtils.getSymbols(organism, db, usefilter, n);
				processed.add(n);
				g.getNodes().addAll(symbols);
				g.init();
				// use radiosity for each node 
				Map<SBGNNodeType, List<SBGNGraphRelation>>  conns = radiosity(n, matcher);
				for (Entry<SBGNNodeType, List<SBGNGraphRelation>> e : conns.entrySet()) {
					if (processed.contains(e.getKey()))
						continue; // already handled
					
					Set<String> symbolsto =  SBGNUtils.getSymbols(organism, db, usefilter, e.getKey());
					for (String s : getDominantRelation(e.getValue())) 
						for (String fr :symbols) 
							for (String to :symbolsto) 
								g.getEdges().add(new GeneGeneRel(fr, to, s));
				}
			}
		
		return g;
	}
	
	// determine which connection is more powerful : 1. mapped connections, 2: modulated, 3: default connections
	public Set<String> getDominantRelation(List<SBGNGraphRelation> rels) {
		for (SBGNGraphRelation r : rels)
			if (inferredRelNames.containsKey(r))
				return inferredRelNames.get(r);
		Set<String> ret = new HashSet<String>();
		ret.add("activation");
		return ret;

		
	}
	
	
	public String getRelationNameByActivityType(String name, int act_inh) {
		String ret = name;
		if (name.length() != 0 && act_inh == -1)
			ret = name+"_inhibition";
		if (name.length() == 0)
			if (act_inh == -1)
				ret = "inhibition";
			else
				ret = "activation";
		return ret;
	}
	
	public String getRelationName(String var, int dir) {
		if (var.equals("P") && dir == 1) {
			return "phosphorylation";
		}
		if (var.equals("P") && dir == -1) {
			return "dephosphorylation";
		}
		if (var.equals("UB") && dir == 1) {
			return "ubiquination";
		}
		if (var.equals("UB") && dir == -1) {
			return "ueubiquination";
		}
		if (var.equals("AC") && dir == 1) {
			return "acetilation";
		}
		if (var.equals("AC") && dir == -1) {
			return "deacetilation";
		}
		if (var.equals("ACTIVE") && dir == 1) {
			return "activation";
		}
		if (var.equals("ACTIVE") && dir == -1) {
			return "deactivation";
		}
		if (var.equals("INACTIVE") && dir == -1) {
			return "activation";
		}
		if (var.equals("INACTIVE") && dir == 1) {
			return "deactivation";
		}
		// unknown, improvise!!
		if (dir == 1)
			return var.toLowerCase();
		else
			return "De-"+var.toLowerCase();
	}
	
	/**
	 * Delete consumption and production arcs (because they get re-created by the identities of the participants)
	 */
	public void deleteProdCons() {
		Set<SBGNGraphRelation> toRem2 = new HashSet<SBGNGraphRelation>();
		for (SBGNGraphRelation r : edges) 
			if (r.getReaction() instanceof ProductionArcType || r.getReaction() instanceof ConsumptionArcType ) 
				toRem2.add(r);
		edges.removeAll(toRem2);
		init();

	}
	
	class CheckSymbolsMatcher implements Matcher<SBGNNodeType> {
	@Override
	public boolean matches(SBGNNodeType n) {
		return SBGNUtils.getSymbols(organism, db, usefilter, n).size() != 0;
	}	
	}
	
	
	/**
	 *  Remove all nodes filtered by the user query (this can broke connections)
	 */
	public void applyFilter() {
		// remove excluded nodes
		Set<SBGNNodeType> toRem = new HashSet<SBGNNodeType>();
		for (SBGNNodeType n : nodes) 
			if (SelectType.EXCLUDE == n.getSelected())
				toRem.add(n);
		for (SBGNNodeType n : toRem) 
			eraseNode(n);
	
		// remove excluded edges
		Set<SBGNGraphRelation> toRem2 = new HashSet<SBGNGraphRelation>();
		for (SBGNGraphRelation n : edges) 
			if (SelectType.EXCLUDE == n.getReaction().getSelected())
				toRem2.add(n);
		edges.removeAll(toRem2);
		init();
	}
	
	
	/**
	 * Removes the node but keeps the connection according to the rules
	 * @param N
	 */
	public void ReduceNode(SBGNNodeType N) {
		
	}

	
	public void disconnect(SBGNNodeType n) {
		Set<SBGNGraphRelation> toRemove = new HashSet<SBGNGraphRelation>();
		for (SBGNGraphRelation r: edges) 
				if ( (r.getFrom() ==  n || r.getTo() ==  n) ) 
					toRemove.add(r);
		edges.removeAll(toRemove);
	}

		
	
	/**
	 * Merges the node n1 into n2
	 * 
	 * @param n1 the merged node, it will be removed from the graph
	 * @param n2 the node will take over all the connection of n1.
	 * 
	 */
	public void MergeNodes(SBGNNodeType n1, SBGNNodeType n2) {
		nodes.remove(n1);
		// remove n1->n2 and n2->n1
		Set<SBGNGraphRelation> toRemove = new HashSet<SBGNGraphRelation>();
		for (SBGNGraphRelation r: edges) 
				if ( (r.getFrom() == n1 && r.getTo() == n2) || (r.getFrom() == n2 && r.getTo() == n1) ) 
					toRemove.add(r);
		edges.removeAll(toRemove);

		// replace X->n1 with X->n2 and n1->Y with n2->Y
		for (SBGNGraphRelation r: edges) {
			if ( r.getFrom() == n1  ) 
				r.setFrom(n2);
			if ( r.getTo() == n1  ) 
				r.setTo(n2);
			
		}

	}

}
