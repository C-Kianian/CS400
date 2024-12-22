import java.util.List;
import java.util.NoSuchElementException;
/**
 * Frontend Class
 */
public class Frontend implements FrontendInterface {

    private BackendInterface backend;

    // Constructor
    public Frontend(BackendInterface backend) { this.backend = backend; }
    
    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="start", for the start location
     * - a text input field with the id="end", for the destination
     * - a button labelled "Find Shortest Path" to request this computation
     * Ensure that these text fields are clearly labelled, so that the user
     * can understand how to use them.
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a shortest path computation
     */
    @Override
    public String generateShortestPathPromptHTML() {
        // Returns HTML with labeled input fields for start and end locations, and a button.
        return "<label for='start'>Start Location:</label>" +
               "<input type='text' id='start' placeholder='Enter start location'>" +
               "<label for='end'>End Location:</label>" +
               "<input type='text' id='end' placeholder='Enter destination'>" +
               "<button onclick='findShortestPath()'>Find Shortest Path</button>";
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the path's start and end locations
     * - an ordered list (ol) of locations along that shortest path
     * - a paragraph (p) that includes the total travel time along this path
     * Or if there is no such path, the HTML returned should instead indicate 
     * the kind of problem encountered.
     * @param start is the starting location to find a shortest path from
     * @param end is the destination that this shortest path should end at
     * @return an HTML string that describes the shortest path between these
     *         two locations
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        List<String> path = backend.findLocationsOnShortestPath(start, end);
        // Hnadling case If no path exists
        if (path.isEmpty()) { return "<p>No path found between " + start + " and " + end + ".</p>"; }

        //Creating html fragment containing path details
        StringBuilder html = new StringBuilder("<p>Shortest path from " + start + " to " + end + ":</p><ol>");
        for (String location : path) {
            html.append("<li>").append(location).append("</li>");
        }
        html.append("</ol>");

        // Calculating Total time travelled
        List<Double> times = backend.findTimesOnShortestPath(start, end);
        // Calculate the total travel time by summing all the travel times in the list
        double totalTime = times.stream().mapToDouble(Double::doubleValue).sum();
        html.append("<p>Total travel time: ").append(totalTime).append(" seconds</p>");

        return html.toString();
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="from", for the start location
     * - a button labelled "Ten Closest Destinations" to submit this request
     * Ensure that this text field is clearly labelled, so that the user
     * can understand how to use it.
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a ten closest destinations calculation
     */    
    @Override
    public String generateTenClosestDestinationsPromptHTML() {
        // Returns HTML with a labeled input field and a button for closest destinations.
        return "<label for='from'>Start Location:</label>" +
               "<input type='text' id='from' placeholder='Enter start location'>" +
               "<button onclick='findTenClosestDestinations()'>Ten Closest Destinations</button>";
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the start location that travel time to
     *        the closest destinations are being measured from
     * - an unordered list (ul) of the ten locations that are closest to start
     * Or if no such destinations can be found, the HTML returned should 
     * instead indicate the kind of problem encountered.
     * @param start is the starting location to find close destinations from
     * @return an HTML string that describes the closest destinations from the
     *         specified start location.
     */    
    @Override
    public String generateTenClosestDestinationsResponseHTML(String start) {
        try {
            //Getting the ten closest destinations 
            List<String> tenClsDest = backend.getTenClosestDestinations(start);
            StringBuilder html = new StringBuilder("<p>Closest destinations from " + start + ":</p><ul>");

            //Building html fragment
            for (String destination : tenClsDest) {
                html.append("<li>").append(destination).append("</li>");
            }
            html.append("</ul>");
            return html.toString();
        } 
        catch (NoSuchElementException e) { //if no such destination found
            return "<p>No such destination from " + start + ".</p>";
        }
    }
}