import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BackendTests {
    /*
     * Tests loadGraphData() to ensure base functionality possible
     */
    @Test
    public void roleTest1()
    {
        // Object to be modified by backend
        GraphADT<String,Double> graph = new Graph_Placeholder();
        // Backend initialization
        BackendInterface backend = new Backend(graph);

        // Graph_Placeholder has 3 values by default and will never be less than 3
        assertEquals(3, graph.getAllNodes().size(), "Graph_Placeholder initalized with " + graph.getAllNodes().size() + " values.");

        // loadGraphData using Graph_Placeholder should add 1 more value: Memorial Union
        try
        {
            backend.loadGraphData("campus.dot");
            assertEquals(4, graph.getAllNodes().size(), "One node should have been added by loadGraphData\n But size is " + graph.getAllNodes().size());
        }
        catch(IOException e)
        {
            fail(e.getMessage());
        }
        // Print out nodes so I can tell what is added
        System.out.println(graph.getAllNodes().toString());

        // Test invalid file name
        assertThrows(IOException.class, () -> {backend.loadGraphData("dot.dot");}, "loadGraphData did not throw exception when invalid file loaded in");
    }

    /* 
     * Tests the following three methods based on implementation of Graph_Placeholder
     * getListOfAllLocations
     * findLocationsOnShortestPath
     * getFurthestDestinationFrom
     */
    @Test
    public void roleTest2()
    {
        // Object to be modified by backend
        GraphADT<String,Double> graph = new Graph_Placeholder();
        // Backend initialization
        BackendInterface backend = new Backend(graph);
        // loadGraphData using Graph_Placeholder should  add 1 more value: Memorial Union
        try {backend.loadGraphData("campus.dot");}
        catch(IOException e) {fail(e.getMessage());}

        // Based on Graph_Placeholder what expected list is
        List<String> locations = new ArrayList<>();
        locations.add("Union South");
        locations.add("Computer Sciences and Statistics");
        locations.add("Weeks Hall for Geological Sciences");
        locations.add("Memorial Union");

        String err = "Not all locations return match the expected result.\nExpected: " + locations.toString() + "\nReturned: ";
        assertEquals(locations, backend.getListOfAllLocations(), err + backend.getListOfAllLocations());

        // Test all possible findLocationsOnShortestPath
        for (int i = 0; i < locations.size(); i++) {
            for (int j = 0; j < locations.size(); j++) {
                String start = locations.get(i);
                String end = locations.get(j);
                List<String> path = backend.findLocationsOnShortestPath(start, end);

                if (i == j) {
                    assertEquals(1, path.size(), "Expected size 1 for start == end");
                } else if (j > i) {
                    int expectedSize = j - i + 1; // includes start and end
                    assertEquals(expectedSize, path.size(), "Incorrect path size from " + start + " to " + end);
                } else {
                    int expectedSize = (locations.size() - i); // distance from start to last location
                    assertEquals(expectedSize, path.size(), "Incorrect path size from " + start + " to " + end);
                }
            }
        }

        // Test all possible getFurthestDestinationFrom assuming findLocationsOnShortestPath works
        for (int i = 0; i < 4; i++)
            if (i == 0)
            {
                assertEquals(locations.get(3),
                    backend.getFurthestDestinationFrom(locations.get(i)),
                    "Furthest desination from " + locations.get(i) + " is not " + locations.get(3));
            }
            else if (i != 3)
            {
                assertEquals(locations.get(0),
                    backend.getFurthestDestinationFrom(locations.get(i)),
                    "Furthest desination from " + locations.get(i) + " is not " + locations.get(0));
            }
            else
            {
                // assertThrows NoSuchElementException goes here. Cannot be here due to Graph_Placeholder implementation
            }
    }

    /*
     * Tests findTimesOnShortestPath which is limited by Graph_Placeholder
     * This test should be re-done in the future.
     * This test assumes roleTest2 passes
     */
    @Test
    public void roleTest3()
    {
        // Object to be modified by backend
        GraphADT<String,Double> graph = new Graph_Placeholder();
        // Backend initialization
        BackendInterface backend = new Backend(graph);
        // loadGraphData using Graph_Placeholder should  add 1 more value: Memorial Union
        try {backend.loadGraphData("campus.dot");}
        catch(IOException e) {fail(e.getMessage());}

        // Based on Graph_Placeholder what expected list is
        List<String> locations = new ArrayList<>();
        locations.add("Union South");
        locations.add("Computer Sciences and Statistics");
        locations.add("Weeks Hall for Geological Sciences");
        locations.add("Memorial Union");

        // Union Souuth to Computer Sciences and Statistics = 1
        // Computer Sciences and Statistics to Weeks Hall for Geological Sciences = 2
        // Weeks Hall for Geological Sciences to Memorial Union = 3
        List<Double> times = backend.findTimesOnShortestPath(locations.get(0), locations.get(3));
        Double sum = 0.0;
        for (int i = 0; i < times.size(); i++) sum = sum + times.get(i);
        assertEquals(6, sum, "Times returned from findTimesOnShortestPath not correct.\nExpected: 6\nReturned: " + sum);
    }
}
