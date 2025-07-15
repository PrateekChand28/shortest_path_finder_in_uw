import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test bench for checking the implementation of generateShortestPathResponseHTML(),
 * generateFurthestDestinationFromResponseHTML(), generateShortestPathPromptHTML(), and
 * generateFurthestDestinationFromPromptHTML() in the Frontend. For testing purpose, the Frontend
 * works on the Graph_Placeholder and Backend_Placeholder implementations.
 *
 * @author Prateek Chand
 */
public class FrontendTests {

    @BeforeEach
    public void createTemporaryFrontendBackedIntegration() {

    }

    /**
     * Tests if generateShortestPathResponseHTML() returns all the necessary HTML fragments with
     * expected backend responses
     */
    @Test
    public void frontendTest1() {
        Graph_Placeholder testGraph = new Graph_Placeholder();
        Backend_Placeholder testBackend = new Backend_Placeholder(testGraph);
        FrontendInterface testFrontEnd = new Frontend(testBackend);

        String shortestPath = testFrontEnd.generateShortestPathResponseHTML("Union South", "Weeks"
                + " Hall for Geological Sciences");

        assertTrue(shortestPath.contains("<p class=\"path-description\">") && shortestPath.contains("<p class=\"path-time\">") && shortestPath.contains("</p>") && shortestPath.contains("<ol>") && shortestPath.contains("</ol>") && shortestPath.contains("<li>") && shortestPath.contains("</li>"), "Missing HTML " + "tags");

        assertTrue(shortestPath.contains("Union South") && shortestPath.contains("Computer " +
                "Sciences and Statistics") && shortestPath.contains("Weeks Hall for Geological " +
                "Sciences"), "Missing start and " + "end location labels");

        assertTrue(shortestPath.contains("Total Travel Time"), "Missing time taken label");
        assertTrue(shortestPath.contains("Total Travel Time: 6.0"), "Wrong time calculated");

    }

    /**
     * Tests if generateFurthestDestinationFromResponseHTML() returns all the necessary HTML
     * fragments
     */
    @Test
    public void frontendTest2() {
        Graph_Placeholder testGraph = new Graph_Placeholder();
        Backend_Placeholder testBackend = new Backend_Placeholder(testGraph);
        FrontendInterface testFrontEnd = new Frontend(testBackend);

        String furthestDestination = testFrontEnd.generateFurthestDestinationFromResponseHTML(
                "Computer " + "Sciences and Statistics");

        assertTrue(furthestDestination.contains("<p>") && furthestDestination.contains("</p>") && furthestDestination.contains("<ol>") && furthestDestination.contains("</ol>") && furthestDestination.contains("<li>") && furthestDestination.contains("</li>"), "Missing HTML " + "tags");

        assertTrue(furthestDestination.contains("Computer Sciences and Statistics"),
                "Missing " + "start location label");
        assertTrue(furthestDestination.contains("Weeks Hall for Geological Science"),
                "Missing " + "Incorrect furthest point calculated");

    }

