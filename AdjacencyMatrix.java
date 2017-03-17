import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Adjacency Matrix class used to represent a graph. This class and the access methods were written by the professors 
 * of Harvey Mudd College. The modifying methods were written by Ivy Chen.
 */
public class AdjacencyMatrix<NodeDataType, EdgeDataType> implements DirectedWeightedGraph<NodeDataType, EdgeDataType> {

	/*********************************
	 * Private fields
	 *********************************/

	/**
	 * The graph is represented as a 2D array where each element represents an
	 * edge. For example, theGraph[3][4] contains the edge from node 3 to node
	 * 4. The type of theGraph is a 2D Object array, but we'll only store
	 * EdgeDataType objects in the array. We can't create an EdgeDataType array
	 * because you can't create arrays of types specified by generics. That is,
	 * when you create an array you have to know exactly what type it will be,
	 * which isn't the case if someone specifies the type when constructing the
	 * object like we do for NodeDataType and EdgeDataType.
	 */
	private Object[][] theGraph = new Object[0][0];

	/**
	 * allows us to convert a node's data to an array index
	 */
	private Map<NodeDataType, Integer> nodeIndexLookup = new HashMap<NodeDataType, Integer>();

	/*********************************
	 * Private helper methods
	 *********************************/

	/**
	 * @return the number of nodes in the graph (never negative)
	 */
	private int getNumNodes() {
		// Each node has one element in the nodeToIndexMap
		return this.nodeIndexLookup.size();
	}

	/**
	 * returns a edge data, given the src and dst indices
	 * 
	 * @param srcIndex
	 * @param dstIndex
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private EdgeDataType getEdge(int srcIndex, int dstIndex) {
		// We store theGraph as an Object[][] so must cast to an EdgeDataType
		return (EdgeDataType) this.theGraph[srcIndex][dstIndex];
	}

	/**
	 * sets the edge data, the src and dst indices
	 * 
	 * @param srcIndex
	 * @param dstIndex
	 * @param edge
	 */
	private void setEdge(int srcIndex, int dstIndex, EdgeDataType edge) {
		this.theGraph[srcIndex][dstIndex] = edge;
	}

	/*********************************
	 * Access Methods
	 *********************************/

	@Override
	public Set<NodeDataType> getNodes() {
		return this.nodeIndexLookup.keySet();
	}

	@Override
	public EdgeDataType getEdge(NodeDataType srcNodeData, NodeDataType dstNodeData) {
		Integer srcNodeIndex = this.nodeIndexLookup.get(srcNodeData);
		Integer dstNodeIndex = this.nodeIndexLookup.get(dstNodeData);
		if (srcNodeIndex == null || dstNodeIndex == null) {
			return null; // srcNodeData or dstNodeData not found
		}
		return this.getEdge(srcNodeIndex.intValue(), dstNodeIndex.intValue());
	}

	@Override
	public boolean containsNode(NodeDataType nodeData) {
		return this.nodeIndexLookup.containsKey(nodeData);
	}

	@Override
	public boolean adjacent(NodeDataType srcNodeData, NodeDataType dstNodeData) {
		return this.getEdge(srcNodeData, dstNodeData) != null;
	}

	@Override
	public Set<NodeDataType> neighbors(NodeDataType srcNodeData) {
		Integer currentNodeIndex = this.nodeIndexLookup.get(srcNodeData);

		if (currentNodeIndex == null) {
			return new HashSet<NodeDataType>(); // the node is not in the graph
		}

		// figure out which nodes are connected to the source node
		int numNodes = this.getNumNodes();
		boolean[] nodesConnected = new boolean[numNodes];
		for (int dstIndex = 0; dstIndex < numNodes; dstIndex++) {
			EdgeDataType edge = this.getEdge(currentNodeIndex, dstIndex);
			if (edge != null) {
				nodesConnected[dstIndex] = true;
			}
		}

		// add those nodes to the result
		Set<NodeDataType> connectedNodes = new HashSet<NodeDataType>();
		Set<NodeDataType> allNodes = this.nodeIndexLookup.keySet();
		for (NodeDataType node : allNodes) {
			int dstIndex = this.nodeIndexLookup.get(node).intValue();
			if (nodesConnected[dstIndex]) {
				connectedNodes.add(node);
			}
		}

		return connectedNodes;
	}

