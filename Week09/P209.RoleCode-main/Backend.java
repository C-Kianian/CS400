import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Backend class that implements methods in BackendInterface
 */
public class Backend implements BackendInterface {

  /**
   * variable storing the graph
   */
  private GraphADT<String, Double> graph;

  /**
   * Implementing classes should support the constructor below.
   *
   * @param graph object to store the backend's graph data
   */
  public Backend(GraphADT<String, Double> graph) {
    this.graph = graph;
  }

  /**
   * clears all the nodes and edges in the current graph
   */
  private void clearGraph() {
    List<String> nodes = new ArrayList<>(this.graph.getAllNodes());
    for (String currNode : nodes) {//for every node delete it
      for (String otherNode : nodes){
        this.graph.removeEdge(currNode, otherNode);//remove the edges to the current node
      }
      this.graph.removeNode(currNode);//remove the node
    }
  }

  /**
   * Loads graph data from a dot file.  If a graph was previously loaded, this method should first
   * delete the contents (nodes and edges) of the existing graph before loading a new one.
   *
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
  @Override
  public void loadGraphData(String filename) throws IOException {
    this.clearGraph();//clear graph

    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));//used to read from file
      String line;//line in the file
      reader.readLine();//throw away first line

      // Regular expression pattern to find info on each line
      String regex = "\"([^\"]+)\"\\s*->\\s*\"([^\"]+)\"\\s*\\[seconds=([\\d.]+)\\];";
      //this makes groups of starting location, ending location, and travel time
      //the grouping can be seen by noticing the "", ->, seconds=, [], etc.
      Pattern pattern = Pattern.compile(regex);//makes a pattern object

      while (!(line = reader.readLine()).contains("}")) {//read until closing }
        line = line.trim();//remove extra whitespace
        Matcher matcher = pattern.matcher(line);
        //based on the pattern and regex this matcher find our expected format on the line

        if (matcher.find()) {//checks that the line has the expected pattern
          // Extract the information using regex groups
          String start = matcher.group(1);// start location
          String end = matcher.group(2);// end location
          double time = Double.parseDouble(matcher.group(3));//time between locations

          this.graph.insertNode(start);//add nodes and connection
          this.graph.insertNode(end);
          this.graph.insertEdge(start, end, time);
        }
        else {
          throw new IOException("File does not match expected format");
        }
      }
    } catch (FileNotFoundException e) {
      throw new IOException("File: " + filename + " was not found");
    }
  }

  /**
   * Returns a list of all locations (node data) available in the graph.
   *
   * @return list of all location names
   */
  @Override
  public List<String> getListOfAllLocations() {
    return this.graph.getAllNodes();
  }

  /**
   * Return the sequence of locations along the shortest path from startLocation to endLocation, or
   * an empty list if no such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the nodes along the shortest path from startLocation to endLocation, or an
   * empty list if no such path exists
   */
  @Override
  public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
    try {
      return this.graph.shortestPathData(startLocation, endLocation);//if it exists return
    } catch (NoSuchElementException e) {
      return new ArrayList<>();//start, end, or path DNE so return an empty list indicating impossibility
    }
  }

  /**
   * Return the walking times in seconds between each two nodes on the shortest path from
   * startLocation to endLocation, or an empty list of no such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the walking times in seconds between two nodes along the shortest path from
   * startLocation to endLocation, or an empty list if no such path exists
   */
  @Override
  public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
    try {
      List<String> locations = this.findLocationsOnShortestPath(startLocation, endLocation);
      List<Double> lengths = new ArrayList<>();
      //gets locations that are on shortest path
      for (int i = 0; i < locations.size() - 1; i++) {//get distance between two locations in path
        lengths.add(this.graph.getEdge(locations.get(i), locations.get(i + 1)));
      }
      return lengths;//return all the lengths between two nodes on the path, or nothing if there is no path
    } catch (NoSuchElementException e) {
      return new ArrayList<>();//if start, end, or path DNE return an empty list indicating impossibility
    }
  }

  /**
   * Returns a list of the ten closest destinations that can be reached most quickly when starting
   * from the specified startLocation.
   *
   * @param startLocation the location to find the closest destinations from
   * @return the ten closest destinations from the specified startLocation
   * @throws NoSuchElementException if startLocation does not exist, or if there are no other
   *                                locations that can be reached from there
   */
  @Override
  public List<String> getTenClosestDestinations(String startLocation)
      throws NoSuchElementException {
    if (!this.graph.containsNode(startLocation)) {//start node does not exist
      throw new NoSuchElementException("Start location does not exist");
    }

    List<String> destinations = this.getDestinationsHelper(startLocation);//get destinations

    if (destinations.isEmpty()){
      throw new NoSuchElementException("Unable to find 10 paths from " + startLocation);
      //unable to find 10 destinations
    }
    return destinations;//found 10 destinations, return
  }

  /**
   * Helper method to getTenClosestDestinations(), sorts through all the reachable destinations
   * using a PQ
   *
   * @param startLocation the location to find the closest destinations from
   * @return the ten closest destinations from the specified startLocation, or 0 if can't to find 10
   */
  public List<String> getDestinationsHelper(String startLocation){
    List<String> nodes = this.graph.getAllNodes();//all node in graph
    nodes.remove(startLocation);//do not include start location
    List<String> closestDestinations = new ArrayList<>();//closest 10 locations
    List<String> potentialDestinations = new ArrayList<>();
    //possible closest 10 locations, locations that are = 10th fastest time
    PriorityQueue<Double> times = new PriorityQueue<>();//priority queue to sort fastest paths
    double time; //time to be faster than in order to be added to the list

    for (String node : nodes) {//add all times to priority queue to sort
      try {
        times.add(this.graph.shortestPathCost(startLocation, node));//add to PQ to sort in order
      }
      catch (NoSuchElementException e){
        //ignore for now, means no path exists
      }
    }

    if (times.size() < 10){//means that start location doesn't have 10 paths
      return new ArrayList<>();//return empty arrayList to indicate impossible
    }

    List<Double> timeList = times.stream().sorted().toList();//make the PQ into an array
    time = timeList.get(9); //get the 10th fastest time

    boolean greater;
    boolean equals;

    for (String node : nodes) {
      //adds nodes that are less than 10th fastest time to closestDestinations list
      // nodes that = 10th fastest time go into potentialDestinations list
      try {
        double toReachTime = this.graph.shortestPathCost(startLocation, node);
        greater = toReachTime < time;
        equals = toReachTime == time;

        if (greater) {
          closestDestinations.add(node);//node is less than the 10th shortest time
        }
        if (equals) {
          potentialDestinations.add(node);//node could be added since it is = 10th fastest time
        }
      }
      catch (NoSuchElementException e){
        //ignore for now, means no path exists
      }
    }

    while (closestDestinations.size() < 10 && !potentialDestinations.isEmpty()){
      //add possible destinations until reaching 10
      closestDestinations.add(potentialDestinations.remove(0));
    }

    if (closestDestinations.size() == 10){
      return closestDestinations;//return the closest destinations
    }
    return new ArrayList<>();//return empty arrayList to indicate impossible, can't reach 10 places
  }
}