    /**
     * Tests if the input controls returned by generateShortestPathPromptHTML and
     * generateFurthestDestinationFromPromptHTML have the necessary HTML fragments
     */
    @Test
    public void frontendTest3() {
        Graph_Placeholder testGraph = new Graph_Placeholder();
        Backend_Placeholder testBackend = new Backend_Placeholder(testGraph);
        FrontendInterface testFrontEnd = new Frontend(testBackend);

        String shortestPathPrompt = testFrontEnd.generateShortestPathPromptHTML();
        String furthestPathPrompt = testFrontEnd.generateFurthestDestinationFromPromptHTML();

        // Verifies if required HTML tags are present in the input controls produced by
        // generateShortestPathPromptHTML
        assertTrue(shortestPathPrompt.contains("for=\"start\"") && shortestPathPrompt.contains(
                "for=\"end\"") && shortestPathPrompt.contains("id=\"start\"") && shortestPathPrompt.contains("id=\"end\"") && shortestPathPrompt.contains("<input " + "type=\"text\" id=\"end\">") && shortestPathPrompt.contains("<button id=\"find-shortest\" type=\"button\" " + "onclick=\"generateShortestPathResponseHTML(" + "document.getElementById('start').value," + "document.getElementById('end').value)\">") && shortestPathPrompt.contains("</button>"), "Missing HTML Tags");

        // Verifies if required HTML tags are present in the input controls produced by
        // generateFurthestDestinationFromPromptHTML
        assertTrue(furthestPathPrompt.contains("for=\"from\"") && furthestPathPrompt.contains("id"
                + "=\"from\"") && furthestPathPrompt.contains("id=\"find-furthest\"") && furthestPathPrompt.contains("<input type=\"text\" id=\"from\">") && furthestPathPrompt.contains("<button id=\"find-furthest\" type=\"button\" " + "onclick=\"generateFurthestDestinationFromResponseHTML(" + "document.getElementById('from').value)\">") && furthestPathPrompt.contains("</button>"), "Missing HTML Tags");

    }

    /**
     * Tests the integration of .generateShortestPathResponseHTML() in the frontend with
     * .findLocationsOnShortestPath() and .findTimesOnShortestPath() in the backend. The tester
     * checks for expected html fragments generated in the frontend in response to valid location
     * parameters provided to the backend.
     */
    @Test
    public void integrationTest1() {
        GraphADT<String, Double> testGraph = new DijkstraGraph<>();
        BackendInterface testBackend = new Backend(testGraph);

        try {
            testBackend.loadGraphData("./campus.dot");
        } catch (IOException e) {
            fail(e.getMessage());
        }

        // Provide valid start and end locations
        FrontendInterface testFrontend = new Frontend(testBackend);
        String responseOne = testFrontend.generateShortestPathResponseHTML("Memorial Union",
                "Science Hall");

        // Compares the expected list structure and total cost
        assertTrue(responseOne.contains("<li>Memorial Union</li>\n") && responseOne.contains("<li" +
                ">Science Hall</li>\n") && responseOne.contains("<p class=\"path-time\">Total " +
                "Travel Time: 105.8</p>"), "Incorrect List Generated for " + "responseOne");

        // Provide another valid start and end date
        String responseTwo = testFrontend.generateShortestPathResponseHTML("Agricultural Hall",
                "Mechanical Engineering Building");

        // Compares the expected list structure and total cost
        assertTrue(responseTwo.contains("<li>Agricultural Hall</li>\n") && responseTwo.contains(
                "<li>Agricultural Engineering</li>\n") && responseTwo.contains("<li>DeLuca " +
                "Biochemistry Building</li>\n") && responseTwo.contains("<li>Materials Science " +
                "and Engineering</li>\n") && responseTwo.contains("<li>Mechanical Engineering " +
                "Building</li>\n") && responseTwo.contains("<p class=\"path-time\">Total Travel " +
                "Time: 444" + ".40000000000003</p>"), "Incorrect List of Path for responseTwo");

    }

