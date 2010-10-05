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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * 
 * @author Razvan Popovici
 * 
 * @param <N>
 *            the nodes
 * @param <E>
 *            the edges
 */
abstract public class Graph<N, E> {

	static private Logger log = Logger.getLogger(Graph.class);

	public Graph() {
	}

	/**
	 * To be called after the constructor is called, to initalize the stuff.
	 * 
	 */
	protected void init() {
		int i = 0;
		Set<N> nodes = getNodes();
		index.clear();
		indexref.clear();
		for (N n : nodes) {
			index.put(i, n);
			indexref.put(n, i);
			i++;
		}
		sz = nodes.size();
	}

	private Map<Integer, N> index = new HashMap<Integer, N>();
	private Map<N, Integer> indexref = new HashMap<N, Integer>();

	private int sz;

	/**
	 * 
	 * Given the edge, returns its "from" node
	 * 
	 * @param e
	 * @return
	 */
	public abstract N getStart(E e);

	/**
	 * 
	 * Given the edge, returns its "to" node
	 * 
	 * @param e
	 * @return
	 */
	public abstract N getEnd(E e);

	/**
	 * 
	 * Returns the list of nodes.
	 * 
	 * @return
	 */
	public abstract Set<N> getNodes();

	/**
	 * 
	 * Returns an edge between two components, if this exists
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public E getEdge(N from, N to) {
		for (E e : getTo(from))
			if (getEnd(e).equals(to))
				return e;
		return null;
	}

	/**
	 * 
	 * Returns a subgraph of the current graph, obtained from uniting the
	 * subgraph set.
	 * 
	 * @param gs
	 * @return
	 */
	public Graph<N, E> mergeGraphs(final Set<Graph<N, E>> gs) {
		Graph<N, E> ret = new Graph<N, E>() {
			public N getEnd(E e) {
				return Graph.this.getEnd(e);
			};

			@Override
			public Set<N> getNodes() {
				Set<N> ret = new HashSet<N>();
				for (Graph<N, E> g : gs)
					ret.addAll(g.getNodes());
				return ret;
			}

			public N getStart(E e) {
				return Graph.this.getStart(e);
			};

			public Collection<E> getTo(N node) {
				Set<E> ret = new HashSet<E>();
				for (Graph<N, E> g : gs)
					ret.addAll(g.getEdges());
				return ret;
			};
		};
		return ret;

	}

	public boolean[][] getAdjacencyMatrix() {
		boolean[][] r = new boolean[sz][sz];
		for (E e : getEdges()) {
			int f = indexref.get(getStart(e));
			int t = indexref.get(getEnd(e));
			r[f][t] = true;
		}
		return r;
	}

	public Object[][] getEdgeMatrix() {
		Object[][] r = null;
		r = new Object[sz][sz];
		for (E e : getEdges()) {
			int f = indexref.get(getStart(e));
			int t = indexref.get(getEnd(e));
			r[f][t] = e;
		}
		return r;
	}

	public void getEdgeMatrix(E[][] r) {

		for (int i = 0; i != sz; i++)
			for (int k = 0; k != sz; k++)
				r[i][k] = null;

		for (E e : getEdges()) {
			int f = indexref.get(getStart(e));
			int t = indexref.get(getEnd(e));
			r[f][t] = e;
		}
	}

	public void getNodeVector(N[] nd) {
		for (int i = 0; i != sz; i++)
			nd[i] = index.get(i);

	}

	public Object[] getNodeVector() {
		Object[] r = null;
		r = new Object[sz];
		for (int i = 0; i != sz; i++) {
			r[i] = index.get(i);
		}
		return r;
	}

	/**
	 * 
	 * Returns a list of incoming edges in the given node.
	 * 
	 * @param node
	 * @return
	 */
	public Collection<E> getFrom(N node) {
		Set<E> ret = new HashSet<E>();
		for (E e : getEdges()) {
			if (getEnd(e).equals(node))
				ret.add(e);
		}
		return ret;
	}

	/**
	 * Returns a list of outcoming edges from the given node.
	 * 
	 * @param node
	 * @return
	 */
	public abstract Collection<E> getTo(N node);

