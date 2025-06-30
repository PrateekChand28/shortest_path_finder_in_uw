// === CS400 File Header Information ===
// Name: <your full name>
// Email: <your @wisc.edu email address>
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: <name of your lecturer>
// Notes to Grader: <optional extra notes>

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType,
        EdgeType> implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode contains data about one
     * specific path between the start node and another node in the graph. The final node in this
     * path is stored in its node field. The total cost of this path is stored in its cost field.
     * And the predecessor SearchNode within this path is referened by the predecessor field (this
     * field is null within the SearchNode containing the starting node in its node field).
     * <p>
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
            if (cost > other.cost) return +1;
            if (cost < other.cost) return -1;
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
     * the provided start and end locations. The SearchNode that is returned by this method is
     * represents the end of the shortest path that is found: it's cost is the cost of that shortest
     * path, and the nodes linked together through predecessor references represent all of the nodes
     * along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found or when either start
     *                                or end data do not correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // implement in step 5.3
        return null;
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
        // implement in step 5.4
        return null;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
     * containing the start data to the node containing the end data. This method uses Dijkstra's
     * shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // implement in step 5.4
        return Double.NaN;
    }


    /**
     * Tests the lecture example to verify if the graph implementation returns the correct
     * shortest path and cost. The start node is D and the end node is I. The expected shortest
     * path is D->G->H->I with the cost of 13.
     */
    @Test
    public void testLectureDemo() {
        DijkstraGraph<String, Integer> testGraph = new DijkstraGraph<>();
        testGraph.insertNode("A");
        testGraph.insertNode("B");
        testGraph.insertNode("D");
        testGraph.insertNode("E");
        testGraph.insertNode("F");
        testGraph.insertNode("G");
        testGraph.insertNode("H");
        testGraph.insertNode("I");
        testGraph.insertNode("L");
        testGraph.insertNode("M");

        testGraph.insertEdge("G", "A", 4);
        testGraph.insertEdge("D", "A", 7);
        testGraph.insertEdge("A", "B", 1);
        testGraph.insertEdge("H", "B", 6);
        testGraph.insertEdge("I", "D", 1);
        testGraph.insertEdge("M", "E", 3);
        testGraph.insertEdge("D", "F", 4);
        testGraph.insertEdge("M", "F", 4);
        testGraph.insertEdge("D", "G", 2);
        testGraph.insertEdge("F", "G", 9);
        testGraph.insertEdge("A", "H", 7);
        testGraph.insertEdge("G", "H", 9);
        testGraph.insertEdge("I", "H", 2);
        testGraph.insertEdge("M", "I", 4);
        testGraph.insertEdge("H", "I", 2);
        testGraph.insertEdge("G", "L", 7);
        testGraph.insertEdge("H", "L", 2);
        testGraph.insertEdge("A", "M", 5);
        testGraph.insertEdge("B", "M", 3);

        List<String> shortestPathBetweenDtoI = testGraph.shortestPathData("D", "I");
        Double shortestPathCostBetweenDtoI = testGraph.shortestPathCost("D", "I");

        List<String> expectedShortestPath = new ArrayList<>();
        expectedShortestPath.add("D");
        expectedShortestPath.add("G");
        expectedShortestPath.add("H");
        expectedShortestPath.add("I");

        assertEquals(expectedShortestPath, shortestPathBetweenDtoI, "Incorrect Shortest Path");
        assertEquals(13, shortestPathCostBetweenDtoI, "Incorrect Shortest Path Cost");

    }

    /**
     * Tests the modified lecture example to verify if the graph implementation returns the correct
     * shortest path and cost. The start node is F and the end node is M. The expected shortest
     * path is F->G->A->M with the cost of 17.
     */
    @Test
    public void testLectureDifferentPath() {
        DijkstraGraph<String, Integer> testGraph = new DijkstraGraph<>();
        testGraph.insertNode("A");
        testGraph.insertNode("B");
        testGraph.insertNode("D");
        testGraph.insertNode("E");
        testGraph.insertNode("F");
        testGraph.insertNode("G");
        testGraph.insertNode("H");
        testGraph.insertNode("I");
        testGraph.insertNode("L");
        testGraph.insertNode("M");

        testGraph.insertEdge("G", "A", 4);
        testGraph.insertEdge("D", "A", 7);
        testGraph.insertEdge("A", "B", 1);
        testGraph.insertEdge("H", "B", 6);
        testGraph.insertEdge("I", "D", 1);
        testGraph.insertEdge("M", "E", 3);
        testGraph.insertEdge("D", "F", 4);
        testGraph.insertEdge("M", "F", 4);
        testGraph.insertEdge("D", "G", 2);
        testGraph.insertEdge("F", "G", 9);
        testGraph.insertEdge("A", "H", 7);
        testGraph.insertEdge("G", "H", 9);
        testGraph.insertEdge("I", "H", 2);
        testGraph.insertEdge("M", "I", 4);
        testGraph.insertEdge("H", "I", 2);
        testGraph.insertEdge("G", "L", 7);
        testGraph.insertEdge("H", "L", 2);
        testGraph.insertEdge("A", "M", 5);
        testGraph.insertEdge("B", "M", 3);

        List<String> shortestPathBetweenDtoI = testGraph.shortestPathData("F", "M");
        Double shortestPathCostBetweenDtoI = testGraph.shortestPathCost("F", "M");

        List<String> expectedShortestPath = new ArrayList<>();
        expectedShortestPath.add("F");
        expectedShortestPath.add("G");
        expectedShortestPath.add("A");
        expectedShortestPath.add("M");

        assertEquals(expectedShortestPath, shortestPathBetweenDtoI, "Incorrect Shortest Path");
        assertEquals(17, shortestPathCostBetweenDtoI, "Incorrect Shortest Path Cost");

    }

    /**
     * Tests the lecture example to verify if the graph implementation properly handles a case
     * when no directed edges sequence can be established between two nodes. The start node is E
     * and the end node is D.
     */
    @Test
    public void testNoDirectedEdgeSequence() {
        DijkstraGraph<String, Integer> testGraph = new DijkstraGraph<>();
        testGraph.insertNode("A");
        testGraph.insertNode("B");
        testGraph.insertNode("D");
        testGraph.insertNode("E");
        testGraph.insertNode("F");
        testGraph.insertNode("G");
        testGraph.insertNode("H");
        testGraph.insertNode("I");
        testGraph.insertNode("L");
        testGraph.insertNode("M");

        testGraph.insertEdge("G", "A", 4);
        testGraph.insertEdge("D", "A", 7);
        testGraph.insertEdge("A", "B", 1);
        testGraph.insertEdge("H", "B", 6);
        testGraph.insertEdge("I", "D", 1);
        testGraph.insertEdge("M", "E", 3);
        testGraph.insertEdge("D", "F", 4);
        testGraph.insertEdge("M", "F", 4);
        testGraph.insertEdge("D", "G", 2);
        testGraph.insertEdge("F", "G", 9);
        testGraph.insertEdge("A", "H", 7);
        testGraph.insertEdge("G", "H", 9);
        testGraph.insertEdge("I", "H", 2);
        testGraph.insertEdge("M", "I", 4);
        testGraph.insertEdge("H", "I", 2);
        testGraph.insertEdge("G", "L", 7);
        testGraph.insertEdge("H", "L", 2);
        testGraph.insertEdge("A", "M", 5);
        testGraph.insertEdge("B", "M", 3);

        List<String> shortestPathBetweenDtoI = testGraph.shortestPathData("E", "D");
        Double shortestPathCostBetweenDtoI = testGraph.shortestPathCost("E", "D");

        assertNull(shortestPathCostBetweenDtoI, "No directed edge sequence to calculate path");
        assertNull(shortestPathCostBetweenDtoI, "No directed edge sequence to calculate cost");

    }
}
