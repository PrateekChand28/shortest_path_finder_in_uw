import java.util.List;

public class Frontend implements FrontendInterface {

    private BackendInterface backend;
    /**
     * Implementing classes should support the constructor below.
     * @param backend is used for shortest path computations
     */
    public Frontend(BackendInterface backend){
        this.backend = backend;
    };
    /**
     * Returns an HTML fragment that can be embedded within the body of a larger html page.  This
     * HTML output should include: - a text input field with the id="start", for the start location
     * - a text input field with the id="end", for the destination - a button labelled "Find
     * Shortest Path" to request this computation Ensure that these text fields are clearly
     * labelled, so that the user can understand how to use them.
     *
     * @return an HTML string that contains input controls that the user can make use of to request
     * a shortest path computation
     */
    @Override
    public String generateShortestPathPromptHTML() {
        StringBuilder htmlInputControls = new StringBuilder();
        htmlInputControls.append(
                "<label for=\"start\"> " +
                        "Start Location" +
                        "<input type=\"text\" id=\"start\">" +
                "</label>");
        htmlInputControls.append("<br>");
        htmlInputControls.append(
                "<label for=\"end\"> " +
                        "End Location" +
                        "<input type=\"text\" id=\"end\">" +
                "</label>");
        htmlInputControls.append("<br>");
        htmlInputControls.append(
                "<button id=\"find-shortest\" type=\"button\" " +
                        "onclick=\"generateShortestPathResponseHTML(" +
                        "document.getElementById('start').value," +
                        "document.getElementById('end').value)\">" +
                        "Find Shortest Path" +
                "</button>");

        return htmlInputControls.toString();
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a larger html page.  This
     * HTML output should include: - a paragraph (p) that describes the path's start and end
     * locations - an ordered list (ol) of locations along that shortest path - a paragraph (p) that
     * includes the total travel time along this path Or if there is no such path, the HTML returned
     * should instead indicate the kind of problem encountered.
     *
     * @param start is the starting location to find a shortest path from
     * @param end   is the destination that this shortest path should end at
     * @return an HTML string that describes the shortest path between these two locations
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        StringBuilder htmlPathDescription = new StringBuilder();
        List<String> locations = null;
        List<Double> timeTaken = null;

        // Feedback 1 Implementation: wrapped the method call in try-catch block
        try {
                locations = backend.findLocationsOnShortestPath(start, end);
                timeTaken = backend.findTimesOnShortestPath(start, end);
        } catch (Exception e){
                System.err.println(e.getMessage());
                return "<p>Encountered Exception</p>";

        }
	if(locations.isEmpty() || timeTaken.isEmpty()){
            return htmlPathDescription.append("<p> No path found </p>").toString();
        }

        htmlPathDescription.append
                ("<p class=\"path-description\">" +
                        "Start point: ").append(start).append("End point: ").append(end).append
                ("</p>");

        htmlPathDescription.append("<ol>");
        htmlPathDescription.append("\n");

        for(String currentLocation: locations){
            htmlPathDescription.append("<li>").append(currentLocation).append("</li>");
            htmlPathDescription.append("\n");
        }
        htmlPathDescription.append("</ol>");
        htmlPathDescription.append("\n");

        Double totalTravelTime = 0.0;
        for(Double currentTime: timeTaken){
            totalTravelTime += currentTime;
        }

        htmlPathDescription.append
                ("<p class=\"path-time\">" +
                        "Total Travel Time: ").append(totalTravelTime).append(
                "</p>");

        return htmlPathDescription.toString();
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a larger html page.  This
     * HTML output should include: - a text input field with the id="from", for the start location -
     * a button labelled "Furthest Destination From" to submit this request Ensure that this text
     * field is clearly labelled, so that the user can understand how to use it.
     *
     * @return an HTML string that contains input controls that the user can make use of to request
     * a furthest destination calculation
     */
    @Override
    public String generateFurthestDestinationFromPromptHTML() {
        StringBuilder htmlFindFurthestControls = new StringBuilder();
        htmlFindFurthestControls.append(
                "<label for=\"from\"> " +
                        "Start Location" +
                        "<input type=\"text\" id=\"from\">" +
                "</label>");
        htmlFindFurthestControls.append(
                "<button id=\"find-furthest\" type=\"button\" " +
                        "onclick=\"generateFurthestDestinationFromResponseHTML(" +
                        "document.getElementById('from').value)\">" +
                        "Furthest Destination From" +
                "</button>");

        return htmlFindFurthestControls.toString();
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a larger html page.  This
     * HTML output should include: - a paragraph (p) that describes the starting point being
     * searched from - a paragraph (p) that describes the furthest destination found - an ordered
     * list (ol) of locations on the path between these locations Or if there is no such
     * destination, the HTML returned should instead indicate the kind of problem encountered.
     *
     * @param start is the starting location to find the furthest dest from
     * @return an HTML string that describes the furthest destination from the specified start
     * location
     */
    @Override
    public String generateFurthestDestinationFromResponseHTML(String start) {
        StringBuilder htmlPathDescription = new StringBuilder();

        String furthestDestination = null;
        // Feedback 1 Implementation: Wrapped the method call in a try-catch block
        try{
                furthestDestination = backend.getFurthestDestinationFrom(start);
        } catch (Exception e) {
                System.err.println(e.getMessage());
                return "<p> Error: Encountered error when calling .getFurthestDestinationFrom()</p>";
        }

        if(furthestDestination.isEmpty()){
            return htmlPathDescription.append("<p>No path found</p>").toString();
        }

        htmlPathDescription.append
                ("<p>" +
                        "Start point: ").append(start).append
                ("</p>");
        htmlPathDescription.append("\n").append
                ("<p>" +
                        "Furthest point: ").append(furthestDestination).append
                ("</p>");

        List<String> locations = backend.findLocationsOnShortestPath(start, furthestDestination);

        htmlPathDescription.append("<ol>");
        htmlPathDescription.append("\n");

        for(String currentLocation: locations){
            htmlPathDescription.append("<li>").append(currentLocation).append("</li>");
            htmlPathDescription.append("\n");
        }
        htmlPathDescription.append("</ol>");
        htmlPathDescription.append("\n");

        return htmlPathDescription.toString();
    }
}
