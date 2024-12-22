import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This BaseGraph class contains stores a set of nodes, along with a set of
 * directed and weighted edges connecting those nodes.
 */
public class BaseGraph<NodeType, EdgeType extends Number> {

    // Each node contains unique data along with two lists of directed edges
    protected class Node {
        public NodeType data;
        public List<Edge> edgesLeaving = new LinkedList<>();
        public List<Edge> edgesEntering = new LinkedList<>();

        public Node(NodeType data) {
            this.data = data;
        }
    }

    // Nodes can be retrieved from this map by their unique data
    protected MapADT<NodeType, Node> nodes = null;

    // Each edge contains data/weight, and two nodes that it connects
    protected class Edge {
        public EdgeType data; // the weight or cost of this edge
        public Node predecessor;
        public Node successor;

        public Edge(EdgeType data, Node pred, Node succ) {
            this.data = data;
            this.predecessor = pred;
            this.successor = succ;
        }
    }

    protected int edgeCount = 0;
    // Edges can be retrieved through the edge lists in either connected node

    /**
     * Constructor for BaseGraph that provides the map the graph uses.
     * 
     * @param map the map the graph uses to map a data object to the node object
     *        it is stored in
     */
    public BaseGraph(MapADT<NodeType, Node> map) {
        this.nodes = map;
    }

    /**
     * Insert a new node into the graph.
     * 
     * @param data is the data item stored in the new node
     * @return true if the data is unique and can be inserted into a new node,
     *         or false if this data is already in the graph
     * @throws NullPointerException if data is null
     */
    public boolean insertNode(NodeType data) {
        if (nodes.containsKey(data))
            return false; // throws NPE when data's null
        nodes.put(data, new Node(data));
        return true;
    }

    /**
     * Remove a node from the graph.
     * And also remove all edges adjacent to that node.
     * 
     * @param data is the data item stored in the node to be removed
     * @return true if a vertex with data is found and removed, or
     *         false if that data value is not found in the graph
     * @throws NullPointerException if data is null
     */
    public boolean removeNode(NodeType data) {
        // remove this node from nodes collection
        if (!nodes.containsKey(data))
            return false; // throws NPE when data==null
        Node oldNode = nodes.remove(data);
        // remove all edges entering neighboring nodes from this one
        for (Edge edge : oldNode.edgesLeaving)
            edge.successor.edgesEntering.remove(edge);
        // remove all edges leaving neighboring nodes toward this one
        for (Edge edge : oldNode.edgesEntering)
            edge.predecessor.edgesLeaving.remove(edge);
        return true;
    }

    /**
     * Check whether the graph contains a node with the provided data.
     * 
     * @param data the node contents to check for
     * @return true if data item is stored in a node within the graph, or
     *         false otherwise
     */
    public boolean containsNode(NodeType data) {
        return nodes.containsKey(data);
    }

    /**
     * Retrieves a list of all node data from this graph.
     *
     * @return list of all node data
     */
    public List<NodeType> getAllNodes(){
        return nodes.getKeys();
    }

    /**
     * Return the number of nodes in the graph
     * 
     * @return the number of nodes in the graph
     */
    public int getNodeCount() {
        return nodes.getSize();
    }

    /**
     * Insert a new directed edge with positive edges weight into the graph.
     * Or if an edge between pred and succ already exists, update the data
     * stored in hat edge to be weight.
     * 
     * @param pred   is the data item contained in the new edge's predecesor node
     * @param succ   is the data item contained in the new edge's successor node
     * @param weight is the non-negative data item stored in the new edge
     * @return true if the edge could be inserted or updated, or
     *         false if the pred or succ data are not found in any graph nodes
     */
    public boolean insertEdge(NodeType pred, NodeType succ, EdgeType weight) {
        // find nodes associated with node data, and return false when not found
        Node predNode = nodes.get(pred);
        Node succNode = nodes.get(succ);
        if (predNode == null || succNode == null)
            return false;
        try {
            // when an edge alread exists within the graph, update its weight
            Edge existingEdge = getEdgeHelper(pred, succ);
            existingEdge.data = weight;
        } catch (NoSuchElementException e) {
            // otherwise create a new edges
            Edge newEdge = new Edge(weight, predNode, succNode);
            this.edgeCount++;
            // and insert it into each of its adjacent nodes' respective lists
            predNode.edgesLeaving.add(newEdge);
            succNode.edgesEntering.add(newEdge);
        }
        return true;
    }

    /**
     * Remove an edge from the graph.
     * 
     * @param pred the data item contained in the source node for the edge
     * @param succ the data item contained in the target node for the edge
     * @return true if the edge could be removed, or
     *         false if such an edge is not found in the graph
     */
    public boolean removeEdge(NodeType pred, NodeType succ) {
        try {
            // when an edge exists
            Edge oldEdge = getEdgeHelper(pred, succ);
            // remove it from the edge lists of each adjacent node
            oldEdge.predecessor.edgesLeaving.remove(oldEdge);
            oldEdge.successor.edgesEntering.remove(oldEdge);
            // and decrement the edge count before removing
            this.edgeCount--;
            return true;
        } catch (NoSuchElementException e) {
            // when no such edge exists, return false instead
            return false;
        }
    }

    /**
     * Check if edge is in the graph.
     * 
     * @param pred the data item contained in the source node for the edge
     * @param succ the data item contained in the target node for the edge
     * @return true if the edge is found in the graph, or false other
     */
    public boolean containsEdge(NodeType pred, NodeType succ) {
        try {
            getEdgeHelper(pred, succ);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Return the data associated with a specific edge.
     * 
     * @param pred the data item contained in the source node for the edge
     * @param succ the data item contained in the target node for the edge
     * @return the non-negative data from the edge between those nodes
     * @throws NoSuchElementException if either node or the edge between them
     *                                are not found within this graph
     */
    public EdgeType getEdge(NodeType pred, NodeType succ) {
        return getEdgeHelper(pred, succ).data;
    }

    protected Edge getEdgeHelper(NodeType pred, NodeType succ) {
        Node predNode = nodes.get(pred);
        // search for edge through the predecessor's list of leaving edges
        for (Edge edge : predNode.edgesLeaving)
            // compare succ to the data in each leaving edge's successor
            if (edge.successor.data.equals(succ))
                return edge;
        // when no such edge can be found, throw NSE
        throw new NoSuchElementException("No edge from " + pred.toString() + " to " +
                succ.toString());
    }

    /**
     * Return the number of edges in the graph.
     * 
     * @return the number of edges in the graph
     */
    public int getEdgeCount() {
        return this.edgeCount;
    }

}

