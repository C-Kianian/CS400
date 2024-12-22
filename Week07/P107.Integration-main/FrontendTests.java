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
        output.contains("Sorting songs by energy, input a minimum and maximum"),
        "Did not enter getSongs method as expected");//test navigating into the method
    Assertions.assertTrue(output.contains("Energy bounds are now"),
        "Bounds were not updated properly");//test for correct bounds

    //setFilter method
    Assertions.assertTrue(
        output.contains("Input a numerical danceability threshold"),
        "setFilter method was not entered properly");//test for entering method
    Assertions.assertTrue(
        output.contains("Invalid input, try inputting a numerical Danceability"),
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
        output.contains("songs"),
        "Songs were not displayed");//test correct output for finding results

    //edit the thresholds
    Assertions.assertTrue(
        output.contains("No songs matching your criteria, update your energy and"),
        "Songs were displayed");//test correct output for finding no results

    //quit
    /*
    Assertions.assertTrue(output.contains("Thanks, and Goodbye."),
        "Did not quit properly");//tests correct ending of program

     */
  }

  /**
   * This method test loading songs.csv and checking the most recent songs
   */
  @Test
  public void integrationTest1(){
    IterableSortedCollection<Song> tree = new IterableRedBlackTree<>(); //song tree
    Backend bknd = new Backend(tree); //backend

    TextUITester input = new TextUITester("DNE\nL\nbadInpt\nsongs.csv\nd\nQ");
    //user input

    Scanner scnr = new Scanner(System.in); //setting up things for testing
    Frontend test = new Frontend(scnr, bknd);

    test.runCommandLoop();
    String output = input.checkOutput();//run program

    Assertions.assertTrue(
        output.contains("Type one of the following letters (case insensitive"),
        "Incorrect UI");//test correct UI
    Assertions.assertTrue(
        output.contains("Unexpected command, try again."),
        "Did not handle bad input as expected");//test error handling
    Assertions.assertTrue(
        output.contains("Input file name then click enter"),
        "Did not enter the loadFile method");//test entering the loadFile method
    Assertions.assertTrue(
        output.contains("There was an issue loading your file"),
        "Did not enter the loadFile method");//test bad file input
    Assertions.assertTrue(
        output.contains("read successfully"),
        "Did not enter the loadFile method");//test good file input
    Assertions.assertTrue(
        output.contains("These are your most recent songs matching your danceability and energy"),
        "Did not display top 5 correctly");//test displayTopFive method


  }

  /**
   * tests loading songs.csv then getSongs with backwards bounds, should result in no matches
   */
  @Test
  public void integrationTest2(){
    IterableSortedCollection<Song> tree = new IterableRedBlackTree<>(); //song tree
    Backend bknd = new Backend(tree); //backend

    TextUITester input = new TextUITester("DNE\nL\nbadInpt\nsongs.csv\ng\n200\n1\nd\nQ");
    //user input

    Scanner scnr = new Scanner(System.in); //setting up things for testing
    Frontend test = new Frontend(scnr, bknd);

    test.runCommandLoop();
    String output = input.checkOutput();//run program

    Assertions.assertTrue(
        output.contains("Type one of the following letters (case insensitive"),
        "Incorrect UI");//test correct UI
    Assertions.assertTrue(
        output.contains("Unexpected command, try again."),
        "Did not handle bad input as expected");//test error handling
    Assertions.assertTrue(
        output.contains("Input file name then click enter"),
        "Did not enter the loadFile method");//test entering the loadFile method
    Assertions.assertTrue(
        output.contains("There was an issue loading your file"),
        "Did not enter the loadFile method");//test bad file input
    Assertions.assertTrue(
        output.contains("read successfully"),
        "Did not enter the loadFile method");//test good file input
    Assertions.assertTrue(
        output.contains("Sorting songs by energy"),
        "Did not enter getSongs() method");//test getSongs method
    Assertions.assertTrue(
        output.contains("No songs matching your criteria"),
        "Found matching songs");//testing that no matching songs were found

  }

  /**
   * testing loading songs.csv then setFilter with very high bound, should result in no matches
   */
  @Test
  public void integrationTest3(){
    IterableSortedCollection<Song> tree = new IterableRedBlackTree<>(); //song tree
    Backend bknd = new Backend(tree); //backend

    TextUITester input = new TextUITester("DNE\nL\nbadInpt\nsongs.csv\nf\n1000\nd\nQ");
    //user input

    Scanner scnr = new Scanner(System.in); //setting up things for testing
    Frontend test = new Frontend(scnr, bknd);

    test.runCommandLoop();
    String output = input.checkOutput();//run program

    Assertions.assertTrue(
        output.contains("Type one of the following letters (case insensitive"),
        "Incorrect UI");//test correct UI
    Assertions.assertTrue(
        output.contains("Unexpected command, try again."),
        "Did not handle bad input as expected");//test error handling
    Assertions.assertTrue(
        output.contains("Input file name then click enter"),
        "Did not enter the loadFile method");//test entering the loadFile method
    Assertions.assertTrue(
        output.contains("There was an issue loading your file"),
        "Did not enter the loadFile method");//test bad file input
    Assertions.assertTrue(
        output.contains("read successfully"),
        "Did not enter the loadFile method");//test good file input
    Assertions.assertTrue(
        output.contains("songs lower than this will be ignored"),
        "Did not enter setFilter() method");//test setFilter method
    Assertions.assertTrue(
        output.contains("No songs matching your criteria"),
        "Found matching songs");//testing that no matching songs were found

  }

  /**
   * testing loading songs.csv then setFilter and getSongs with very specific values, looking for
   * one song in particular
   */
  @Test
  public void integrationTest4(){
    IterableSortedCollection<Song> tree = new IterableRedBlackTree<>(); //song tree
    Backend bknd = new Backend(tree); //backend

    TextUITester input = new TextUITester("DNE\nL\nbadInpt\nsongs.csv\nf\n95\ng\n59\n61\nd\nQ");
    //user input

    Scanner scnr = new Scanner(System.in); //setting up things for testing
    Frontend test = new Frontend(scnr, bknd);

    test.runCommandLoop();
    String output = input.checkOutput();//run program

    Assertions.assertTrue(//finds our one match
        output.contains("Anaconda"),
        "Did not find the one matching song");//testing that the one song was found

  }
}
