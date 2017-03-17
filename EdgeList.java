import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EdgeList<NodeDataType, EdgeDataType>
        implements DirectedWeightedGraph<NodeDataType, EdgeDataType> {

    /*
     * Edge class used to represent an edge.
     * Written by Harvey Mudd CS Professors. Comments and methods written by Ivy Chen.
     */
    private class Edge {
        NodeDataType source;
        NodeDataType destination;
        EdgeDataType data;

        private Edge(NodeDataType src, NodeDataType dst, EdgeDataType data) {
            this.source = src;
            this.destination = dst;
            this.data = data;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result
                    + ((destination == null) ? 0 : destination.hashCode());
            result = prime * result
                    + ((source == null) ? 0 : source.hashCode());
            result = prime * result + ((data == null) ? 0 : data.hashCode());
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
            Edge other = (Edge) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (destination == null) {
                if (other.destination != null) {
                    return false;
                }
            } else if (!destination.equals(other.destination)) {
                return false;
            }
            if (source == null) {
                if (other.source != null) {
                    return false;
                }
            } else if (!source.equals(other.source)) {
                return false;
            }
            if (data == null) {
                if (other.data != null) {
                    return false;
                }
            } else if (!data.equals(other.data)) {
                return false;
            }
            return true;
        }

        private EdgeList<NodeDataType, EdgeDataType> getOuterType() {
            return EdgeList.this;
        }
    }

    /*
     * ArrayList of edges to represent a graph.
     */
    private Collection<Edge> theGraph = new ArrayList<Edge>();
    /*
     * Set of all Nodes in the graph.
     */
    private Set<NodeDataType> allNodes = new HashSet<NodeDataType>();

    @Override
    public boolean addNode(NodeDataType nodeData) {
        return this.allNodes.add(nodeData);
    }

    @Override
    public boolean removeNode(NodeDataType nodeData) {    	
    	if( !this.allNodes.contains(nodeData)){
        	return false;
        }
        this.allNodes.remove(nodeData);
        //remove all the edges with the node
        Collection<Edge> newGraph = new ArrayList<Edge>();
        
        for(Edge e : theGraph){
        	if(!e.destination.equals(nodeData)&& !e.source.equals(nodeData)){
        		newGraph.add(e);
        	}
        }
        
        this.theGraph = newGraph;
        
        return true;
    }

    @Override
    public boolean containsNode(NodeDataType nodeData) {
        return this.allNodes.contains(nodeData);
    }

    @Override
    public boolean addEdge(NodeDataType srcNode, NodeDataType dstNode,
            EdgeDataType edgeData) {

        // don't add an edge from a node to itself
        if (srcNode.equals(dstNode)) {
            return false;
        }

        Edge edge = new Edge(srcNode, dstNode, edgeData);

        // don't add an edge that's already in the graph
        if (this.containsEdge(edge)) {
            return false;
        }

        this.theGraph.add(edge);
        this.allNodes.add(srcNode);
        this.allNodes.add(dstNode);
        return true;
    }

    /**
     * private helper method to determine if an edge is in the graph
     * 
     * @param edge
     * @return true if the graph contains the edge
     */
    private boolean containsEdge(Edge edge) {
        return this.theGraph.contains(edge);
    }

    @Override
    public EdgeDataType removeEdge(NodeDataType srcNode, NodeDataType dstNode) {
        EdgeDataType edgeData = this.getEdge(srcNode, dstNode);
        Edge edge = new Edge(srcNode, dstNode, edgeData);
        this.theGraph.remove(edge);
        return edgeData;
    }

    @Override
    public EdgeDataType getEdge(NodeDataType srcNode, NodeDataType dstNode) {
        for (Edge edge : this.theGraph) {
            if (edge.source.equals(srcNode)
                    && edge.destination.equals(dstNode)) {
                return edge.data;
            }
        }
        return null;
    }

    @Override
    public boolean adjacent(NodeDataType srcNode, NodeDataType dstNode) {
        return this.getEdge(srcNode, dstNode) != null;
    }

    @Override
    public Set<NodeDataType> neighbors(NodeDataType srcNode) {
        Set<NodeDataType> results = new HashSet<NodeDataType>();
        for (Edge edge : this.theGraph) {
            if (edge.source.equals(srcNode)) {
                results.add(edge.destination);
            }
        }
        return results;
    }

    @Override
    public Set<NodeDataType> getNodes() {
        return new HashSet<NodeDataType>(this.allNodes);
    }

}