	public Set<E> getEdges() {
		Set<E> ret = new HashSet<E>();
		for (N n : getNodes())
			ret.addAll(getTo(n));
		return ret;
	}

	/**
	 * Returns a set of the given edges ends.
	 * 
	 * @param e
	 * @return
	 */
	public Collection<N> getEnd(Collection<E> e) {
		Set<N> ret = new HashSet<N>();
		for (E edge : e)
			ret.add(getEnd(edge));
		return ret;
	}

	/**
	 * returns a set of given edges starts
	 * 
	 * @param e
	 * @return
	 */
	public Collection<N> getStart(Collection<E> e) {
		Set<N> ret = new HashSet<N>();
		for (E edge : e)
			ret.add(getStart(edge));
		return ret;
	}

	/**
	 * Returns all the nodes connected to the given nodes
	 * 
	 * @param nodes
	 *            the nodes
	 * @param upper
	 *            if true - returns all nodes before the given node (they are
	 *            referred by it). if false - returns all nodes after the given
	 *            node (they refer the given node) null - returns both
	 * @return
	 */
	public Collection<N> getConnectedNodes(Collection<N> nodes, Boolean upper) {
		Collection<N> ret = new HashSet<N>();
		for (N n : nodes)
			ret.addAll(getConnectedNodes(n, upper));
		return ret;

	}

	/**
	 * Returns all the nodes connected to the given node
	 * 
	 * @param node
	 *            the node
	 * @param upper
	 *            if true - returns all nodes before the given node (they are
	 *            referred by it). if false - returns all nodes after the given
	 *            node (they refer the given node) null - returns both
	 * @return
	 */
	public Collection<N> getConnectedNodes(N node, Boolean upper) {
		Collection<N> ret;
		if (upper == null) {
			ret = getConnectedNodes(node, false);
			ret.addAll(getConnectedNodes(node, true));
			return ret;
		}
		ret = new HashSet<N>();
		Collection<N> toExplore = new HashSet<N>();
		toExplore.add(node);
		while (!toExplore.isEmpty()) {
			Collection<N> connect = getNeibourghs(toExplore, upper);
			connect.removeAll(ret);
			connect.removeAll(toExplore);
			connect.remove(node);
			toExplore = connect;
			ret.addAll(toExplore);
		}
		return ret;
	}

	/**
	 * @param node
	 * @param upper
	 *            if true - returns all nodes <B>directly connected</B> before
	 *            the given node (they are referred by it). if false - returns
	 *            all nodes <B>directly connected</B> after the given node (they
	 *            refer the given node)
	 * @return
	 */
	public Collection<N> getNeibourghs(N node, Boolean upper) {
		if (upper)
			return getStart(getFrom(node));
		else
			return getEnd(getTo(node));
	}

	/**
	 * @param nodes
	 * @param upper
	 *            if true - returns all nodes <B>directly connected</B> before
	 *            the given node (they are referred by it). if false - returns
	 *            all nodes <B>directly connected</B> after the given node (they
	 *            refer the given node)
	 * @return
	 */
	public Collection<N> getNeibourghs(Collection<N> nodes, Boolean upper) {
		Set<N> ret = new HashSet<N>();
		for (N n : nodes)
			ret.addAll(getNeibourghs(n, upper));
		return ret;
	}

//	@bogus
	public Map<N, List<E>> radiosity(N start, Matcher<N> stopCondition) {
		Map<N, List<E>> ret = new HashMap<N, List<E>>();
		ret.put(start, new LinkedList<E>());
		Map<N, List<E>> add;
		do {
			add = new HashMap<N, List<E>>();
			for (Entry<N, List<E>> e : ret.entrySet()) {
				if (!stopCondition.matches(e.getKey()) || (e.getKey()==start && e.getValue().size() ==0)) 
					for (E e1 : getTo(e.getKey())) {
						if (!ret.containsKey(getEnd(e1))) {
							List<E> newList = new LinkedList<E>();
							newList.addAll(e.getValue());
							newList.add(e1);
							add.put(getEnd(e1), newList);
						}
					}
			}
			ret.putAll(add);
		} while (add.size() > 0);
		Set<N> toRemove = new HashSet<N>();
		for (Entry<N, List<E>> e1 : ret.entrySet()) {
			if (e1.getKey() == start && e1.getValue().size() == 0)
				toRemove.add(e1.getKey());
			if (!stopCondition.matches(e1.getKey()))
				toRemove.add(e1.getKey());
		}
		for (N n1 : toRemove)
			ret.remove(n1); 
		return ret;
	}


