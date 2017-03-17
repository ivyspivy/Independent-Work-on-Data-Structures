
import java.util.*;

public class AdjacencyList<NodeDataType, EdgeDataType> implements DirectedWeightedGraph<NodeDataType, EdgeDataType> {

	/*
	 * Adjacency class. This was written by the CS professors of Harvey Mudd
	 * College. Methods were written by Ivy Chen, comments written in the
	 * interface also by Ivy Chen.
	 */
	private class Adjacency {
		NodeDataType destination;
		EdgeDataType data;

		public Adjacency(NodeDataType destination, EdgeDataType data) {
			this.destination = destination;
			this.data = data;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((data == null) ? 0 : data.hashCode());
			result = prime * result + ((destination == null) ? 0 : destination.hashCode());
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Adjacency other = (Adjacency) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (data == null) {
				if (other.data != null) {
					return false;
				}
			} else if (!data.equals(other.data)) {
				return false;
			}
			if (destination == null) {
				if (other.destination != null) {
					return false;
				}
			} else if (!destination.equals(other.destination)) {
				return false;
			}
			return true;
		}

		private AdjacencyList<NodeDataType, EdgeDataType> getOuterType() {
			return AdjacencyList.this;
		}
	}

	Map<NodeDataType, Collection<Adjacency>> theGraph = new HashMap<NodeDataType, Collection<Adjacency>>();

	@Override
	public EdgeDataType getEdge(NodeDataType srcNode, NodeDataType dstNode) {
		if (this.theGraph.containsKey(srcNode)) {
			Collection<AdjacencyList<NodeDataType, EdgeDataType>.Adjacency> test = theGraph.get(srcNode);
			for (Adjacency a : test) {
				if (a.destination.equals(dstNode)) {
					return a.data;
				}
			}
		}
		return null;
	}

	@Override
	public boolean adjacent(NodeDataType srcNode, NodeDataType dstNode) {
		if (this.theGraph.containsKey(srcNode)) {
			Collection<AdjacencyList<NodeDataType, EdgeDataType>.Adjacency> test = theGraph.get(srcNode);
			for (Adjacency a : test) {
				if (a.destination.equals(dstNode)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean addEdge(NodeDataType srcNode, NodeDataType dstNode, EdgeDataType edge) {
		if (srcNode.equals(dstNode)) {
			return false;
		}
		if (!this.theGraph.containsKey(srcNode)) {
			this.addNode(srcNode);
		}
		if (!this.theGraph.containsKey(dstNode)) {
			this.addNode(dstNode);
		}
		Collection<AdjacencyList<NodeDataType, EdgeDataType>.Adjacency> list = theGraph.get(srcNode);
		for (Adjacency a : list) {
			if (a.destination.equals(dstNode)) {
				if (a.data.equals(edge)) {
					return false;
				}
			}
		}
		list.add(new Adjacency(dstNode, edge));
		return true;
	}

	@Override
	public EdgeDataType removeEdge(NodeDataType srcNode, NodeDataType dstNode) {
		EdgeDataType e = null;
		if (this.theGraph.containsKey(srcNode)) {
			Collection<AdjacencyList<NodeDataType, EdgeDataType>.Adjacency> list = theGraph.get(srcNode);
			for (Adjacency a : list) {
				if (a.destination.equals(dstNode)) {
					e = a.data;
				}
			}
			list.remove(new Adjacency(dstNode, e));

		}
		return e;
	}

	@Override
	public Set<NodeDataType> getNodes() {
		Set<NodeDataType> s = new HashSet<NodeDataType>();
		for (NodeDataType n : this.theGraph.keySet()) {
			if (!s.contains(n)) {
				s.add(n);
			}
			for (Adjacency a : this.theGraph.get(n)) {
				if (!s.contains(a.destination)) {
					s.add(a.destination);
				}
			}
		}
		return s;
	}

	@Override
	public boolean containsNode(NodeDataType nodeData) {
		return this.theGraph.containsKey(nodeData) || this.theGraph.containsValue(nodeData);
	}

	@Override
	public Set<NodeDataType> neighbors(NodeDataType srcNode) {
		Set<NodeDataType> s = new HashSet<NodeDataType>();
		if (this.theGraph.get(srcNode) != null) {
			for (Adjacency n : this.theGraph.get(srcNode)) {
				s.add(n.destination);
			}
		}
		return s;
	}

	@Override
	public boolean addNode(NodeDataType nodeData) {
		if (this.theGraph.keySet().contains(nodeData)) {
			return false;
		}
		this.theGraph.put(nodeData, new ArrayList<Adjacency>());
		return true;
	}

	@Override
	public boolean removeNode(NodeDataType nodeData) {
		if (!this.theGraph.keySet().contains(nodeData)) {
			return false;
		}
		this.theGraph.remove(nodeData);
		return true;
	}

}
