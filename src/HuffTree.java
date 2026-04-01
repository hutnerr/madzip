import java.util.PriorityQueue;

/**
 * Huffman Coding Tree.
 * 
 * @author OpenDSA Textbook & Hunter Baker
 */
class HuffTree implements Comparable {

  private HuffBaseNode root;

  /**
   * Leaf node constructor.
   * 
   * @param el The element
   * @param wt The weight
   */
  public HuffTree(byte el, int wt) {
    root = new HuffLeafNode(el, wt);
  }

  /**
   * Internal node constructor.
   * 
   * @param l The left node
   * @param r The right node
   * @param wt The weight
   */
  public HuffTree(HuffBaseNode l, HuffBaseNode r, int wt) {
    root = new HuffInternalNode(l, r, wt);
  }

  /**
   * Getter for the root node.
   * 
   * @return The root node
   */
  public HuffBaseNode root() {
    return root;
  }

  /**
   * Getter for the root weight.
   * 
   * @return The root's weight
   */
  public int weight() {
    return root.weight();
  }

  /**
   * Comparable method for HuffTrees.
   */
  public int compareTo(Object t) {
    HuffTree that = (HuffTree) t;
    if (root.weight() < that.weight()) {
      return -1;
    } else if (root.weight() == that.weight()) {
      return 0;
    } else {
      return 1;
    }
  }

  /**
   * Method to build a HuffTree.
   * 
   * @param heap The heap we're using to build
   * @return The
   */
  static HuffTree buildTree(PriorityQueue<HuffTree> heap) {
    HuffTree tmp1 = null;
    HuffTree tmp2 = null;
    HuffTree tmp3 = null;

    if (heap.size() == 0) {
      return new HuffTree((byte) 0, 0);
    }

    if (heap.size() == 1) {
      tmp2 = heap.remove();
      tmp3 = new HuffTree(tmp2.root(), null, tmp2.weight());
    }

    while (heap.size() > 1) { // While two items left
      tmp1 = heap.remove();
      tmp2 = heap.remove();
      tmp3 = new HuffTree(tmp1.root(), tmp2.root(), tmp1.weight() + tmp2.weight());
      heap.add(tmp3); // Return new tree to heap
    }
    return tmp3; // Return the tree
  }
}
