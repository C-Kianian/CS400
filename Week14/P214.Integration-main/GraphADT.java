import java.util.List;
import java.util.NoSuchElementException;

/**
 * This ADT represents a directed graph data structure with only positive edge 
 * weights. Duplicate node values are not allowed.
 *
 * @param NodeType is the data type stored at each graph node
 * @param EdgeType is the numeric data type stored at each graph edge, with a 
 * doubleValue() method that always returns a value >0.0
 */
public interface GraphADT<NodeType, EdgeType extends Number> {

  /**
   * Insert a new node into the graph.
   *
   * @param data is the data item stored in the new node
   * @return true if the data is unique and can be inserted into a new node, 
   *         or false if this data is already in the graph
   * @throws NullPointerException if data is null
   */
  public boolean insertNode(NodeType data);

  /**
   * Remove a node from the graph. And also remove all edges adjacent to that 
   * node.
   *
   * @param data is the data item stored in the node to be removed
   * @return true if a vertex with data is found and removed, or false if that 
   *         data value is not found in the graph
   * @throws NullPointerException if data is null
   */
  public boolean removeNode(NodeType data);

  /**
   * Check whether the graph contains a node with the provided data.
   *
   * @param data the node contains to check for
   * @return true if data item is stored in a node within the graph, or false 
   *         otherwise
   */
  public boolean containsNode(NodeType data);

  /**
   * Retrieves a list of all node data from this graph.
   *
   * @return list of all node data
   */
  public List<NodeType> getAllNodes();
    
  /**
   * Return the number of nodes in the graph.
   *
   * @return the number of nodes in the graph
   */
  public int getNodeCount();

  /**
   * Insert a new directed edge with positive edges weight into the graph. Or 
   * if an edge between pred and succ already exists, update the data stored 
   * in that edge with the new weight.
   *
   * @param pred is the data item contained in the new edge's predecesor node
   * @param succ is the data item contained in the new edge's successor node
   * @param weight is the positive valued data item stored in the new edge
   * @return true if the edge could be inserted or updated, or false if the 
   *         pred or succ data are not found in any graph nodes or if the 
   *         weight specified was not a positive value >0.0
   */
  public boolean insertEdge(NodeType pred, NodeType succ, EdgeType weight);

  /**
   * Remove an edge from the graph.
   *
   * @param pred the data item contained in the source node for the edge
   * @param succ the data item contained in the target node for the edge
   * @return true if the edge could be removed, or false if such an edge is 
   *         not found in the graph
   */
  public boolean removeEdge(NodeType pred, NodeType succ);

  /**
   * Check if edge is in the graph.
   *
   * @param pred the data item contained in the source node for the edge
   * @param succ the data item contained in the target node for the edge
   * @return true if the edge is found in the graph, or false other
   */
  public boolean containsEdge(NodeType pred, NodeType succ);

  /**
   * Return the data associated with a specific edge.
   *
   * @param pred the data item contained in the source node for the edge
   * @param succ the data item contained in the target node for the edge
   * @return the non-negative data from the edge between those nodes
   * @throws NoSuchElementException if either node or the edge between them 
   *         are not found within this graph
   */
  public EdgeType getEdge(NodeType pred, NodeType succ);

  /**
   * Return the number of edges in the graph.
   *
   * @return the number of edges in the graph
   */
  public int getEdgeCount();

  /**
   * Returns the list of data values from nodes along the shortest path from 
   * the node with the provided start value through the node with the provided
   * end value. This list of data values starts with the start value, ends with
   * the end value, and contains intermediary values in the order they are 
   * encountered while traversing this shortest path. This method uses 
   * Dijkstra's shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   * @throws NoSuchElementException if either the start or end node cannot
   *         be found in the graph, or if there is no directed path from the
   *         start node to the end node
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end);

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path
   * from the node containing the start data to the node containing the end 
   * data. This method uses Dijkstra's shortest path algorithm to find this 
   * solution.
   *
   * @param start the data item in the starting node for the path
   * @param end the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   * @throws NoSuchElementException if either the start or end node cannot
   *         be found in the graph, or if there is no directed path from the
   *         start node to the end node
   */
  public double shortestPathCost(NodeType start, NodeType end);
    
}