    /**
     * Tests the integration of .generateFurthestDestinationFromResponseHTML() in the frontend with
     * .getFurthestDestinationFrom() in the backend. The tester checks for expected html fragments
     * when provide with valid arguments in the backend.
     */
    @Test
    public void integrationTest2() {
        GraphADT<String, Double> testGraph = new DijkstraGraph<>();
        BackendInterface testBackend = new Backend(testGraph);
        try {
            testBackend.loadGraphData("./campus.dot");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        FrontendInterface testFrontend = new Frontend(testBackend);
        // Provides valid start location for which a valid end location exists
        String response = testFrontend.generateFurthestDestinationFromResponseHTML("Smith " +
                "Residence Hall");

        // Compares with the expected html fragment
        assertTrue(response.contains("<p>Furthest point: Arthur D. Hasler Laboratory of " +
                "Limnology</p>"), "Incorrect Furthest Distance");

    }

    /**
     * Tests the integration of .generateShortestPathResponseHTML() in the frontend with
     * .findLocationsOnShortestPath() and .findTimesOnShortestPath() in the backend for error
     * handling when invalid start and end locations are provided. A correct implementation should
     * display html fragments with specific error message.
     */
    @Test
    public void integrationTest3() {
        GraphADT<String, Double> testGraph = new DijkstraGraph<>();
        BackendInterface testBackend = new Backend(testGraph);
        try {
            testBackend.loadGraphData("./campus.dot");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        FrontendInterface testFrontend = new Frontend(testBackend);
        // Provides a start location that is not in the graph
        String responseOne = testFrontend.generateShortestPathResponseHTML("Invalid Start",
                "Memorial Union");
        // Checks if a html fragment with expected error message is returned
        assertEquals("<p> No path found </p>", responseOne, "Backend returns empty list so " +
                "frontend should return specific error message when invalid start location is " + "provided");

        // Provides an end location that is not in the graph
        String responseTwo = testFrontend.generateShortestPathResponseHTML("Rust-Schreiner Hall",
                "Invalid End");
        // Checks if a html fragment with expected error message is returned
        assertEquals("<p> No path found </p>", responseTwo, "Backend returns empty list so " +
                "frontend should return specific error message when invalid end location is " +
                "provided");

    }

    /**
     * Tests the integration of .generateFurthestDestinationFromResponseHTML() in the frontend with
     * .getFurthestDestinationFrom() in the backend for error handling when invalid start location
     * is provided. A correct implementation should display html fragments with specific error
     * message.
     */
    @Test
    public void integrationTest4() {
        GraphADT<String, Double> testGraph = new DijkstraGraph<>();
        BackendInterface testBackend = new Backend(testGraph);
        try {
            testBackend.loadGraphData("./campus.dot");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        FrontendInterface testFrontend = new Frontend(testBackend);
        // Provides a location that is not present in the graph
        String responseOne =
                testFrontend.generateFurthestDestinationFromResponseHTML("Invalid " + "Start");
        // Checks if a html fragment with expected error message is returned
        assertEquals("<p> Error: Encountered error when calling .getFurthestDestinationFrom()" +
                "</p>", responseOne, "Backend should throw NoSuchElementException and Frontend " +
                "should " + "display specific error message");

    }

    /**
     * Tests the integration of frontend and backend when null queries are provided. This tester
     * checks for both the shortest distance and the furthest distance functionality and verifies if
     * expected error message are presented as html fragments. Note: In the WebApp.java, null
     * queries will throw IllegalArgumentException and hence they can not be explicitly tested in
     * the browser. However, in the server terminal, it will display the IllegalArgumentException.
     */
    @Test
    public void integrationTest5() {
        GraphADT<String, Double> testGraph = new DijkstraGraph<>();
        BackendInterface testBackend = new Backend(testGraph);
        try {
            testBackend.loadGraphData("./campus.dot");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        FrontendInterface testFrontend = new Frontend(testBackend);
        // Checks for error handling in the implementation when frontend calls the
        // .findLocationsOnShortestPath() and the .findTimesOnShortestPath() in the backend with
        // null arguments
        String responseOne = testFrontend.generateShortestPathResponseHTML(null, null);
        // Checks if a html fragment with expected error message is returned
        assertEquals("<p>Encountered Exception</p>", responseOne,
                "Backend throws null pointer " + "exception " + "when calling the HashtableMap" +
                        ".contains() and frontend expected to display " + "specific message");

        // Checks for error handling in the implementation when frontend calls the
        // .getFurthestDestinationFrom() in the backend with null arguments
        String responseTwo = testFrontend.generateFurthestDestinationFromResponseHTML(null);
        // Checks if a html fragment with expected error message is returned
        assertEquals("<p> Error: Encountered error when calling .getFurthestDestinationFrom()" +
                "</p>", responseTwo, "Backend throws null pointer exception when calling the " +
                "HashtableMap.contains() and frontend expected to display specific " + "message");

    }
}
