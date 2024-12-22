import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Tester class for the frontend implementation
 */
public class FrontendTests {

  /**
   * Testing the load method (Ignore: things to add after backend is updated, quit test and load
   * failure)
   */
  @Test
  public void roleTest1() {
    IterableSortedCollection<Song> tree = new Tree_Placeholder(); //song tree
    Backend_Placeholder bknd = new Backend_Placeholder(tree); //backend

    TextUITester input = new TextUITester("afsdfsdfs\nL\nsongs.csv\nl\nsongs.csv\nq");
    //user input

    Scanner scnr = new Scanner(System.in); //setting up things for testing
    Frontend test = new Frontend(scnr, bknd);

    test.runCommandLoop();
    String output = input.checkOutput();//run program
    System.out.print(output);

    //tests entering a file and quitting
    Assertions.assertTrue(output.contains("Type one of the following letters"),
        "Main menu was not displayed");//test correct ui
    Assertions.assertTrue(output.contains("Input file name then click enter: "),
        "Did not enter loadFile method as expected");//tests entering method
    Assertions.assertTrue(output.contains("File: songs.csv read successfully."),
        "File was not read properly");//tests reading a file
    /*Assertions.assertTrue(output.contains("Thanks, and Goodbye."),
        "Did not quit properly");//tests quitting

     */
  }

  /**
   * Testing the getSongs and setFilter methods and helpers
   */
  @Test
  public void roleTest2() {
    IterableSortedCollection<Song> tree = new Tree_Placeholder(); //song tree
    Backend_Placeholder bknd = new Backend_Placeholder(tree); //backend

    TextUITester input = new TextUITester("g\n100\n10\nbadWord\n1\n1000\nF\ninputMismatch\n10\nQ");
    //user input

    Scanner scnr = new Scanner(System.in); //setting up things for testing
    Frontend test = new Frontend(scnr, bknd);

    test.runCommandLoop();
    String output = input.checkOutput();//run program

    //getSongs method
    Assertions.assertTrue(output.contains("Type one of the following letters"),
        "Main menu was not displayed");//test correct ui
    Assertions.assertTrue(
        output.contains("Sorting songs by energy, input a minimum and maximum value"),
        "Did not enter getSongs method as expected");//test navigating into the method
    Assertions.assertTrue(output.contains("Energy bounds are now"),
        "Bounds were not updated properly");//test for correct bounds

    //setFilter method
    Assertions.assertTrue(
        output.contains("Input danceability threshold, songs lower than this will be ignored"),
        "setFilter method was not entered properly");//test for entering method
    Assertions.assertTrue(
        output.contains("Invalid input, try inputting Danceability threshold again"),
        "input mismatch was not handled correctly");//test for error handling
    Assertions.assertTrue(output.contains("Danceability threshold is now:"),
        "input mismatch was not handled correctly");//test for correct input

    //quit
    /*
    Assertions.assertTrue(output.contains("Thanks, and Goodbye."),
        "Did not quit properly");//tests correct ending of program

     */
  }

  /**
   * test displayTopFive method and error handling of command loop
   */
  @Test
  public void roleTest3() {
    IterableSortedCollection<Song> tree = new Tree_Placeholder(); //song tree
    Backend_Placeholder bknd = new Backend_Placeholder(tree); //backend

    TextUITester input = new TextUITester("DNE\nd\ng\n1000\n2000\nd\nQ");
    //user input

    Scanner scnr = new Scanner(System.in); //setting up things for testing
    Frontend test = new Frontend(scnr, bknd);

    test.runCommandLoop();
    String output = input.checkOutput();//run program

    //displayTopFive method
    Assertions.assertTrue(output.contains("Type one of the following letters"),
        "Main menu was not displayed");//test correct ui
    Assertions.assertTrue(output.contains("Unexpected command, try again."),
        "Didn't catch error");//test correct ui
    Assertions.assertTrue(
        output.contains("These are your most recent songs matching your danceability and energy"),
        "Songs where not displayed");//test correct output for finding results

    //edit the thresholds
    Assertions.assertTrue(
        output.contains("No songs matching your criteria, update your energy and"),
        "Songs where displayed");//test correct output for finding no results

    //quit
    /*
    Assertions.assertTrue(output.contains("Thanks, and Goodbye."),
        "Did not quit properly");//tests correct ending of program

     */
  }
}
