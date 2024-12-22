import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class BackendTests {
  /**
   * tests loadGraphData method and getListOfAllLocations by loading good and bad data as well as
   * checking for a newly inserted node in the list of all locations
   */
  @Test
  public void backendTest1() {
    Backend back = new Backend(new Graph_Placeholder());//make backend

    try {//tests loading a valid file
      back.loadGraphData("campus.dot");//should load this file
      //passes
    } catch (IOException e) {
      Assertions.assertEquals(0, 1, "does not read a valid file correctly");//fails
    }

    //tests getListOfAllLocations method
    Assertions.assertTrue(back.getListOfAllLocations().contains("Union South"),
        "Does not contain all locations");//checks that expected data is in graph

    try {//tests loading a bad file
      back.loadGraphData("DNE.dot");
      Assertions.assertTrue(true, "Reads an invalid file");//fails, should not load
    } catch (IOException e) {
      //passes
    }
  }

  /**
   * tests findLocationsOnShortestPath and findTimesOnShortestPath methods
   */
  @Test
  public void backendTest2() {
    Backend back = new Backend(new Graph_Placeholder());//make backend

    Assertions.assertTrue(//checks that start location is included in shortest path
        back.findLocationsOnShortestPath("Union South", "Computer Sciences and Statistics")
            .contains("Union South"));

    Assertions.assertTrue(//checks that the distance between two locations is as expected
        back.findTimesOnShortestPath("Union South", "Computer Sciences and Statistics").contains(1.0));
  }

  /**
   * tests getTenClosestDestinations method
   */
  @Test
  public void backendTest3() {
    Backend back = new Backend(new Graph_Placeholder());//make backend

    try {
      List<String> locations = back.getTenClosestDestinations("Union South");
      Assertions.fail("Did not fail when less than 10 locations found");
      //should fail since only 3 total locations exist
    }
    catch (NoSuchElementException e){
      //passes
    }

  }

  /**
   * tests valid paths between locations
   */
  @Test
  public void integrationTest1() {
    Backend bcknd = new Backend(new DijkstraGraph<>());
    Frontend frnt = new Frontend(bcknd);//make the front,back,graph

    try {
      bcknd.loadGraphData("campus.dot");//done twice to ensure the clear method works
      bcknd.loadGraphData("campus.dot");
      //works
    } catch (IOException e) {
      Assertions.fail("Didn't read good data properly");//fails
    }

    String out =
        frnt.generateShortestPathResponseHTML("Wisconsin Institute for Discovery", "Wendt Commons");
    //get path between the two locations

    Assertions.assertTrue(out.contains("<p>Shortest path"),
        "Path was not found but should have been");//ensure a path was found
    //ensure that path contains the correct locations
    Assertions.assertTrue(out.contains("Wendt Commons") && out.contains(
            "Wisconsin Institute for Discovery") && out.contains("Union South"),
        "Path doesn't contain correct locations");
    Assertions.assertTrue(out.contains("237"), "Travel time is not correct");//check travel time
  }

  /**
   * tests finding a path between invalid location(s) or valid location with no path between them
   */
  @Test
  public void integrationTest2() {
    Backend bcknd = new Backend(new DijkstraGraph<>());
    Frontend frnt = new Frontend(bcknd);//make the front,back,graph

    try {
      bcknd.loadGraphData("campus.dot");//done twice to ensure the clear method works
      bcknd.loadGraphData("campus.dot");
      //works
    } catch (IOException e) {
      Assertions.fail("Didn't read good data properly");//fails
    }

    String out = frnt.generateShortestPathResponseHTML("Jorns Hall", "Not a real place");
    //get path between the two locations

    Assertions.assertTrue(out.contains("No path found between"),
        "Path was found but should not have been");//ensure a path was not found

    out = frnt.generateShortestPathResponseHTML("unreal", "Jorns Hall");

    Assertions.assertTrue(out.contains("No path found between"),
        "Path was found but should not have been");//ensure a path was not found

    out = frnt.generateShortestPathResponseHTML("unreal", "DNE");

    Assertions.assertTrue(out.contains("No path found between"),
        "Path was found but should not have been");//ensure a path was not found


    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    Backend bcknd2 = new Backend(graph);
    Frontend frnt2 = new Frontend(bcknd2);//make the front,back,graph

    graph.insertNode("A");//making a new disconnected graph
    graph.insertNode("B");
    graph.insertEdge("A", "B", 1.0);
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertEdge("C", "D", 1.0);

    out = frnt2.generateShortestPathResponseHTML("A", "C");

    Assertions.assertTrue(out.contains("No path found between"),
        "Path was found but should not have been");//ensure a path was not found
  }

  /**
   * Tests not being able to find 10 locations, one with a non-real starting point, and a real start
   * but not being able to reach 10 places
   */
  @Test
  public void integrationTest3() {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    Backend bcknd = new Backend(graph);
    Frontend frnt = new Frontend(bcknd);//make the front,back,graph

    try {
      bcknd.loadGraphData("campus.dot");//done twice to ensure the clear method works
      bcknd.loadGraphData("campus.dot");
      //works
    } catch (IOException e) {
      Assertions.fail("Didn't read good data properly");//fails
    }

    String out = frnt.generateTenClosestDestinationsResponseHTML("Not a real place");
    //find nearest places from here
    Assertions.assertTrue(out.contains("No such destination from"),
        "Didn't fail when given non real start location");

    graph.insertNode("Start");
    graph.insertNode("End");
    graph.insertEdge("Start", "End", 5.0);

    out = frnt.generateTenClosestDestinationsResponseHTML("Start");
    //find nearest places from here
    Assertions.assertTrue(out.contains("No such destination from"),
        "Didn't fail when found less than 10 locations");
  }

  /**
   * Test actually finding 10 locations
   */
  @Test
  public void integrationTest4() {
    Backend bcknd = new Backend(new DijkstraGraph<>());
    Frontend frnt = new Frontend(bcknd);//make the front,back,graph

    try {
      bcknd.loadGraphData("campus.dot");//done twice to ensure the clear method works
      bcknd.loadGraphData("campus.dot");
      //works
    } catch (IOException e) {
      Assertions.fail("Didn't read good data properly");//fails
    }

    String out = frnt.generateTenClosestDestinationsResponseHTML("Memorial Union");

    Assertions.assertTrue(
        out.contains("Closest destinations from"));//ensures 10 locations were found

    ArrayList<String> expected = new ArrayList<>();
    expected.add("Science Hall");
    expected.add("Brat Stand");
    expected.add("Helen C White Hall");
    expected.add("Radio Hall");
    expected.add("Wisconsin State Historical Society");
    expected.add("Education Building");
    expected.add("Water Science & Engineering Laboratory");
    expected.add("North Hall");
    expected.add("Arthur D. Hasler Laboratory of Limnology");

    for (String location: expected){//check each of the nearest locations, ensure they are included
      Assertions.assertTrue(out.contains(location));
    }

  }

}
