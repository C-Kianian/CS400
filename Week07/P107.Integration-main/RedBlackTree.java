/**
 * class for implementing a RBT
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {
  /**
   * Checks if a new red node in the RedBlackTree causes a red property violation by having a red
   * parent. If this is not the case, the method terminates without making any changes to the tree.
   * If a red property violation is detected, then the method repairs this violation and any
   * additional red property violations that are generated as a result of the applied repair
   * operation.
   *
   * @param newRedNode a newly inserted red node, or a node turned red by previous repair
   */
  protected void ensureRedProperty(RBTNode<T> newRedNode) {
    if (newRedNode.getUp() != null && newRedNode.getUp().getUp() != null && newRedNode.getUp()
        .isRed()) {
      //fix, parent + child r red, new node has grandparent
      RBTNode<T> grand = newRedNode.getUp().getUp();
      RBTNode<T> parent = newRedNode.getUp();
      boolean blkUnc = false;
      RBTNode<T> unc;
      int comp = newRedNode.getData().compareTo(grand.getData());
      if (comp <= 0) {//find the unc, on the right side
        if (grand.getRight() == null || !grand.getRight().isRed) {//black or null case, case 2 or 3
          blkUnc = true;
        }
        unc = grand.getRight();
      } else {//find the unc, on the left side
        if (grand.getLeft() == null || !grand.getLeft().isRed) {
          blkUnc = true;
        }
        unc = grand.getLeft();
      }
      if (blkUnc) {//has a black unc, check if line or skew case
        if (grand.getLeft() == parent || grand.getRight() == parent &&
            newRedNode.isRightChild()) {//line formation, case 2
          case2(parent, grand);
          ensureRedProperty(grand);
        } else { //skew/triangle formation, case 3
          rotate(newRedNode, parent);//perform rotation, make line
          case2(newRedNode, grand);
          ensureRedProperty(grand);
        }
      } else {//case one, red unc
        parent.flipColor();//make parent black
        unc.flipColor();//make unc black
        grand.flipColor();//make grandparent red
        ensureRedProperty(grand);//make sure no new violations
      }
    }
    //no violation, we r chilling, base case
  }

  /**
   * helper method for fixing violations where uncle is black and the new node is in a line
   *
   * @param grand  - grand of node that was added to the tree
   * @param parent - parent of the added node
   */
  private void case2(RBTNode<T> parent, RBTNode<T> grand) {
    rotate(parent, grand);//rotate
    grand.flipColor();//swap parent and grand colors
    parent.flipColor();
  }

  /**
   * adds a new node to the RBT and will fix any introduced violations
   *
   * @param data - the data to add to the RBT
   */
  @Override
  public void insert(T data) {
    if (data != null) {//ensures no null inputs
      if (this.isEmpty()) {//checks if new node will be the root
        RBTNode<T> RBnode = new RBTNode<>(data);
        if (RBnode.isRed()) {
          RBnode.flipColor();//ensure root is black
        }
        this.root = RBnode;//set the root
      } else {//node is not the root, time to insert
        RBTNode<T> node = new RBTNode<>(data);
        insertHelper(node, this.root); //insert
        ensureRedProperty(node);//ensure node is red, fix violations
      }
      if (this.root instanceof RBTNode<T>) {//change root to black
        if (((RBTNode<T>) this.root).isRed) {
          ((RBTNode<T>) this.root).flipColor();
        }
      }
    }
  }
}
