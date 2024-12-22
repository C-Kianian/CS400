import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class extends the DijsktraGraph class to run submission checks on it.
 */
public class P210SubmissionChecker extends DijkstraGraph<Integer, Integer> {

    /**
     * Creates a graph with 3 nodes and 7 edges and checks the path that
     * returned between two of the nodes based on the shortestPathData
     * and shortestPathData methods.
     */
    @Test
    public void submissionCheckerSmallGraph() {
        DijkstraGraph<Integer, Integer> graph = new DijkstraGraph<>();
        graph.insertNode(1);
        graph.insertNode(6);
        graph.insertNode(11);

        graph.insertEdge(11, 1, 3);
        graph.insertEdge(6, 11, 3);
        graph.insertEdge(1, 11, 5);
        graph.insertEdge(6, 6, 1);
        graph.insertEdge(1, 6, 4);
        graph.insertEdge(6, 1, 6);
        graph.insertEdge(6, 11, 7);

        Assertions.assertEquals(7, graph.shortestPathCost(6, 11));
        Assertions.assertEquals("[6, 11]", graph.shortestPathData(6, 11).toString());
    }

    /**
     * Check that a NoSuchElementException is thrown if a path from start to end
     * node does not exist.
     */
    @Test
    public void submissionCheckerNoPath() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");

        graph.insertEdge("A", "B", 2);
        graph.insertEdge("B", "C", 3);

        Assertions.assertThrows(NoSuchElementException.class, () -> graph.computeShortestPath("B", "A"));
    }

    /**
     * Check that a NoSuchElementException is thrown if start node does not exist
     * in graph.
     */
    @Test
    public void submissionCheckerNoNode() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");

        graph.insertEdge("A", "B", 2);
        graph.insertEdge("B", "C", 3);

        Assertions.assertThrows(NoSuchElementException.class, () -> graph.computeShortestPath("X", "A"));
    }

}
