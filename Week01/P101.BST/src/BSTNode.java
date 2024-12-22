import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * This class represents a node for a Binary Search Tree that holds a single 
 * data value and is doubly linked: has a reference to its parent node and 
 * two references to its children nodes.
 */
public class BSTNode<T> {

    // stores the data value for the node
    protected T data;

    // reference to the node's parent
    protected BSTNode<T> up = null;
    // reference to the node's left child
    protected BSTNode<T> left = null;
    // reference to the node's right child
    protected BSTNode<T> right = null;

    /**
     * Constructor that creates a new node with the value data. Both parent 
     * and child references of the new node are initialized to null.
     * @param data the value the new node stores
     */
    public BSTNode(T data) { this.data = data; }

    /**
     * @return value stored in this node
     */
    public T getData() { return this.data; }

    /**
     * @return the reference to the left child of this node, 
     * or null if this node has no left child
     */
    public BSTNode<T> getLeft() { return this.left; }

    /**
     * @return the reference to the right child of this node, 
     * or null if this node has no right child
     */
    public BSTNode<T> getRight() { return this.right; }

    /**
     * @return the reference to the parent of this node, 
     * or null if it has no parent
     */
    public BSTNode<T> getUp() { return this.up; }

    /**
     * Gives this node a new value and deletes the old value.
     * @param newData the new value to store in this node
     */
    public void setData(T newData) { this.data = newData; }

    /**
     * Gives this node a new parent and deletes the old parent.
     * @param newParent the new parent for this node
     */
    public void setUp(BSTNode<T> newParent) { this.up = newParent; }

    /**
     * Gives this node a new left child and deletes the old left child.
     * @param newLeftChild the new left child for this node
     */
    public void setLeft(BSTNode<T> newLeftChild) { this.left = newLeftChild; }

    /**
     * Gives this node a new right child and deletes the old right child.
     * @param newRightChild the new right child for this node
     */
    public void setRight(BSTNode<T> newRightChild) {
	this.right = newRightChild;
    }
    
    /**
     * @return true when this node has a parent and is the right child of
     * that parent, otherwise return false
     */
    public boolean isRightChild() {
        return this.getUp() != null && this.getUp().getRight() == this;
    }

    /**
     * Returns a string representation for this node.
     * @return a string representation of the node's value
     */
    @Override
    public String toString() {
        return this.data.toString();
    }
    
    /**
     * Performs an level-order traversal of the subtree rooted at this node
     * and generates a string represeation of those nodes' contents.
     * @return a string of node values in level-order
     */
    public String toLevelOrderString() {
        // create a linked list that we'll use as a queue to store inprocessed nodes
        Queue<BSTNode<T>> nodeList = new LinkedList<>();
        // add this node to the queue first
        nodeList.add(this);
        // create the buffer to assemble the string efficiently
        StringBuffer sb = new StringBuffer();
        // add the bracket preceding the list of nodes to the buffer first
        sb.append("[ ");
        // keep processing nodes as long as we have any left on the queue
        while (!nodeList.isEmpty()) {
            // if it exists, add the left child of the head of the queue to the queue
            if (nodeList.peek().getLeft() != null) {
                nodeList.add(nodeList.peek().getLeft());
            }
            // if it exists, add the right child of the head of the queue to the queue
            if (nodeList.peek().getRight() != null) {
                nodeList.add(nodeList.peek().getRight());
            }
            // add the head of the queue to the string buffer and remove from queue
            sb.append(nodeList.poll().toString());
            // add a comma to separate values to the buffer, or close the bracket if
            // we've just added the last node to it
            if (nodeList.isEmpty()) {
                sb.append(" ]");
            } else {
                sb.append(", ");
            }
        }
        // return the string built with the string buffer
        return sb.toString();
    }

    /**
     * Performs an in-order traversal of the subtree rooted at this node
     * and generates a string representation of those nodes' contents.
     * @return a string of node value in in-order
     */
    public String toInOrderString() {
        // create a stack to keep track of unvisited nodes
        Stack<BSTNode<T>> stack = new Stack<>();
        // add root (this node) to the stack first
        stack.push(this);
        // follow the left child references and add all nodes on the path from this node
        // to its left-most descendant to the stack
        while (stack.peek().getLeft() != null) {
            stack.push(stack.peek().getLeft());
        }
        // create a buffer to assemble the string efficiently
        StringBuffer sb = new StringBuffer();
        // add the bracket preceding the list of nodes to the buffer first
        sb.append("[ ");
        // keep processing nodes as long as the stack is not empty
        while (!stack.isEmpty()) {
            // pop the top node from the stack
            BSTNode<T> current = stack.pop();
            // add popped node to the string
            sb.append(current.toString());
            // handle the right subtree of the popped node
            if (current.getRight() != null) {
                stack.push(current.getRight());
                while (stack.peek().getLeft() != null) {
                    stack.push(stack.peek().getLeft());
                }
            }
            // add a comma to separate values to the buffer, or close the bracket if
            // we've just added the last node to it
            if (!stack.isEmpty()) {
                sb.append(", ");
            } else {
                sb.append(" ]");
            }
        }
        // return the string built with the string buffer
        return sb.toString();
    }

}
