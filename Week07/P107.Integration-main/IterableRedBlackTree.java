import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it stores in
 * sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>> extends RedBlackTree<T>
    implements IterableSortedCollection<T> {

  private Comparable<T> max = null; //maximum for the iterator, or null if no maximum is set.
  private Comparable<T> min = null; //minimum for the iterator, or null if no minimum is set.

  /**
   * Testing with a start and stop point also including duplicates
   */
  @Test
  public void test5(){
    IterableRedBlackTree<Character> tree = new IterableRedBlackTree<>();
    tree.setIteratorMax('h');//set min and max
    tree.setIteratorMin('d');
    tree.insert('f'); //inert vals into rbt
    tree.insert('h');
    tree.insert('d');
    tree.insert('g');
    tree.insert('i');
    tree.insert('e');
    tree.insert('c');
    tree.insert('d');
    tree.insert('i');

    RBTIterator<Character> test = (RBTIterator<Character>) tree.iterator();//make iterator

    String actual = ""; //make the actual string by iterating over the tree
    while (test.hasNext()){
      actual = actual.concat(test.next().toString().concat(", "));
    }

    String expected = "d, d, e, f, g, h, ";//expected output

    Assertions.assertEquals(expected, actual, "Actual did not match expected");
  }

  /**
   * Testing with a start and no stop, also no start and a stop
   */
  @Test
  public void test6(){
    IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
    tree.insert(7); //inert vals into rbt
    tree.insert(9);
    tree.insert(5);
    tree.insert(8);
    tree.insert(10);
    tree.insert(10);
    tree.insert(6);
    tree.insert(4);
    tree.insert(4);
    tree.setIteratorMin(6);//set min
    RBTIterator<Integer> test = (RBTIterator<Integer>) tree.iterator();//make iterator

    String actual = "";//make the actual string by iterating over the tree
    while (test.hasNext()){
      actual = actual.concat(test.next().toString().concat(", "));
    }

    String expected = "6, 7, 8, 9, 10, 10, ";//expected output

    Assertions.assertEquals(expected, actual, "Actual did not match expected");

    tree.clear();//clear tree
    tree.insert(7); //inert vals into rbt
    tree.insert(9);
    tree.insert(5);
    tree.insert(8);
    tree.insert(10);
    tree.insert(10);
    tree.insert(6);
    tree.insert(4);
    tree.insert(4);
    tree.setIteratorMin(null);//reset min set max
    tree.setIteratorMax(8);
    test = (RBTIterator<Integer>) tree.iterator();//make iterator

    actual = "";//make the actual string by iterating over the tree
    while (test.hasNext()){
      actual = actual.concat(test.next().toString().concat(", "));
    }

    expected = "4, 4, 5, 6, 7, 8, ";//expected output

    Assertions.assertEquals(expected, actual, "Actual did not match expected");

  }

  /**
   * Testing with no start or stop, also uses a different comparable type than the lest test
   */
  @Test
  public void test7(){
    IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();
    tree.insert("Duck");
    tree.insert("Bear");
    tree.insert("Frog");
    tree.insert("Zebra");
    tree.insert("Elephant");
    tree.insert("Cow");
    tree.insert("Ant");
    RBTIterator<String> test = (RBTIterator<String>) tree.iterator();//make iterator

    String actual = "";//make the actual string by iterating over the tree
    while (test.hasNext()){
      actual = actual.concat(test.next().concat(", "));
    }

    String expected = "Ant, Bear, Cow, Duck, Elephant, Frog, Zebra, ";//expected output

    Assertions.assertEquals(expected, actual, "Actual did not match expected");
  }

  /**
   * Allows setting the start (minimum) value of the iterator. When this method is called, every
   * iterator created after it will use the minimum set by this method until this method is called
   * again to set a new minimum value.
   *
   * @param min the minimum for iterators created for this tree, or null for no minimum
   */
  public void setIteratorMin(Comparable<T> min) {
    this.min = min;//set min val
  }

  /**
   * Allows setting the stop (maximum) value of the iterator. When this method is called, every
   * iterator created after it will use the maximum set by this method until this method is called
   * again to set a new maximum value.
   *
   * @param max the maximum for iterators created for this tree, or null for no maximum
   */
  public void setIteratorMax(Comparable<T> max) {
    this.max = max;//set max val
  }

  /**
   * Returns an iterator over the values stored in this tree. The iterator uses the start (minimum)
   * value set by a previous call to setIteratorMin, and the stop (maximum) value set by a previous
   * call to setIteratorMax. If setIteratorMin has not been called before, or if it was called with
   * a null argument, the iterator uses no minimum value and starts with the lowest value that
   * exists in the tree. If setIteratorMax has not been called before, or if it was called with a
   * null argument, the iterator uses no maximum value and finishes with the highest value that
   * exists in the tree.
   */
  public Iterator<T> iterator() {
    return new RBTIterator<>(this.root, this.min, this.max);//create and return new iterator
  }

  /**
   * Nested class for Iterator objects created for this tree and returned by the iterator method.
   * This iterator follows an in-order traversal of the tree and returns the values in sorted,
   * ascending order.
   */
  protected static class RBTIterator<R> implements Iterator<R> {

    // stores the start point (minimum) for the iterator
    Comparable<R> min = null;
    // stores the stop point (maximum) for the iterator
    Comparable<R> max = null;
    // stores the stack that keeps track of the inorder traversal
    Stack<BSTNode<R>> stack;

    /**
     * Constructor for a new iterator if the tree with root as its root node, and min as the start
     * (minimum) value (or null if no start value) and max as the stop (maximum) value (or null if
     * no stop value) of the new iterator.
     *
     * @param root root node of the tree to traverse
     * @param min  the minimum value that the iterator will return
     * @param max  the maximum value that the iterator will return
     */
    public RBTIterator(BSTNode<R> root, Comparable<R> min, Comparable<R> max) {
      this.stack = new Stack<>();
      this.buildStackHelper(root);
      this.min = min;
      this.max = max;
    }

    /**
     * Helper method for initializing and updating the stack. This method both - finds the next data
     * value stored in the tree (or subtree) that is bigger than or equal to the specified start
     * point (maximum), and - builds up the stack of ancestor nodes that contain values larger than
     * or equal to the start point so that those nodes can be visited in the future.
     *
     * @param node the root node of the subtree to process
     */
    private void buildStackHelper(BSTNode<R> node) {
      if (node != null) {//recursive cases
        if (this.min == null || this.min.compareTo(node.getData()) <= 0) {
          this.stack.push(node);
          this.buildStackHelper(node.getLeft()); //go left
        } else {
          this.buildStackHelper(node.getRight()); //go right
        }
      }
      //base case, node is null
    }

    /**
     * Returns true if the iterator has another value to return, and false otherwise.
     */
    public boolean hasNext() {
      while (!stack.isEmpty()) {
        BSTNode<R> current = this.stack.peek();
        if (this.max == null || this.max.compareTo(current.getData()) >= 0) {
          //current is in range
          return true;
        }
        this.stack.pop();//otherwise remove non-matching, ignore its right tree, won't match
      }
      return false;//stack empty
    }

    /**
     * Returns the next value of the iterator.
     *
     * @throws NoSuchElementException if the iterator has no more values to return
     */
    public R next() {
      while (!this.stack.isEmpty()) {
        BSTNode<R> next = this.stack.pop();//next node to remove
        R nextData = next.getData();//next node data

        this.buildStackHelper(next.getRight());//add right subtree to stack

        boolean greater = this.min == null || this.min.compareTo(nextData) <= 0;
        //next item is greater than the min
        boolean less = this.max == null || this.max.compareTo(nextData) >= 0;
        //next item is less than the max
        if (greater && less) {//next item is in range, return
          return nextData;
        }
      }
      throw new NoSuchElementException("the iterator has no more values to return");
      //no more matches, stack is empty
    }

  }

}
