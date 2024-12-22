/**
 * This class implements the necessary functions for creating and maintaining a BST
 *
 * @param <T> type of the BST
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

  //root of the tree
  protected BSTNode<T> root;

  public BinarySearchTree() {
    this.root = null;
  }

  /**
   * Performs the naive binary search tree insert algorithm to recursively insert the provided
   * newNode (which has already been initialized with a data value) into the provided tree/subtree.
   * When the provided subtree is null, this method does nothing.
   */
  protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
    int newPos = newNode.getData().compareTo(subtree.getData());//where to insert new node

    if (newPos > 0) { //greater than the root of subtree, go right
      if (subtree.getRight() == null) { //add to right of subtree if it doesn't have a right child
        subtree.setRight(newNode);
        newNode.setUp(subtree); //base case
      } else {
        insertHelper(newNode, subtree.getRight());//traverse right, try again, recursive case
      }
    } else { //less than or = to the root of subtree, go left
      if (subtree.getLeft() == null) {//add to left of subtree if it doesn't have a left child
        subtree.setLeft(newNode);
        newNode.setUp(subtree); //base case
      } else {
        insertHelper(newNode, subtree.getLeft());//traverse left, try again, recursive case
      }
    }
  }

  /**
   * Inserts a new data value into the sorted collection.
   *
   * @param data the new value to be inserted
   * @throws NullPointerException if data argument is null, we do not allow null values to be stored
   *                              within a SortedCollection
   */
  @Override
  public void insert(T data) throws NullPointerException {
    if (data != null) {
      if (this.isEmpty()) {
        this.root = new BSTNode<>(data);
      } else {
        BSTNode<T> newNode = new BSTNode<>(data);
        this.insertHelper(newNode, this.root);
      }
    } else {
      throw new NullPointerException("Data argument is null, we do not allow null values bru");
    }
  }

  /**
   * Check whether data is stored in the tree.
   *
   * @param data the value to check for in the collection
   * @return true if the collection contains data one or more times, and false otherwise
   */
  @Override
  public boolean contains(Comparable<T> data) {
    if (data != null) {
      if (!this.isEmpty()) {
        return this.containsHelper(data, this.root);
      }
    }

    return false;
  }

  /**
   * helper method to search through a subtree
   *
   * @param data value to look for
   * @param subtree subtree to search
   * @return true or false if value is found
   */
  private boolean containsHelper(Comparable<T> data, BSTNode<T> subtree) {
    int comp = data.compareTo(subtree.getData());

    if (comp == 0) {
      return true; //base case, found
    }
    else if (comp > 0) {//value should be on the right of the subtree
      if (subtree.getRight() != null) {
        return (containsHelper(data, subtree.getRight())); //recursive case
      } else {
        return false; //base case
      }
    }
    else {//value should be on the left of the subtree
      if (subtree.getLeft() != null) {
        return (containsHelper(data, subtree.getLeft())); //recursive case
      } else {
        return false; //base case
      }
    }
  }

  /**
   * Counts the number of values in the collection, with each duplicate value being counted
   * separately within the value returned.
   *
   * @return the number of values in the collection, including duplicates
   */
  @Override
  public int size() {
    if (!this.isEmpty()){
      return sizeHelper(this.root);
    }

    return 0;
  }


  /**
   * traverses the tree recursively to find its size
   *
   * @return the number of values in the collection, including duplicates
   */
  private int sizeHelper(BSTNode<T> subtree) {
    int size = 1;//just the root of subtree

    if (subtree.getRight() != null){ //go right first
      size = size + sizeHelper(subtree.getRight());
    }
    if (subtree.getLeft() != null){ //go left second
      size = size + sizeHelper(subtree.getLeft()); //recursive case, until
    }

    return size; //base case
  }

  /**
   * Checks if the collection is empty.
   *
   * @return true if the collection contains 0 values, false otherwise
   */
  @Override
  public boolean isEmpty() {
    //check the root, if root DNE the tree is empty
    return this.root == null;
  }

  /**
   * Removes all values and duplicates from the collection.
   */
  @Override
  public void clear() {
    //clear root, clears the tree
    if (this.root != null) { //check if the tree is already empty
      this.root.setRight(null);
      this.root.setLeft(null);
      this.root = null;
    }
  }

  /**
   * testing the insert and insertHelper methods
   *
   * @return true or false if the test passes
   */
  public boolean test1(){
    //TESTING: insert and insertHelper
    BinarySearchTree<Integer> tree = new BinarySearchTree<>();

    //The root test:
    tree.insert(5);
    if (!(tree.root.getData() == 5 && tree.root.getRight() == null && tree.root.getUp() == null && tree.root.getLeft() == null)){
      return false;
    }

    //equal value to the left:
    tree.insert(5);
    if(!(tree.root.getLeft().getData() == 5 && tree.root.getLeft().getUp() == tree.root)){
      return false;
    }

    //greater value to the right:
    tree.insert(7);
    if (!(tree.root.getRight().getData() == 7 && tree.root.getRight().getUp() == tree.root)){
      return false;
    }

    //greater than the root and right child:
    tree.insert(9);
    if (!(tree.root.getRight().getRight().getData() == 9 && tree.root.getRight().getRight()
        .getUp() == tree.root.getRight())){
      return false;
    }

    //greater than the root but less than right child
    tree.insert(6);
    if(!(tree.root.getRight().getLeft().getData() == 6 && tree.root.getRight().getLeft()
        .getUp() == tree.root.getRight())){
      return false;
    }

    //null handling test
    try {
      tree.insert(null);
    } catch (NullPointerException e) {
      //yes!
    }

    return true;
  }

  /**
   * testing the contains and containsHelper methods
   *
   * @return true or false if the test passes
   */
  public boolean test2(){
    //TESTING: contains and containsHelper, using the tree above
    BinarySearchTree<String> tree = new BinarySearchTree<>();
    tree.insert("hello");
    tree.insert("hi");
    tree.insert("hey");
    tree.insert("sup");
    tree.insert("wassup");
    tree.insert("yo");

    //testing hello
    if(!tree.contains("hello")){
      return false;
    }

    //testing yo
    if(!tree.contains("yo")){
      return false;
    }

    //testing hi
    if(!tree.contains("hi")){
      return false;
    }

    //testing hey
    if(!tree.contains("hey")){
      return false;
    }

    //testing wassup
    if(!tree.contains("wassup")){
      return false;
    }

    if(!tree.contains("sup")){
      return false;
    }

    return true;
  }

  /**
   * testing the size and sizeHelper methods
   *
   * @return true or false if the test passes
   */
  public boolean test3(){

    //TESTING: size and sizeHelper, using the tree above
    BinarySearchTree<Boolean> tree = new BinarySearchTree<>();
    tree.insert(true);
    tree.insert(false);
    tree.insert(true);
    tree.insert(false);
    tree.insert(true);
    tree.insert(false);


    //test the entire tree
    if(tree.size() != 6){
      return false;
    }

    //test on an empty tree
    BinarySearchTree<String> empty = new BinarySearchTree<>();

    if (empty.size() != 0){
      return false;
    }

    //test on only root
    empty.insert("hi");
    if (empty.size() != 1) {
      return false;
    }

    return true;
  }

  /**
   * testing the isEmpty and clear methods
   *
   * @return true or false if the test passes
   */
  public boolean test4(){
    BinarySearchTree<Boolean> tree = new BinarySearchTree<>();
    tree.insert(true);
    tree.insert(false);
    tree.insert(true);
    tree.insert(false);
    tree.insert(true);
    tree.insert(false);

    //TESTING: isEmpty()

    //test on not empty tree
    if(tree.isEmpty()){
      return false;
    }

    //test on empty tree
    BinarySearchTree<String> empty2 = new BinarySearchTree<>();
    if(!empty2.isEmpty()){
      return false;
    }

    //TESTING: Clear()

    //test on not empty tree
    tree.clear();
    if(!tree.isEmpty()){
      return false;
    }

    return true;
  }

  /**
   * contains the tester methods
   *
   * @param args unused
   */
  public static void main(String[] args) {
    BinarySearchTree<Float> forTesting = new BinarySearchTree<>();

    System.out.println("test1: " + forTesting.test1());
    System.out.println("test2: " + forTesting.test2());
    System.out.println("test3: " + forTesting.test3());
    System.out.println("test4: " + forTesting.test4());

  }
}
