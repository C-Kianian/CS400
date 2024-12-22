/**
 * This class is a placeholder that defines a specific bst structure from its
 * constructor.  This is meant to help with the development and testing of the
 * rotation operation.  Once the rotation is working, you can change the base
 * class from this placeholder to a working BinarySearchTree implementation.
 */
public class BinarySearchTree_Placeholder <T extends Comparable<T>>
    implements SortedCollection<T> {

    protected BSTNode<T> root; // this is what BSTRotation will use

    /**
     * Creates a hard-coded binary search tree, the same as if you were to
     * insert the following String values into an empty tree in this order:
     * E, B, H, A, D, F, J, C, G, I, K
     */
    @SuppressWarnings("unchecked")
    BinarySearchTree_Placeholder() {
	root = (BSTNode<T>)
	    newTree("E",
		    newTree("B",
			    new BSTNode("A"),
			    newTree("D",
				    new BSTNode("C"),
				    null)),
		    newTree("H",
			    newTree("F",
				    null,
				    new BSTNode("G")),
			    newTree("J",
				    new BSTNode("I"),
				    new BSTNode("K"))));
    }

    // these methods are simple to implement for the hard-coded tree above
    
    public int size() {
	return 11;
    }
    public boolean isEmpty() {
	return false;
    }
    public void clear() {
	this.root = null;
    }

    // These methods are not needed or implemented within this placeholder
    
    public void insert(T data) throws NullPointerException {
	throw new UnsupportedOperationException("cannot call on placeholder");
    }
    protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
	throw new UnsupportedOperationException("cannot call on placeholder");
    }
    public boolean contains(Comparable<T> data) {
	throw new UnsupportedOperationException("cannot call on placeholder");
    }

    /**
     * Private helper method to quickly build hard-coded subtree in constructor.
     * This method create a new node containing data which is correctly linked
     * (with both parent and child references) with the left and right nodes.
     * @param data is stored within the newly created node
     * @param left is linked as the new node's left child
     * @param right is linked as the new node's right child
     * @return the newly created and linked node
     */
    private BSTNode<String>
	newTree(String data, BSTNode<String> left, BSTNode<String> right) {
	BSTNode<String> node = new BSTNode<>(data);
	node.setLeft(left);
	node.setRight(right);
	if(left != null) left.setUp(node);
	if(right != null) right.setUp(node);
	return node;
    }
    
}
