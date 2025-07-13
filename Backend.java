import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Backend implements BackendInterface{

    GraphADT<String,Double> graph;

    /*
     * Implementing classes should support the constructor below.
     * @param graph object to store the backend's graph data
     */
    public Backend(GraphADT<String,Double> graph)
    {
        this.graph = graph;
    }

    /**
     * Loads graph data from a dot file.  If a graph was previously loaded, this
     * method should first delete the contents (nodes and edges) of the existing 
     * graph before loading a new one.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    public void loadGraphData(String filename) throws IOException
    {
        // Local vars
        File file = new File(filename);
        
        // dot file format: "Memorial Union" -> "Science Hall" [seconds=105.8];
        try (Scanner scanner = new Scanner(file)) 
        { // Open Scanner - Will throw IOException if no file found
            if (!scanner.hasNextLine()) return; // Return if file is empty
            // Skip line 1
            scanner.nextLine();

            // Remove all nodes and edges
            List<String> nodes = graph.getAllNodes();
            for(int i = 0; i < nodes.size(); i++) graph.removeNode(nodes.get(i));

            // Go through each line, created nodes and edges
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                double seconds = 0;

                String[] parts = line.split(" -> ");
                // If split didn't work it is the last line in the file and can be skipped
                if (parts.length == 1)
                {
                    scanner.close();
                    return; 
                } 

                // Store "Source" as Source in a string
                String source = parts[0].replace("\"", "").trim(); 

                String[] partTwo = parts[1].split(" \\[seconds=");
                // Store "Destination" as Destination in a string
                String dest = partTwo[0].replace("\"", "").trim(); 

                try
                {
                    seconds = Double.parseDouble(partTwo[1].replace("];", ""));
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Recieved invalid double for seconds\nDouble check code works\nError: " + e.getMessage());
                    scanner.close();
                    return;
                }
                /* Insert new nodes and edges */
                // insertNode() handles duplicate nodes, no need to check
                graph.insertNode(source);
                graph.insertNode(dest);
                // insertEdge() updates edge if one is already there, no need to check
                graph.insertEdge(source, dest, seconds);
            }
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filename, e);
        }
    }

    /**
     * Returns a list of all locations (node data) available in the graph.
     * @return list of all location names
     */
    public List<String> getListOfAllLocations()
    {
        return graph.getAllNodes();
    }

    /**
     * Return the sequence of locations along the shortest path from 
     * startLocation to endLocation, or an empty list if no such path exists.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the nodes along the shortest path from startLocation 
     *         to endLocation, or an empty list if no such path exists
     */
    public List<String> findLocationsOnShortestPath(String startLocation, String endLocation)
    {
        // Default return value
        List<String> path = new ArrayList<>();

        // Check locations are valid
        if (!graph.containsNode(startLocation)){
            System.out.println("Location \"" + startLocation + "\" not found");
            return path;
        } else if (!graph.containsNode(endLocation)){
            System.out.println("Location \"" + endLocation + "\" not found");
            return path;
        }

        // Use GraphADT to get path and return
        try
        {
            path = graph.shortestPathData(startLocation, endLocation);
        }
        catch (NoSuchElementException e) 
        {
            // Locations are valid so NoSuchElementException only thrown if no path exists.
            System.out.println("No possible path found between " + startLocation + " and " + endLocation);
            System.out.println("Error: " + e.getMessage());
        }

        return path;
    }

    /**
     * Return the walking times in seconds between each two nodes on the 
     * shortest path from startLocation to endLocation, or an empty list of no 
     * such path exists.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the walking times in seconds between two nodes along 
     *         the shortest path from startLocation to endLocation, or an empty 
     *         list if no such path exists
     */
    public List<Double> findTimesOnShortestPath(String startLocation, String endLocation)
    {
        // Default return value
        List<String> path = new ArrayList<>();
        List<Double> path_times = new ArrayList<Double>();

        // Check locations are valid
        if (!graph.containsNode(startLocation)){
            System.out.println("Location \"" + startLocation + "\" not found");
            return path_times;
        } else if (!graph.containsNode(endLocation)){
            System.out.println("Location \"" + endLocation + "\" not found");
            return path_times;
        }

        // Use GraphADT to get path and return
        try
        {
            path = graph.shortestPathData(startLocation, endLocation);
        }
        catch (NoSuchElementException e) 
        {
            // Locations are valid so NoSuchElementException only thrown if no path exists.
            System.out.println("No possible path found between " + startLocation + " and " + endLocation);
            System.out.println("Error: " + e.getMessage());
            return path_times;
        }

        // Loop over and add each travel time between nodes to path.
        // Do not need to check node or path is valid, returned from GraphADT shortestPathData is valid.
        // For m nodes there is m-1 travel times.
        for (int i = 0; i < path.size() - 1; i++)
        {
            path_times.add(graph.getEdge(path.get(i), path.get(i+1)));
        }

        return path_times;
    }

    /**
     * Returns the most distant location (the one that takes the longest time to 
     * reach) when comparing all shortest paths that begin from the provided 
     * startLocation.
     * @param startLocation the location to find the most distant location from
     * @return the most distant location (the one that takes the longest time to 
     *         reach which following the shortest path)
     * @throws NoSuchElementException if startLocation does not exist, or if
     *         there are no other locations that can be reached from there
     */
  public String getFurthestDestinationFrom(String startLocation) throws NoSuchElementException
  {
        // Check location is valid
        if (!graph.containsNode(startLocation)){
            String err = "Location \"" + startLocation + "\" not found";
            throw new NoSuchElementException(err);
        }

        String furthest = startLocation;
        double maxDistance = -1.0;
        boolean foundValidPath = false;

        for (String node : graph.getAllNodes()) {
            if (node.equals(startLocation)) continue;

            try {
                double distance = graph.shortestPathCost(startLocation, node);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    furthest = node;
                    foundValidPath = true;
                }
            } catch (NoSuchElementException e) {
                // skip unreachable nodes
            }
        }

        if (!foundValidPath) {
            String err = "No location can be reached from " + startLocation;
            //String dist = " Distance from start to end: " +
                    //graph.shortestPathCost(graph.getAllNodes().get(0), graph.getAllNodes().get(graph.getAllNodes().size() - 1));
            throw new NoSuchElementException(err);
	    
        }

        return furthest;
  }
}
