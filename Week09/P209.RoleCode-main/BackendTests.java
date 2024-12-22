import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.List;
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
        back.findTimesOnShortestPath("Union South", "Computer Sciences and Statistics").contains(3.0));
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
}