	private void iterate(N item, Set<N> known, int d, Map<N, Integer> ret,
			int threshold) {

		// record the postion
		if (ret.containsKey(item)) {
			Integer dist = ret.get(item);
			if (dist < d)
				ret.put(item, d);
		} else
			ret.put(item, d);
		known.add(item);
		if (d == threshold)
			return; // don't go above the treshold, they will
		// be added using greedy

		for (N n : getEnd(getTo(item))) {
			if (!known.contains(n)) {
				iterate(n, known, d + 1, ret, threshold);
			}
		}
		known.remove(item);
	}

	/**
	 * Internal consistency check of the redundant functions
	 */
	public int checkEdges() {
		int errs = 0;
		log.info("Checking edges for: " + toString());
		// check edge validity
		for (E e : getEdges()) {
			if (getStart(e) == null) {
				log.error("Edge: " + e.toString() + " has a null start!");
				errs++;
			} else {
				if (!getNodes().contains(getStart(e))) {
					log.error("Edge: " + e.toString()
							+ " starts from an unlisted node!");
					errs++;
				}
			}

			if (getEnd(e) == null) {
				log.error("Edge: " + e.toString() + " has a null end!");
				errs++;
			} else {
				if (!getNodes().contains(getEnd(e))) {
					log.error("Edge: " + e.toString()
							+ " ends in an unlisted node!");
					errs++;
				}
			}

		}

		// check edge redundantly
		for (E e : getEdges()) {
			N st = getStart(e), end = getEnd(e);
			if (!getNodes().contains(st)) {
				log.error("The end node " + st + " of the edge: " + e
						+ " is not in the node list! (1)");
				errs++;
			}
			if (!getNodes().contains(end)) {
				log.error("The start node " + end + " of the edge: " + e
						+ " is not in the node list! (2)");
				errs++;
			}
			E e1 = getEdge(st, end);
			if (e1 == null) {
				log.error("The edge: " + e
						+ " fails to retrieve using getEdge (3)");
				errs++;
			}
		}

		for (N n1 : getNodes())
			for (N n2 : getNodes()) {
				E e1 = null;
				for (E e : getEdges())
					if (getStart(e).equals(n1) && getEnd(e).equals(n2))
						e1 = e;
				E e2 = getEdge(n1, n2);
				if (e1 == null && e2 == null)
					continue;
				if (e1 == null) {
					log.error("Edge " + e2 + " not found (5)");
					errs++;
					continue;
				}
				if (!e1.equals(e2)) {
					log.error("Different edges retrieved for the nodes: " + n1
							+ " and " + n2 + ". First is+" + e1 + " and second"
							+ e2 + " (4)");
					errs++;
				}
			}
		log.info("Checking edges completed for: " + toString());
		return errs;
	}

	public void iterateFrom(N toBeexplored, Set<N> explored, Set<N> far,
			Set<N> onPath, int threshold) {
		explored.add(toBeexplored);
		if (explored.size() == threshold) {
			// found one
			far.add(toBeexplored); // this is far
			onPath.addAll(explored); // they form a path
		} else {
			// still not found the end, explore what is next
			Collection<N> directTo = getNeibourghs(toBeexplored, false);
			directTo.removeAll(explored); // remove what is already known
			for (N n : directTo) {
				iterateFrom(n, explored, far, onPath, threshold);
			}
		}
		explored.remove(toBeexplored);
	}

	/**
	 * Outputs the summary of the graph.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + " " + getNodes().size() + " nodes "
				+ getEdges().size() + " edges";
	}

}
