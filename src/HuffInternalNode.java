/**
 * Huffman Coding Tree internal node.
 * 
 * @author OpenDSA Textbook & Hunter Baker
 */
public class HuffInternalNode extends HuffBaseNode {

  private HuffBaseNode left;
  private HuffBaseNode right;

  /**
   * Constructor.
   * 
   * @param left The left node
   * @param right The right node
   * @param weight The combined weight
   */
  public HuffInternalNode(HuffBaseNode left, HuffBaseNode right, int weight) {
    super(weight);
    this.left = left;
    this.right = right;
  }

  /**
   * Getter for the left node.
   * 
   * @return the left child
   */
  public HuffBaseNode left() {
    return left;
  }

  /**
   * Getter for the right node.
   * 
   * @return the right child
   */
  public HuffBaseNode right() {
    return right;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

}
