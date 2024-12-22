import java.util.ArrayList;

interface MathOperation {
  public double compute(double a, double b);
}

public class CalculatorApp {

  public static MathOperation mul() {
    // TODO: (1 of 3) Using a standard class
    // Define a class named MultiplicationOperation that implements
    // MathOperation within this Java file, outside of the CalculatorApp
    // class definition. Note that this class cannot be public because it
    // is not defined in a file named MultiplicationOperation.java.
    // Implement the compute method in this class to return the product of
    // its two input parameters. Replace the null return value below with
    // an instance of the MultiplicationOperation class.
    return new MultiplicationOperation();
  }

  public static MathOperation add() {
    // TODO: (2 of 3) Using an anonymous class
    // Replace the null return value below with an instance of an
    // anonymous class that implements MathOperation. Define its compute
    // method to return the sum of its two input parameters.
    return new MathOperation() {
      public double compute(double a, double b) {
        return a + b;
      }
    };
  }

  public static MathOperation sub() {
    // TODO: (3 of 3) Using a lambda expression
    // Replace the null return value below with a lambda expression to
    // create an object with a compute method that returns the difference
    // of the two input parameters (first minus second).
    return (a,b) -> a - b;
  }

  /**
   * DO NOT MAKE ANY CHANGES TO THE MAIN METHOD BELOW FOR THIS ACTIVITY.
   *
   * This main method uses the objects returned by the methods above to
   * display the sum, difference, and product of operands between 1 and 5.
   * @param args is not used by this program
   */
  public static void main(String[] args) {
    // add all math operations to this array
    ArrayList<MathOperation> ops = new ArrayList<>();
    ops.add( mul() );
    ops.add( add() );
    ops.add( sub() );

    // display table of math operations applied to operands
    System.out.println("Operands:  mul  add  sub");
    for(int a = 1; a <= 5; a++)
      for(int b = 1; b <= a; b++) {
        System.out.print("     "+a+","+b+":"); // print operands first
        for(MathOperation op: ops)
          if(op != null) // then print out result of operation
            System.out.printf( "%5.1f", op.compute(a,b) );
          else // or a dash when the operation is null
            System.out.print("    -");
        System.out.println(); // print each operand pair on a new line
      }
  }
}

class MultiplicationOperation implements MathOperation{
  public double compute(double a, double b){
    return (a * b);
  }
}
