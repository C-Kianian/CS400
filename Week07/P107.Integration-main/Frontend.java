import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class that implements the FrontEndInterface and allows the user to interact with the backend
 */
public class Frontend implements FrontendInterface {

  private Scanner scnr;
  private BackendInterface bknd;

  /**
   * constructor for the frontend
   *
   * @param in      - scanner to take user input
   * @param backend - back end to send calls to
   */
  public Frontend(Scanner in, BackendInterface backend) {
    this.scnr = in;
    this.bknd = backend;
  }

  /**
   * Repeatedly gives the user an opportunity to issue new commands until they select Q to quit.
   * Uses the scanner passed to the constructor to read user input.
   */
  @Override
  public void runCommandLoop() {
    boolean quit = false;
    while (!quit) {//wait for valid user input or quit command
      displayMainMenu(); // Display the main menu
      if (scnr.hasNext()) {
        String inputStr = scnr.next();//gets user input
        if (inputStr.length() == 1) {
          char input = inputStr.charAt(0);
          switch (Character.toUpperCase(input)) {
            case 'L' -> loadFile(); //load file
            case 'G' -> getSongs(); //energy filter
            case 'F' -> setFilter(); //dancability filter
            case 'D' -> displayTopFive(); //5 most recent songs
            case 'Q' -> quit = true; // Stop the loop and quit
            default -> {//user inputted something other than expected
              System.out.println("Unexpected command, try again.");
              scnr.nextLine(); //clear input
            }
          }
        } else {
          System.out.println("Unexpected command, try again.");
          scnr.nextLine(); //clear input
        }
      }
    }
  }


  /**
   * Displays the menu of command options to the user.  Giving the user the instructions of entering
   * L, G, F, D, or Q (case-insensitive) to load a file, get songs, set filter, display the top
   * five, or quit.
   */
  @Override
  public void displayMainMenu() {//simply displays the options to the user
    System.out.print("""
        Type one of the following letters (case insensitive) to execute its corresponding command:
        L to [L]oad Song File
        G to [G]et Songs by Energy
        F to [F]ilter Songs by Danceability
        D to [D]isplay five most Recent
        Q to [Q]uit
        Type here:""" + " ");
  }

  /**
   * Provides text-based user interface for prompting the user to select the csv file that they
   * would like to load, provides feedback about whether this is successful vs any errors are
   * encountered. [L]oad Song File
   *
   * When the user enters a valid filename, the file with that name should be loaded. Uses the
   * scanner passed to the constructor to read user input and the backend passed to the constructor
   * to load the file provided by the user. If the backend indicates a problem with finding or
   * reading the file by throwing an IOException, a message is displayed to the user, and they will
   * be asked to enter a new filename.
   */
  @Override
  public void loadFile() {
    System.out.print("Input file name then click enter: ");
    boolean done = false;
    while (!done) {//waits for user to input a valid file
      if (scnr.hasNext()) {
        String input = scnr.next();//gets user input
        try {//tries to pass the user's file to the backend and read it
          bknd.readData(input);
          System.out.println("File: " + input + " read successfully.");
          done = true;
        } catch (IOException e) {//user did not input a valid filename or there was an issue reading
          scnr.nextLine();//clear any input
          System.out.println("There was an issue loading your file: " + e.getMessage());
          System.out.print("Try again --->");
        }
      }
    }
  }

  /**
   * Provides text-based user interface and error handling for retrieving a list of song titles that
   * are sorted by Energy.  The user should be given the opportunity to optionally specify a minimum
   * and/or maximum Energy to limit the number of songs displayed to that range. [G]et Songs by
   * Energy
   *
   * When the user enters only two numbers (pressing enter after each), the first of those numbers
   * should be interpreted as the minimum, and the second as the maximum Energy. Uses the scanner
   * passed to the constructor to read user input and the backend passed to the constructor to
   * retrieve the list of sorted songs.
   */
  @Override
  public void getSongs() {//calls helper method
    System.out.print("""
        Sorting songs by energy, input a minimum and maximum numerical value (null = unbounded)
        Min:""" + " ");
    scnr.nextLine(); //clear input
    getSongsHelper();
  }

  /**
   * ensures the users input are legit (i.e. not strings), Only allows the string input "null" and
   * numerical values. Does this by taking in a string and parsing for an integer, throws away any
   * string that contains more than an int or null by throwing a NumberFormatException, then passes
   * valid inputs to the backend
   */
  private void getSongsHelper() {
    boolean done = false;
    Integer min;
    Integer max;
    while (!done) {//waits for user to input a number or null
      try {
        String input = scnr.nextLine();
        if (input.equals("null")) {//looks for valid user input
          min = null;
        }
        else {
          min = Integer.parseInt(input);
        }

        System.out.print("Max: ");//min is good now get a max bound
        input = scnr.nextLine();
        if (input.equals("null")){//looks for valid user input
          max = null;
        }
        else {
          max = Integer.parseInt(input);
        }

        bknd.getRange(min, max);//call backend method
        System.out.println("Energy bounds are now " + min + " to " + max);
        done = true;
        //successful input, end loop
      } catch (NumberFormatException e) {//user did not input a number or null
        System.out.print("""
            Invalid input, try inputting a number value again, Min:""" + " ");
        //failure, try again
      }
    }
  }

  /**
   * Provides text-based user interface and error handling for setting a filter threshold. This and
   * future requests to retrieve songs will only return the titles of songs that are larger than the
   * user specified Danceability.  The user should also be able to clear any previously specified
   * filters. [F]ilter Songs by Danceability
   *
   * When the user enters only a single number, that number should be used as the new filter
   * threshold. Uses the scanner passed to the constructor to read user input and the backend passed
   * to the constructor to set the filters provided by the user and retrieve songs that maths the
   * filter criteria.
   */
  @Override
  public void setFilter() {//calls the helper method
    System.out.print("Input a numerical " +
        "danceability threshold, songs lower than this will be ignored: ");
    setFilterHelper();
  }

  /**
   * ensures the users input are legit and in the expected range (i.e. no negative nums), does this
   * by catching whenever something other than a string is inputted and then re prompts the user for
   * a new valid input
   */
  private void setFilterHelper() {
    boolean done = false;
    while (!done) {//Continuously iterate through scanner for a valid value, returns if input mismatch
      try {
        int thres = scnr.nextInt();//takes in the valid user input
        System.out.println("Danceability threshold is now: " + thres);
        bknd.setFilter(thres);
        done = true;//end
      } catch (InputMismatchException e) {//user inputted something other than a number, try again
        scnr.nextLine(); //clear input
        System.out.print("""
            Invalid input, try inputting a numerical Danceability threshold again:""" + " ");
      }
    }
  }

  /**
   * Displays the titles of up to five of the most Recent songs within the previously set Energy
   * range and larger than the specified Danceability.  If there are no such songs, then this method
   * should indicate that and recommend that the user change their current range or filter settings.
   * [D]isplay five most Recent
   *
   * The user should not need to enter any input when running this command. Uses the backend passed
   * to the constructor to retrieve the list of up to five songs.
   */
  @Override
  public void displayTopFive() {
    String five = bknd.fiveMost().toString();//gets 5 most songs

    if (five.equals("[]")) {//user has no matching songs
      System.out.println(
          "No songs matching your criteria, update your energy and danceability settings then try again");
    } else {//print matching songs
      System.out.println(
          "These are your most recent songs matching your danceability and energy criteria (up to 5): " + five);
    }
  }
}
