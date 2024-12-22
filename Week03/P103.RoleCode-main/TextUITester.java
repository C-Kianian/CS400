import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class can be used to test text based user interactions by 1) specifying a String of text
 * input (that will be fed to System.in as if entered by the user), and then 2) capturing the output
 * printed to System.out and System.err in String form so that it can be compared to the expect
 * output.
 *
 * @date 2024.09
 */
public class TextUITester {

  /**
   * This main method demonstrates the use of a TextUITester object to check the behavior of the
   * following run method.
   *
   * @param args from the commandline are not used in this example
   */
  public static void main(String[] args) {

    // 1. Create a new TextUITester object for each test, and
    // pass the text that you'd like to simulate a user typing
    // as only argument.  It's IMPORTANT that this be created
    // before you create a Scanner objects reading from System.in
    TextUITester tester = new TextUITester("apple\n3.14\nq\n");

    // 2. Create a Scanner, and run then code that you want to test here:
    run(); // (this code reads from System.in and writes to System.out)

    // Note hat you cannot see output printed by your program between the
    // constructor call above and the checkOutput call below.  This is
    // is useful for your final tests.  For debugging purposes you can
    // pass an extra false argument to the TextUITester's constructor to
    // be able to see what is happening while the test is running: for
    // debugging purposes.

    // 3. Check whether the output printed to System.out is correct
    String output = tester.checkOutput();
    if (output.startsWith("Welcome to the run program.") && output.contains("apple4.14"))
      System.out.println("Test passed.");
    else
      System.out.println("Test FAILED.");
  }

  /**
   * This is the code being tested by the main method above. It 1) prints out a welcome message, 2)
   * reads a String, a double, and a character from System.in, and then 3) prints out the string, 4)
   * if the char is a lowercase q, it prints out a number that is one larger than the entered double
   * value.
   */
  public static void run() {
    // Note: AVOID instantiating more than one Scanner reading from
    // System.in, as this leads to undefined behavior!
    Scanner in = new Scanner(System.in);
    System.out.println("Welcome to the run program.");
    System.out.println("Enter a string, a double, and then q to quit: ");
    String s = in.nextLine();
    double d = in.nextDouble();
    in.nextLine(); // read newline after double
    char c = in.nextLine().charAt(0);
    if (c == 'q')
      System.out.println(s + (d + 1.0));
    in.close();
  }

  // Below is the code that actually implements the redirection of System.in
  // and System.out.  You are welcome to ignore this code: focusing instead
  // on how the constructor and checkOutput() method are used in the example
  // above.

  // save initial stream references
  private PrintStream saveSystemOut;
  private PrintStream saveSystemErr;
  private InputStream saveSystemIn;
  // streams to use in place of those standard ones
  private ByteArrayOutputStream redirectedOut;
  private ByteArrayOutputStream redirectedErr;

  private boolean hideOutput;

  /**
   * Creates a new test object with the specified string of simulated user input text.  The output
   * printed to System.out will be hidden, if true is passed for the second argument.  This can be
   * helpful to change while debugging a failing test.
   *
   * @param programInput the String of text that you want to simulate being typed in by the user.
   * @param hideOutput   determines whether this test's output is hidden from System.out while the
   *                     test is running.
   */
  public TextUITester(String programInput, boolean hideOutput) {
    // backup standard io before redirecting for tests
    this.saveSystemOut = System.out;
    this.saveSystemErr = System.err;
    this.saveSystemIn = System.in;
    this.hideOutput = hideOutput;

    // create alternative location to write output, and to read input from
    this.redirectedOut = new ByteArrayOutputStream();
    if (hideOutput) {
      System.setOut(new PrintStream(this.redirectedOut));
    } else {
      this.saveSystemOut.println("TextUITester's capture starts:");
      System.setOut(new PrintStream(new SplitStream(this.saveSystemOut, this.redirectedOut)));
    }
    this.redirectedErr = new ByteArrayOutputStream();
    if (hideOutput) {
      System.setErr(new PrintStream(this.redirectedErr));
    } else {
      System.setErr(new PrintStream(new SplitStream(this.saveSystemErr, this.redirectedErr)));
    }
    System.setIn(new ByteArrayInputStream(programInput.getBytes()));
  }

  // hide System.out by default to keep test output more clean and clear
  public TextUITester(String programInput) {
    this(programInput, true);
  }

  /**
   * Call this method after running your test code to check whether the expected text was printed
   * out to System.out and System.err.  Calling this method will also un-redirect standard io, so
   * that the console can be used as normal again.
   *
   * @return captured text that was printed to System.out and System.err durring test.
   */
  public String checkOutput() {
    try {
      String programOutput = redirectedOut.toString() + redirectedErr.toString();
      return programOutput;
    } finally {
      // restore standard io to their pre-test states
      if (!this.hideOutput)
        this.saveSystemOut.println("TextUITester's capture ends.");
      System.out.close();
      System.setOut(saveSystemOut);
      System.err.close();
      System.setErr(saveSystemErr);
      System.setIn(saveSystemIn);
    }
  }

  /**
   * This helper class splits the output stream that System.out is directed to so that it can both
   * be captured and displayed (for debugging purposes) at the same time.
   */
  private static class SplitStream extends OutputStream {
    private OutputStream left;
    private OutputStream right;

    public SplitStream(OutputStream left, OutputStream right) {
      this.left = left;
      this.right = right;
    }

    public void write(byte[] bytes) throws IOException {
      this.left.write(bytes);
      this.right.write(bytes);
    }

    public void write(byte[] bytes, int offset, int length) throws IOException {
      this.left.write(bytes, offset, length);
      this.right.write(bytes, offset, length);
    }

    public void write(int oneByte) throws IOException {
      left.write(oneByte);
      right.write(oneByte);
    }
  }
}
