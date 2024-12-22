import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This class extends the RedBlackTree class to run submission check on it.
 */
public class P104SubmissionChecker extends RedBlackTree<Integer> {

  /**
   * Submission check that checks if the root node of a newly created tree is null.
   *
   * @return true is check passes, false if it fails
   */
  @Test
  public void submissionCheckerSmallTree() {
    RedBlackTree<String> tree = new RedBlackTree<>();
    tree.insert("a");
    tree.insert("b");
    tree.insert("c");
    Assertions.assertTrue(tree.root.toLevelOrderString().equals("[ b(b), a(r), c(r) ]"));
  }

  /**
   * test case one, red unc
   */
  @Test
  public void testC1() {
    RedBlackTree<Character> test = new RedBlackTree<>();
    test.insert('c'); //making new tree
    test.insert('b');
    test.insert('e');
    test.insert('a');

    String actual = test.root.toLevelOrderString();
    String expected = "[ c(b), b(b), e(b), a(r) ]";
    //test case 1

    Assertions.assertEquals(actual, expected, "tree was not correct after case 1");
  }

  /**
   * test case two, black unc with line formation
   */
  @Test
  public void testC2() {
    RedBlackTree<Character> test = new RedBlackTree<>();
    test.insert('c'); //making new tree
    test.insert('b');
    test.insert('a');

    String actual = test.root.toLevelOrderString();
    String expected = "[ b(b), a(r), c(r) ]";
    //test case 2

    Assertions.assertEquals(actual, expected, "tree was not correct after case 2");

  }

  /**
   * includes q3 from prairie learn, also ensures that ensureRedProperty works for multiple
   * consecutive violations, in addition to testing a black unc skew case (case 3)
   */
  @Test
  public void testC3() {
    RedBlackTree<Character> test = new RedBlackTree<>();
    test.insert('O'); //making new tree
    test.insert('S');
    RBTNode<Character> e = new RBTNode<>('E');
    test.root.setLeft(e);
    e.setUp(test.root);
    test.insert('W');
    RBTNode<Character> c = new RBTNode<>('C');
    e.setLeft(c);
    c.setUp(e);
    RBTNode<Character> k = new RBTNode<>('K');
    e.setRight(k);
    k.setUp(e);
    e.flipColor();//make e red
    c.flipColor();//make c and k black
    k.flipColor();

    //ensure tree is set up correctly
    String q3Tree = "[ O(b), E(r), S(b), C(b), K(b), W(r) ]";
    String actual = test.root.toLevelOrderString();
    Assertions.assertEquals(q3Tree, actual, "Tree not set up correctly");

    test.insert('V');//question 3
    String expected = "[ O(b), E(r), V(b), C(b), K(b), S(r), W(r) ]";
    actual = test.root.toLevelOrderString();
    Assertions.assertEquals(actual, expected, "tree was not correct after case 3");

    //Testing a case where ensureRedProp is used recursively
    RedBlackTree<Integer> tree = new RedBlackTree<>();
    tree.insert(8);
    tree.insert(5);
    tree.insert(15);
    tree.insert(12);
    tree.insert(19);
    tree.insert(23);
    tree.insert(9);
    tree.insert(13);

    tree.insert(10);//causes recursive fixing by moving violation up the large tree

    actual = tree.root.toLevelOrderString();
    expected = "[ 12(b), 8(r), 15(r), 5(b), 9(b), 13(b), 19(b), 10(r), 23(r) ]";
    //
    Assertions.assertEquals(actual, expected, "tree was not correct after recursive case");
  }

  /**
   * Override for the ensureRedProperty method with required signature. This will cause compilation
   * to fail if the method does not exist.
   */
  @Override
  protected void ensureRedProperty(RBTNode<Integer> newRedNode) {
  }

}
