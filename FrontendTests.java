import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
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

        assertTrue(shortestPath.contains("<p class=\"path-description\">") &&
                        shortestPath.contains("<p class=\"path-time\">") &&
                        shortestPath.contains("</p>") &&
                        shortestPath.contains("<ol>") &&
                        shortestPath.contains("</ol>") &&
                        shortestPath.contains("<li>") &&
                        shortestPath.contains("</li>"),
                "Missing HTML " + "tags");

        assertTrue(shortestPath.contains("Union South") && shortestPath.contains("Computer " +
                "Sciences and Statistics") &&
                shortestPath.contains("Weeks Hall for Geological Sciences"),
                "Missing start and " + "end location labels");

        assertTrue(shortestPath.contains("Total Travel Time"), "Missing time taken label");
        assertTrue(shortestPath.contains("Total Travel Time: 6.0"), "Wrong time calculated");

    }

    /**
     * Tests if generateFurthestDestinationFromResponseHTML() returns all the necessary HTML
     * fragments with expected backend responses
     */
    @Test
    public void frontendTest2() {
        Graph_Placeholder testGraph = new Graph_Placeholder();
        Backend_Placeholder testBackend = new Backend_Placeholder(testGraph);
        FrontendInterface testFrontEnd = new Frontend(testBackend);

        String furthestDestination = testFrontEnd.generateFurthestDestinationFromResponseHTML(
                "Computer " + "Sciences and Statistics");

        assertTrue(furthestDestination.contains("<p>") &&
                furthestDestination.contains("</p>") && furthestDestination.contains("<ol>") &&
                furthestDestination.contains("</ol>") && furthestDestination.contains("<li>") &&
                furthestDestination.contains("</li>"), "Missing HTML " + "tags");

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
                "for=\"end\"") && shortestPathPrompt.contains("id=\"start\"") &&
                shortestPathPrompt.contains("id=\"end\"") && shortestPathPrompt.contains("<input " +
                "type=\"text\" id=\"end\">")
                && shortestPathPrompt.contains("<button id=\"find-shortest\" type=\"button\" " +
                "onclick=\"generateShortestPathResponseHTML()\">") &&
                shortestPathPrompt.contains("</button>"), "Missing HTML Tags");

        // Verifies if required HTML tags are present in the input controls produced by
        // generateFurthestDestinationFromPromptHTML
        assertTrue(furthestPathPrompt.contains("for=\"from\"") &&
                furthestPathPrompt.contains("id" + "=\"from\"") &&
                furthestPathPrompt.contains("id=\"find-furthest\"") &&
                furthestPathPrompt.contains("<input type=\"text\" id=\"from\">") &&
                furthestPathPrompt.contains("<button id=\"find-furthest\" type=\"button\" " +
                        "onclick=\"generateFurthestDestinationFromResponseHTML()\">") &&
                furthestPathPrompt.contains("</button>"), "Missing HTML Tags");

    }
}
