// === CS400 File Header Information ===
// Name: Cyrus Kianian
// Email: kianian@wisc.edu
// Group and Team: P2.1703 black (I think)
// Lecturer: Gary Dahl
// Notes to Grader: :)

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new PlaceholderMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method represents
   * the end of the shortest path that is found: it's cost is the cost of that shortest path, and
   * the nodes linked together through predecessor references represent all of the nodes along that
   * shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    if (!this.containsNode(start) || !this.containsNode(end)) {
      throw new NoSuchElementException("Start or end node DNE in the graph");
    }

    PriorityQueue<SearchNode> queue = new PriorityQueue<>();//PQ to sort edges cost
    PlaceholderMap<Node, Double> visitedSet = new PlaceholderMap<>();//visited nodes + cost

    queue.add(new SearchNode(nodes.get(start), 0, null)); //adds start to pq

    while (!queue.isEmpty()) {
      SearchNode curr = queue.remove();//fastest path

      if (curr.node.data == end) return curr;//reached the end node

      visitedSet.put(curr.node, curr.cost);//adding smallest val to visited set
      for (Edge edge : curr.node.edgesLeaving) {//for all edges of removed node see if new/better path
        if (!visitedSet.containsKey(edge.successor)) {//ensures not already visited, add to pq
          queue.add(new SearchNode(edge.successor, (curr.cost + edge.data.doubleValue()), curr));
        }
      }
    }

    throw new NoSuchElementException("No path exists");//didn't find a path
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    List<NodeType> path = new ArrayList<>();
    try {
      SearchNode curr = this.computeShortestPath(start, end);
      path.add(end);//add end
      while (curr.predecessor != null) {//go until first node of the path
        curr = curr.predecessor;
        path.add(curr.node.data);
      }
      Collections.reverse(path);//flip the list to become start to end node
      return path;
    } catch (
        NoSuchElementException e) {//if computeShortestPath throws any exception, path/nodes DNE
      throw new NoSuchElementException(e);
    }
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path from the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    try {
      SearchNode lastNode = this.computeShortestPath(start, end);
      return lastNode.cost;//returns total cost
    } catch (NoSuchElementException e) {//path or nodes DNE, throw same error
      throw new NoSuchElementException(e);
    }
  }

  /**
   * makes use of an example in lecture, first one on gary's slides with the ??? edge = 5
   */
  @Test
  public void test1() {
    //create graph, nodes, and edges
    DijkstraGraph<Character, Integer> test = new DijkstraGraph<>();
    test.insertNode('A');
    test.insertNode('B');
    test.insertNode('C');
    test.insertNode('D');
    test.insertNode('E');
    test.insertNode('F');
    test.insertEdge('A', 'B', 1);
    test.insertEdge('B', 'F', 5);
    test.insertEdge('A', 'C', 2);
    test.insertEdge('C', 'D', 2);
    test.insertEdge('D', 'E', 2);
    test.insertEdge('E', 'F', 2);

    double expected = 1;
    double actual = test.shortestPathCost('A', 'B');
    //ensures the expected path was taken
    Assertions.assertEquals(expected, actual, "Shortest path is not correct weight");

    List<Character> expectedPath = new ArrayList<>();
    expectedPath.add('A');
    expectedPath.add('B');
    List<Character> actualPath = test.shortestPathData('A', 'B');
    //checks that the nodes on the shortest path match what is expected
    Assertions.assertEquals(expectedPath, actualPath, "Did not match expected path");

  }

  /**
   * tests different nodes in the graph from lecture, this time follows the same path as in lecture
   */
  @Test
  public void test2() {
    //create graph, nodes, and edges
    DijkstraGraph<Character, Integer> test = new DijkstraGraph<>();
    test.insertNode('A');
    test.insertNode('B');
    test.insertNode('C');
    test.insertNode('D');
    test.insertNode('E');
    test.insertNode('F');
    test.insertEdge('A', 'B', 1);
    test.insertEdge('B', 'F', 5);
    test.insertEdge('A', 'C', 2);
    test.insertEdge('C', 'D', 2);
    test.insertEdge('D', 'E', 2);
    test.insertEdge('E', 'F', 2);

    double expected = 6;
    double actual = test.shortestPathCost('A', 'E');
    //ensures the expected path was taken
    Assertions.assertEquals(expected, actual, "Shortest path is not correct weight");

    List<Character> expectedPath = new ArrayList<>();
    expectedPath.add('A');
    expectedPath.add('C');
    expectedPath.add('D');
    expectedPath.add('E');
    List<Character> actualPath = test.shortestPathData('A', 'E');
    //checks that the nodes on the shortest path match what is expected
    Assertions.assertEquals(expectedPath, actualPath, "Did not match expected path");

  }

  /**
   * uses the graph from lecture and checks the behavior when 2 nodes are not connected
   */
  @Test
  public void test3() {
    //create graph, nodes, and edges
    DijkstraGraph<Character, Integer> test = new DijkstraGraph<>();
    test.insertNode('A');
    test.insertNode('B');
    test.insertNode('C');
    test.insertNode('D');
    test.insertNode('E');
    test.insertNode('F');
    test.insertEdge('A', 'B', 1);
    test.insertEdge('B', 'F', 5);
    test.insertEdge('A', 'C', 2);
    test.insertEdge('C', 'D', 2);
    test.insertEdge('D', 'E', 2);
    test.insertEdge('E', 'F', 2);

    try {
      test.computeShortestPath('C', 'B');
      Assertions.fail("Path was found between unconnected nodes");
      //fail, found a path between unconnected nodes
    } catch (NoSuchElementException e) {
      //works, didn't find a path
    }

  }
}
