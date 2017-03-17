
import java.util.Set;

/*
 * An interface that describes the methods of a directed weighted graph
 */
public interface DirectedWeightedGraph<NodeDataType, EdgeDataType> {

	/*
	 * Returns the weight of the edge, or null if the edge is not in the graph.
	 */
	public EdgeDataType getEdge(NodeDataType srcNode, NodeDataType dstNode);

	/*
	 * Returns true if there is an edge from one node to another
	 */
	public boolean adjacent(NodeDataType srcNode, NodeDataType dstNode);

	/*
	 * Modifies the graph to add a new edge,will not add an edge from a node to
	 * itself Returns true if the edge was added to the graph
	 */
	public boolean addEdge(NodeDataType srcNode, NodeDataType dstNode, EdgeDataType edge);

	/*
	 * Removes an edge, and returns true if the edge was removed from the graph
	 */
	public EdgeDataType removeEdge(NodeDataType srcNode, NodeDataType dstNode);

	/*
	 * returns a set that contains all the nodes in the graph
	 */
	public Set<NodeDataType> getNodes();

	/*
	 * Returns true if the graph contains a node with the specified data
	 */
	public boolean containsNode(NodeDataType nodeData);

	/*
	 * Returns a set of nodes that are adjacent to the given node
	 */
	public Set<NodeDataType> neighbors(NodeDataType srcNode);

	/*
	 * Adds a new node to the graph, returns true if the node was added returns
	 * false if the node was already in the graph.
	 */
	public boolean addNode(NodeDataType nodeData);

	/*
	 * Removes a node. Returns true if the node was removed.
	 */
	public boolean removeNode(NodeDataType nodeData);

}
