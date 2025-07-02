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

        // DijkstraGraph is a derived class of BaseGraph. The constructor of the BaseGraph is
        // inherited in the DijkstraGraph graph and is initialized with PlaceholderMap.
        // PlaceholderMap implements the MapADT which underneath uses HashMap as the base map and
        // all the methods invoked in PlaceholderMap use the HashMap class methods.

        // containsNode() is inherited by DijkstraGraph. In the BaseGraph, the parameter passed to
        // this method checks in the PlaceholderMap and searches for matching key using the
        // .containsKey() method. The .containsKey() further searches for the key value in the
        // base HashMap
        if (!this.containsNode(start)) {
            throw new NoSuchElementException("Start node is not present in the base graph");
        }

        if (!this.containsNode(end)) {
            throw new NoSuchElementException("End node is not present in the base graph");
        }

        // SearchNode contains cost field and has predecessor pointing to the previous SearchNode
        // These features are why we are not directly using the Node class. When looking for
        // different possibilities, the same node can have different paths since they have
        // different edgesLeaving and SearchNode will help represent these possibilities.

        // Priority queue can use the cost field in the SearchNode to process cheapest path first.
        // Helps to keep track of unvisited nodes. Since SearchNode implements comparable
        // interface, the Priority Queue uses the overridden compareTo() method in the
        // SearchNode class when deciding priority of SearchNodes inserted into it
        PriorityQueue<SearchNode> unvisitedQueue = new PriorityQueue<>();

        // MapADT helps to keep track of visited nodes. Treats the map as a visited set by
        // inserting nodes as both keys and values. The containsKey() method from this data
        // structure can later be used to check if the node is already visited. If the node is
        // already visited then it is not added into the visitedNodeList and the edge nodes from
        // that node are not explored. That also means that this list will never have duplicates.
        // However, there will be cases when the priority queue above will have some shared
        // predecessor or successor node. Then again the edge weights will be different which is
        // we run in the debug mode is one of the way to identify the predecessor-successor pair
        PlaceholderMap<Node, Node> visitedNodesList = new PlaceholderMap<>();

        // Step 1: Add the starting node into the priority queue. Since it is the starting node,
        // its cost is zero and has no predecessor. Since we need to use SearchNode to keep track
        // of path, we need to get the node from the graph with this starting value and turn it
        // into a SearchNode.
        // nodes here is a MapADT with KeyType as NodeType and ValueType as Node, so the .get()
        // returns the node containing this starting value in the graph
        SearchNode startingNode = new SearchNode(this.nodes.get(start), 0.0, null);
        unvisitedQueue.add(startingNode);

        // Step 2: First add the starting node into the visited list and then remove from the
        // priority queue. Then, all the nodes from the list of edgeLeaving field of the starting
        // node are added into the priority queue. The priority queue should sequence these
        // adjacent nodes in the expected priority order. So, when the poll() is invoked, the
        // head removed from the queue represents the node with the cheapest cost.
        while (!unvisitedQueue.isEmpty()) {

            // retrieves the node with the minimum cost from the current priority queue
            SearchNode currentVertex = unvisitedQueue.poll();

            // Step 3: It is necessary to check the visited list of nodes here because multiple
            // predecessors can share the same node and if that node was already previously added
            // into the visited list then that means that the new node + predecessor pair cost
            // more. So this SearchNode needs to be skipped
            if (!visitedNodesList.containsKey(currentVertex.node)) {

                // Step 4: Need to keep track of this node if the node doesn't already exist
                // in the visited list of nodes
                visitedNodesList.put(currentVertex.node, currentVertex.node);

                // Step 5: Now before we further explore the edges of this current node, we should
                // confirm if we already reached the end. If we reached the end, we should terminate
                // the loop and stop exploring other edges.
                if (currentVertex.node.data.equals(end)) {
                    // this is the final end point and so to traverse the path we need to
                    // traverse through the predecessors that led to the ending node
                    // this also means that not all the visited nodes will fall in this path
                    return currentVertex;
                }

                // Step 6: Since we have not yet reached the end, what we need to do is now from
                // this current vertex, we need to explore outgoing edges and their edge weight.
                // We then need to calculate the cost of taking each path. We then add each of
                // the adjacent nodes into the priority queue for the priority queue to continue
                // doing its job of selecting the less costly SearchNode.
                for (Edge eachEdgeAdjacentToCurrentVertex : currentVertex.node.edgesLeaving) {

                    // Step 7: We explicitly converted the data field of the Edge class here because
                    // it is a generic data type but if we look at the cost field in the SearchNode
                    // class, its data type is double.When I usually work on paper, I tend to forget 
		    // adding the edge weight with the predecessor's weight, which may
                    // lead to an unexpected path.
                    double costForTakingCurrentEdge =
                            currentVertex.cost + Double.parseDouble(eachEdgeAdjacentToCurrentVertex.data.toString());

                    // Step 8: Here, the SearchNode is being added to the priority queue. The
                    // advantage of using SearchNode here is that we are able to track the path we
                    // are taking from each vertex thanks to the fact that we are storing the
                    // predecessor information into each SearchNode. Since the priority queue will
                    // end up popping only the SearchNodes that are less costly to their
                    // counterparts, when the end node is finally reached, the SearchNode will
                    // have access to all the previous predecessors that were selected. In the
                    // lecture, it is similar to writing the predecessor-successor node pair.
                    // Something that can be noticed when running in debug mode is that, we can
                    // see who the predecessor is and so in cases when the same successors are there
                    // in the queue, we can tell which predecessor-successor pair it is.
                    SearchNode currentAdjacentNode =
                            new SearchNode(eachEdgeAdjacentToCurrentVertex.successor,
                                    costForTakingCurrentEdge, currentVertex);

                    unvisitedQueue.add(currentAdjacentNode);
                }
            }
        }

        // Step 8: If no path was found, then that means no path exist to reach the end from the
        // start. This means when we were adding SearchNodes for each vertex starting from our
        // starting node, we never encountered an edge that linked with the end node. Hence, the
        // while loop above never returned a vertex.
        throw new NoSuchElementException("Path doesn't exist between the given points");

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

        // SearchNode for the end node. Traversing back all the predecessors linked to this
        // end node gives the shortest path.
        SearchNode endNode = this.computeShortestPath(start, end);
        LinkedList<NodeType> shortestPathTaken = new LinkedList<>();

        // Adds the end search node as the head of the linked list initially and then traverses
        // through the predecessors until the start node is reached.
        // updates the head of the linked list with new predecessor each time. If the hint was
        // not provided in the import section, I almost ended up using stacks. That way the start
        // node would get added in the top, and it would also maintain O(1) time complexity. Here
        // the head pointer gets updated in constant time and only one link should be updated
        // when a new predecessor is added which also happens in constant time.
        while (endNode != null) {
            shortestPathTaken.addFirst(endNode.node.data);
            // Since the start node has null as its predecessor the traversal gets terminated
            // once the start node is added to the list
            endNode = endNode.predecessor;
        }

        return shortestPathTaken;
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
        // The cost field of the end SearchNode is cumulative of all the edge weights from start
        // node to all the adjacent nodes that fall in the shortest path
        return computeShortestPath(start, end).cost;
    }


    /**
     * Tests the lecture example to verify if the graph implementation returns the correct shortest
     * path and cost. The start node is D and the end node is I. The expected shortest path is
     * D->G->H->I with the cost of 13.
     */
    @Test
    public void testLectureDemo() {
        DijkstraGraph<String, Double> testGraph = new DijkstraGraph<>();
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

        testGraph.insertEdge("G", "A", 4.0);
        testGraph.insertEdge("D", "A", 7.0);
        testGraph.insertEdge("A", "B", 1.0);
        testGraph.insertEdge("H", "B", 6.0);
        testGraph.insertEdge("I", "D", 1.0);
        testGraph.insertEdge("M", "E", 3.0);
        testGraph.insertEdge("D", "F", 4.0);
        testGraph.insertEdge("M", "F", 4.0);
        testGraph.insertEdge("D", "G", 2.0);
        testGraph.insertEdge("F", "G", 9.0);
        testGraph.insertEdge("A", "H", 7.0);
        testGraph.insertEdge("G", "H", 9.0);
        testGraph.insertEdge("I", "H", 2.0);
        testGraph.insertEdge("M", "I", 4.0);
        testGraph.insertEdge("H", "I", 2.0);
        testGraph.insertEdge("G", "L", 7.0);
        testGraph.insertEdge("H", "L", 2.0);
        testGraph.insertEdge("A", "M", 5.0);
        testGraph.insertEdge("B", "M", 3.0);

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
     * shortest path and cost. The start node is F and the end node is M. The expected shortest path
     * is F->G->A->B->M with the cost of 17.
     */
    @Test
    public void testLectureDifferentPath() {
        DijkstraGraph<String, Double> testGraph = new DijkstraGraph<>();
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

        testGraph.insertEdge("G", "A", 4.0);
        testGraph.insertEdge("D", "A", 7.0);
        testGraph.insertEdge("A", "B", 1.0);
        testGraph.insertEdge("H", "B", 6.0);
        testGraph.insertEdge("I", "D", 1.0);
        testGraph.insertEdge("M", "E", 3.0);
        testGraph.insertEdge("D", "F", 4.0);
        testGraph.insertEdge("M", "F", 4.0);
        testGraph.insertEdge("D", "G", 2.0);
        testGraph.insertEdge("F", "G", 9.0);
        testGraph.insertEdge("A", "H", 7.0);
        testGraph.insertEdge("G", "H", 9.0);
        testGraph.insertEdge("I", "H", 2.0);
        testGraph.insertEdge("M", "I", 4.0);
        testGraph.insertEdge("H", "I", 2.0);
        testGraph.insertEdge("G", "L", 7.0);
        testGraph.insertEdge("H", "L", 2.0);
        testGraph.insertEdge("A", "M", 5.0);
        testGraph.insertEdge("B", "M", 3.0);

        List<String> shortestPathBetweenFtoM = testGraph.shortestPathData("F", "M");
        Double shortestPathCostBetweenFtoM = testGraph.shortestPathCost("F", "M");

        List<String> expectedShortestPath = new ArrayList<>();
        expectedShortestPath.add("F");
        expectedShortestPath.add("G");
        expectedShortestPath.add("A");
        expectedShortestPath.add("B");
        expectedShortestPath.add("M");

        assertEquals(expectedShortestPath, shortestPathBetweenFtoM, "Incorrect Shortest Path");
        assertEquals(17, shortestPathCostBetweenFtoM, "Incorrect Shortest Path Cost");

    }

    /**
     * Tests the lecture example to verify if the graph implementation properly handles a case when
     * no directed edges sequence can be established between two nodes. The start node is E and the
     * end node is D.
     */
    @Test
    public void testNoDirectedEdgeSequence() {
        DijkstraGraph<String, Double> testGraph = new DijkstraGraph<>();
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

        testGraph.insertEdge("G", "A", 4.0);
        testGraph.insertEdge("D", "A", 7.0);
        testGraph.insertEdge("A", "B", 1.0);
        testGraph.insertEdge("H", "B", 6.0);
        testGraph.insertEdge("I", "D", 1.0);
        testGraph.insertEdge("M", "E", 3.0);
        testGraph.insertEdge("D", "F", 4.0);
        testGraph.insertEdge("M", "F", 4.0);
        testGraph.insertEdge("D", "G", 2.0);
        testGraph.insertEdge("F", "G", 9.0);
        testGraph.insertEdge("A", "H", 7.0);
        testGraph.insertEdge("G", "H", 9.0);
        testGraph.insertEdge("I", "H", 2.0);
        testGraph.insertEdge("M", "I", 4.0);
        testGraph.insertEdge("H", "I", 2.0);
        testGraph.insertEdge("G", "L", 7.0);
        testGraph.insertEdge("H", "L", 2.0);
        testGraph.insertEdge("A", "M", 5.0);
        testGraph.insertEdge("B", "M", 3.0);

        assertThrows(NoSuchElementException.class, () -> testGraph.shortestPathData("E", "D"),
                "No " + "directed edge sequence to calculate path but still didn't throw " +
                        "exception");

        assertThrows(NoSuchElementException.class, () -> testGraph.shortestPathCost("E", "D"),
                "No " + "directed edge sequence to calculate path but still didn't throw " +
                        "exception");

    }

    /**
     * Tests question number 3 from assignment to verify if the graph implementation returns the
     * correct shortest path and cost. The start node is A and the end node is G. The expected
     * shortest path is A->C->B->E->G with the cost of 9.
     */

    @Test
    public void assignmentQuestionThree() {
        DijkstraGraph<String, Double> testGraph = new DijkstraGraph<>();
        testGraph.insertNode("A");
        testGraph.insertNode("B");
        testGraph.insertNode("C");
        testGraph.insertNode("D");
        testGraph.insertNode("E");
        testGraph.insertNode("F");
        testGraph.insertNode("G");

        testGraph.insertEdge("C", "B", 2.0);
        testGraph.insertEdge("F", "B", 2.0);
        testGraph.insertEdge("A", "C", 2.0);
        testGraph.insertEdge("F", "C", 4.0);
        testGraph.insertEdge("A", "D", 4.0);
        testGraph.insertEdge("B", "D", 2.0);
        testGraph.insertEdge("F", "D", 3.0);
        testGraph.insertEdge("B", "E", 3.0);
        testGraph.insertEdge("D", "E", 4.0);
        testGraph.insertEdge("A", "F", 3.0);
        testGraph.insertEdge("C", "G", 8.0);
        testGraph.insertEdge("E", "G", 2.0);

        List<String> shortestPathBetweenAtoG = testGraph.shortestPathData("A", "G");
        Double shortestPathCostBetweenAtoG = testGraph.shortestPathCost("A", "G");

        List<String> expectedShortestPath = new ArrayList<>();
        expectedShortestPath.add("A");
        expectedShortestPath.add("C");
        expectedShortestPath.add("B");
        expectedShortestPath.add("E");
        expectedShortestPath.add("G");

        assertEquals(expectedShortestPath, shortestPathBetweenAtoG, "Incorrect Shortest Path");
        assertEquals(9, shortestPathCostBetweenAtoG, "Incorrect Shortest Path Cost");
    }
}
