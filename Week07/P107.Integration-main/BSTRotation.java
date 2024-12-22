/**
 * This class implements the rotation method of a BST and includes some tests to ensure the method
 * is working
 *
 * @param <T> type of the BST
 */
public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a left child of the provided parent, this method will perform a right rotation. When the
   * provided child is a right child of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will either
   * throw a NullPointerException: when either reference is null, or otherwise will throw an
   * IllegalArgumentException.
   *
   * @param child  is the node being rotated from child to parent position
   * @param parent is the node being rotated from parent to child position
   * @throws NullPointerException     when either passed argument is null
   * @throws IllegalArgumentException when the provided child and parent nodes are not initially
   *                                  (pre-rotation) related that way
   */
  protected void rotate(BSTNode<T> child, BSTNode<T> parent)
      throws NullPointerException, IllegalArgumentException {

    if (child != null && parent != null) { //ensure no null arguments
      if (parent.getLeft() != null && parent.getLeft().equals(child)) { //right rotation

        if (parent.getUp() != null) { //not the root of the tree
          BSTNode<T> grandparent = parent.getUp();
          if (grandparent.getRight() == parent) {//new right child
            grandparent.setRight(child);
            child.setUp(grandparent);
          } else {//new left child
            grandparent.setLeft(child);
            child.setUp(grandparent);
          }
        } else {//root of the tree
          child.setUp(null);
          super.root = child;
        }


        if (child.getRight() != null) { //moving child's right subtree to parent's new left subtree
          BSTNode<T> rightTreeChild = child.getRight();
          parent.setLeft(rightTreeChild);
          rightTreeChild.setUp(parent);
        } else { //if DNE set to null
          parent.setLeft(null);
        }
        child.setRight(parent); //set up the swapped relationship
        parent.setUp(child);

      } else if (parent.getRight() != null && parent.getRight().equals(child)) { //left rotation

        if (parent.getUp() != null) { //not the root of the tree
          BSTNode<T> grandparent = parent.getUp();
          if (grandparent.getRight() == parent) {//new right child
            grandparent.setRight(child);
            child.setUp(grandparent);
          } else {//new left child
            grandparent.setLeft(child);
            child.setUp(grandparent);
          }
        } else {//root of the tree
          child.setUp(null);
          super.root = child;
        }

        if (child.getLeft() != null) { //moving child's left subtree to parent's new right subtree
          BSTNode<T> leftTreeChild = child.getLeft();
          parent.setRight(leftTreeChild);
          leftTreeChild.setUp(parent);
        } else { //if DNE set to null
          parent.setRight(null);
        }
        child.setLeft(parent); //set up the swapped relationship
        parent.setUp(child);

      } else { //nodes are not related
        throw new IllegalArgumentException(
            "The provided child and parent nodes are not initially related.");
      }
    } else { //DNE
      throw new NullPointerException("Either parent or child is null.");
    }
  }

  /**
   * testing the rotation method on a simple BST with 0-1 shared children, also tests the error
   * handling of the rotation method
   *
   * @return
   */
  public boolean test1() { //simple left right rotations, 0 shared children
    BSTRotation<Integer> test = new BSTRotation<>();
    test.insert(2);
    test.insert(1);
    test.insert(3);

    //error checks

    try {//checking unrelated nodes
      test.rotate(test.root.getLeft(), test.root.getRight());

      return false;
    } catch (IllegalArgumentException e) {
      //works
    }

    try {//checking null reference handling
      test.rotate(null, test.root.getRight());
      test.rotate(test.root.getLeft(), null);

      return false;
    } catch (NullPointerException e) {
      //works
    }

    if (!test.root.getData().equals(2) || !test.root.getLeft().getData()
        .equals(1) || !test.root.getRight().getData().equals(3)) {
      //ensure correct shape
      return false;
    }

    test.rotate(test.root.getLeft(), test.root); //right rotation
    BSTNode<Integer> right = test.root.getRight();

    if (!test.root.getData().equals(1) || !right.getData().equals(2) || !right.getRight().getData()
        .equals(3)) {
      return false; //ensure correct structure
    }

    test.rotate(test.root.getRight(), test.root); //reset tree

    if (!test.root.getData().equals(2) || !test.root.getRight().getData()
        .equals(3) || !test.root.getLeft().getData().equals(1)) {
      return false; //ensure correct structure
    }

    test.rotate(test.root.getRight(), test.root); //left rotation
    BSTNode<Integer> left = test.root.getLeft();

    if (!test.root.getData().equals(3) || !left.getData().equals(2) || !left.getLeft().getData()
        .equals(1)) {
      return false; //ensure correct structure
    }


    return true; //all test pass
  }

  /**
   * tests the rotation method on a more complex BST where there is 1-2 shared children, also checks
   * for the correct connections between nodes
   *
   * @return true if all tests pass
   */
  public boolean test2() { //rotating on non-root nodes, 1-2 shared children
    BSTRotation<Integer> test = new BSTRotation<>();
    test.insert(5);
    test.insert(3);
    test.insert(7);
    test.insert(4);
    test.insert(2);

    if (!test.root.getData().equals(5) || !test.root.getLeft().getData()
        .equals(3) || !test.root.getRight().getData().equals(7)) {
      //ensure correct shape
      return false;
    }

    test.rotate(test.root.getLeft().getRight(),
        test.root.getLeft()); //left rotation on left subtree
    BSTNode<Integer> left = test.root.getLeft();

    if (!left.getData().equals(4) || !left.getLeft().getData().equals(3) || !left.getLeft()
        .getLeft().getData().equals(2)) { //ensure correct structure
      return false;
    }

    if (!left.getLeft().getUp().equals(left)
        || !left.getLeft().getLeft().getUp().equals(left.getLeft())
        || !left.getUp().equals(test.root)) {
      return false; //ensure correct connections
    }

    test.rotate(test.root.getLeft().getLeft(), test.root.getLeft()); //reset, right rotation
    left = test.root.getLeft();

    if (!left.getData().equals(3) || !left.getRight().getData().equals(4) || !left.getLeft()
        .getData().equals(2)) { //ensure correct structure
      return false;
    }

    test.rotate(test.root.getLeft().getLeft(),
        test.root.getLeft()); //right rotation on left subtree
    left = test.root.getLeft();

    if (!left.getData().equals(2) || !left.getRight().getData().equals(3) || !left.getRight()
        .getRight().getData().equals(4)) { //ensure correct structure
      return false;
    }

    if (!left.getRight().getUp().equals(left)
        || !left.getRight().getRight().getUp().equals(left.getRight())
        || !left.getUp().equals(test.root)) {
      return false; //ensure correct connections
    }


    return true; //all test pass
  }


  /**
   * tests the rotation method on a more complex BST where there can be 3-5 shared children
   *
   * @return true if all tests pass
   */
  public boolean test3() { //multiple children, 3 shared children
    BSTRotation<Integer> test = new BSTRotation<>();
    test.insert(5);
    test.insert(3);
    test.insert(7);
    test.insert(4);
    test.insert(2);
    test.insert(6);
    test.insert(8);
    test.insert(1);
    test.insert(9);

    String actual = test.root.toLevelOrderString();
    String expected = "[ 5, 3, 7, 2, 4, 6, 8, 1, 9 ]";

    if (!expected.equals(actual)){ //check for same structure
      return false;
    }

    test.rotate(test.root.getLeft(), test.root); //right rotation

    actual = test.root.toLevelOrderString();
    expected = "[ 3, 2, 5, 1, 4, 7, 6, 8, 9 ]";

    //System.out.println(expected + "\n" + actual);

    if (!expected.equals(actual)){ //check for same structure
      return false;
    }

    test.rotate(test.root.getRight(), test.root); //reset

    actual = test.root.toLevelOrderString();
    expected = "[ 5, 3, 7, 2, 4, 6, 8, 1, 9 ]";

    if (!expected.equals(actual)){ //check for same structure
      return false;
    }

    test.rotate(test.root.getRight(), test.root); //left rotation

    actual = test.root.toLevelOrderString();
    expected = "[ 7, 5, 8, 3, 6, 9, 2, 4, 1 ]";

    if (!expected.equals(actual)){ //check for same structure
      return false;
    }

    return true; //all test pass
  }


  public static void main(String[] args) {
    BSTRotation<Integer> test = new BSTRotation<>();
    System.out.println("Test 1: " + test.test1());
    System.out.println("Test 2: " + test.test2());
    System.out.println("Test 3: " + test.test3());
  }
}
