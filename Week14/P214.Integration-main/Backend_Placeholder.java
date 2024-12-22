import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * This is a placeholder for the fully working Backend that will be developed
 * by one of your teammates this week and then integrated with your role code
 * in a future week.  It is designed to help develop and test the functionality
 * of your own Frontend role code this week.  Note the limitations described
 * below.
 */
public class Backend_Placeholder implements BackendInterface {

  // Presumably this placeholder is using a placeholder graph that is itself
  // not fully functional.
  GraphADT<String,Double> graph;
  public Backend_Placeholder(GraphADT<String,Double> graph) {
    this.graph = graph; 
  }

  // this method adds a single extra location to the graph when called
  public void loadGraphData(String filename) throws IOException {
    graph.insertNode("Mosse Humanities Building");
  }

  public List<String> getListOfAllLocations() {
    return graph.getAllNodes();
  }

  public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
    return graph.shortestPathData(startLocation,endLocation);
  }

  // returns list of increasing values
  public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
    List<String> locations = graph.shortestPathData(startLocation,endLocation);
    List<Double> times = new ArrayList<>();
    for(int i=0;i<locations.size();i++) times.add(i+1.0);
    return times;
  }
    
  // returns list of all locations
  public List<String> getTenClosestDestinations(String startLocation) throws NoSuchElementException {
    return graph.getAllNodes();
  }

}