	/*********************************
	 * Methods to Modify the Graph
	 *********************************/

	@Override
	public boolean addNode(NodeDataType nodeData) {
		// can't add a node that is already in the graph

		if (this.containsNode(nodeData)) {
			return false;
		}
		this.nodeIndexLookup.put(nodeData, this.nodeIndexLookup.size());
		int numNodes = this.getNumNodes();
		
		Object[][] newGraph = new Object[numNodes + 1][numNodes + 1];
		
		for (int row = 0; row < theGraph.length; row++) {
			for (int col = 0; col < theGraph[0].length; col++) {
				newGraph[row][col] = this.theGraph[row][col];
			}
		}
		this.theGraph = newGraph;
		
		return true;
	}

	@Override
	public boolean removeNode(NodeDataType nodeData) {

		// we can't remove a node that's not in the graph
		if (!this.containsNode(nodeData)) {
			return false;
		}

		int numNodes = this.getNumNodes();

		// otherwise, remove the node data from our hash table ...
		int nodeIndex = this.nodeIndexLookup.get(nodeData).intValue();
		this.nodeIndexLookup.remove(nodeData);

		// ... and shrink the adjacency matrix to include one fewer node
		Object[][] newGraph = new Object[numNodes - 1][numNodes - 1];
		for (int row = 0; row < numNodes - 1; row++) {
			for (int column = 0; column < numNodes - 1; column++) {
				// calculate the src row and columns
				int srcRow = (row >= nodeIndex) ? row : row + 1;
				int srcColumn = (column >= nodeIndex) ? column : column + 1;
				// copy all of the elements over to graphTemp
				newGraph[row][column] = this.theGraph[srcRow][srcColumn];
			}
		}
		this.theGraph = newGraph;

		return true;
	}

	@Override
	public boolean addEdge(NodeDataType srcNodeData, NodeDataType dstNodeData, EdgeDataType edge) {

		// if the graph already contains an edge for these two nodes,
		// don't add a new one
		if (this.getEdge(srcNodeData, dstNodeData) != null) {
			return false;
		}

		// can't add an edge from a node to itself
		if (srcNodeData.equals(dstNodeData)) {
			return false;
		}

		// add the source and destination nodes to the graph
		this.addNode(srcNodeData);
		this.addNode(dstNodeData);
		Integer srcNodeIndex = this.nodeIndexLookup.get(srcNodeData);
		Integer dstNodeIndex = this.nodeIndexLookup.get(dstNodeData);

		// if we weren't able to add one of the nodes, return false
		if (srcNodeIndex == null || dstNodeIndex == null) {
			return false;
		}

		// otherwise, add the edge's data to the graph
		this.setEdge(srcNodeIndex.intValue(), dstNodeIndex.intValue(), edge);

		return true;
	}

	@Override
	public EdgeDataType removeEdge(NodeDataType srcNodeData, NodeDataType dstNodeData) {

		// see if there is an edge from the source to the destination
		Integer srcNodeIndex = this.nodeIndexLookup.get(srcNodeData);
		Integer dstNodeIndex = this.nodeIndexLookup.get(dstNodeData);

		// if there's no such edge, return null
		if (srcNodeIndex == null || dstNodeIndex == null) {
			return null;
		}

		// otherwise, set the corresponding edge data to null
		// (but save the data, so we can return it)
		EdgeDataType edge = this.getEdge(srcNodeIndex, dstNodeIndex);
		this.setEdge(srcNodeIndex.intValue(), dstNodeIndex.intValue(), null);
		return edge;
	}
}
